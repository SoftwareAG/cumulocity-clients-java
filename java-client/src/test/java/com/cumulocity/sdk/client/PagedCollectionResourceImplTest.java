package com.cumulocity.sdk.client;

import static com.cumulocity.test.matchers.UrlMatcher.matchesUrl;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.sun.jersey.api.client.ClientResponse;

public class PagedCollectionResourceImplTest {

    private static final Class<BaseCollectionRepresentation> CLAZZ = BaseCollectionRepresentation.class;

    private static final CumulocityMediaType MEDIA_TYPE = CumulocityMediaType.ERROR_MESSAGE;

    private static final String URL = "http://hello.com/blah/blah";

    private static final int PAGE_SIZE = 333;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    PagedCollectionResourceImpl<BaseCollectionRepresentation> target;

    @Mock
    ClientResponse clientResponse;

    @Mock
    private RestConnector restConnector;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        target = createPagedCollectionResource(restConnector, URL, PAGE_SIZE, MEDIA_TYPE, CLAZZ);

        MeasurementCollectionRepresentation measurementCollectionRepresentation = new MeasurementCollectionRepresentation();
        when(clientResponse.getStatus()).thenReturn(200);
        when(clientResponse.getEntity(MeasurementCollectionRepresentation.class)).thenReturn(measurementCollectionRepresentation);
    }

    @Test
    public void shouldGetAndAddPageSizeParamWhenNoOtherParams() throws SDKException {
        // Given
        BaseCollectionRepresentation expectedRepresentation = new BaseCollectionRepresentation();
        when(restConnector.get(URL + "?pageSize=" + PAGE_SIZE, MEDIA_TYPE, CLAZZ)).thenReturn(expectedRepresentation);

        // When
        BaseCollectionRepresentation page = target.get();

        // Then        
        assertThat(page, sameInstance(expectedRepresentation));
    }

    @Test
    public void shouldGetAndAddPageSizeParamWhenOtherParamsPresent() throws SDKException {
        // Given
        String myUrlWithOtherParam = URL + "?param=value";
        target = createPagedCollectionResource(restConnector, myUrlWithOtherParam, PAGE_SIZE, MEDIA_TYPE, CLAZZ);
        BaseCollectionRepresentation expectedRepresentation = new BaseCollectionRepresentation();
        String expectedUrl = myUrlWithOtherParam + "&pageSize=" + PAGE_SIZE;

        when(restConnector.get(argThat(matchesUrl(expectedUrl)), eq(MEDIA_TYPE), eq(CLAZZ))).thenReturn(expectedRepresentation);

        // When
        BaseCollectionRepresentation page = target.get();

        // Then        
        assertThat(page, sameInstance(expectedRepresentation));
    }

    @Test
    public void shouldGetAndReplaceAlreadyPresentPageSizeParam() throws SDKException {
        // Given
        String myUrlWithOtherParamAndPageSize = URL + "?param1=value1&pageSize=15&param2=value2";
        target = createPagedCollectionResource(restConnector, myUrlWithOtherParamAndPageSize, PAGE_SIZE, MEDIA_TYPE, CLAZZ);
        BaseCollectionRepresentation expectedRepresentation = new BaseCollectionRepresentation();
        String expectedUrl = URL + "?param1=value1&param2=value2&pageSize=" + PAGE_SIZE;

        when(restConnector.get(argThat(matchesUrl(expectedUrl)), eq(MEDIA_TYPE), eq(CLAZZ))).thenReturn(expectedRepresentation);

        // When
        BaseCollectionRepresentation page = target.get();

        // Then        
        assertThat(page, sameInstance(expectedRepresentation));
    }

    @Test
    public void shouldGetPageWhenNoParamsPresent() throws SDKException {
        // Given
        BaseCollectionRepresentation input = new BaseCollectionRepresentation();
        String myUrl = URL + "/measuremnt/measurments";
        input.setSelf(myUrl);

        String expectedUrl = myUrl + "?pageSize=" + PAGE_SIZE + "&currentPage=5";
        BaseCollectionRepresentation expectedRep = new BaseCollectionRepresentation();
        when(restConnector.get(argThat(matchesUrl(expectedUrl)), eq(MEDIA_TYPE), eq(CLAZZ))).thenReturn(expectedRep);

        // When
        BaseCollectionRepresentation page = target.getPage(input, 5);

        // Then        
        assertThat(page, sameInstance(expectedRep));
    }

    @Test
    public void shouldGetPageWhenOtherParamsAlreadyPresent() throws SDKException {
        // Given
        BaseCollectionRepresentation input = new BaseCollectionRepresentation();
        String myUrl = URL + "/measuremnt/measurments?param1=value1";
        input.setSelf(myUrl);

        String expectedUrl = myUrl + "&pageSize=" + PAGE_SIZE + "&currentPage=5";
        BaseCollectionRepresentation expectedRep = new BaseCollectionRepresentation();
        when(restConnector.get(argThat(matchesUrl(expectedUrl)), eq(MEDIA_TYPE), eq(CLAZZ))).thenReturn(expectedRep);

        // When
        BaseCollectionRepresentation page = target.getPage(input, 5);

        // Then        
        assertThat(page, sameInstance(expectedRep));
    }

    @Test
    public void shouldGetPageAndReplaceAlreadyPresentPageSizeAndCurrentPageParams() throws SDKException {
        // Given
        BaseCollectionRepresentation input = new BaseCollectionRepresentation();
        String myUrl = URL + "/measuremnt/measurments?param1=value1&pageSize=123&currentPage=234";
        input.setSelf(myUrl);

        String expectedUrl = URL + "/measuremnt/measurments?param1=value1" + "&pageSize=" + PAGE_SIZE + "&currentPage=6";
        BaseCollectionRepresentation expectedRep = new BaseCollectionRepresentation();
        when(restConnector.get(argThat(matchesUrl(expectedUrl)), eq(MEDIA_TYPE), eq(CLAZZ))).thenReturn(expectedRep);

        // When
        BaseCollectionRepresentation page = target.getPage(input, 6);

        // Then        
        assertThat(page, sameInstance(expectedRep));
    }

    @Test(expected = SDKException.class)
    public void shouldGetPageValidInput() throws SDKException {
        // Given
        BaseCollectionRepresentation input = null;

        // When
        target.getPage(input, 5);

        // Then 
        fail();
    }

    @Test
    public void shouldGetNextPage() throws SDKException {
        //Given
        String url = "/measuremnt/measurments";
        BaseCollectionRepresentation input = new BaseCollectionRepresentation();
        input.setNext(url);
        BaseCollectionRepresentation expectedResult = new BaseCollectionRepresentation();

        when(restConnector.get(url, MEDIA_TYPE, CLAZZ)).thenReturn(expectedResult);

        //when
        BaseCollectionRepresentation nextPage = target.getNextPage(input);

        // Then
        assertThat(nextPage, is(expectedResult));
    }

    @Test
    public void shouldGetNextPageInvalidInput() throws SDKException {
        // Given
        BaseCollectionRepresentation input = new BaseCollectionRepresentation();

        // When
        BaseCollectionRepresentation nextPage = target.getNextPage(input);

        // Then
        assertNull(nextPage);
    }

    @Test
    public void shouldGetPreviousPage() throws SDKException {
        // Given
        String url = "/measuremnt/measurments";
        BaseCollectionRepresentation input = new BaseCollectionRepresentation();
        input.setPrev(url);

        BaseCollectionRepresentation expectedResult = new BaseCollectionRepresentation();
        when(restConnector.get(url, MEDIA_TYPE, CLAZZ)).thenReturn(expectedResult);

        // When
        BaseCollectionRepresentation prevPage = target.getPreviousPage(input);

        // Then
        assertThat(prevPage, is(expectedResult));
    }

    @Test
    public void shouldGetPreviousPageInvalidInput() throws SDKException {
        // Given
        BaseCollectionRepresentation input = new BaseCollectionRepresentation();

        // When
        BaseCollectionRepresentation prevPage = target.getPreviousPage(input);

        // Then
        assertNull(prevPage);
    }

    @Test
    public void shouldEqualsIfPageSizeAndUrlAndMediaTypeAndRepresentationClassEqual() throws Exception {
        // Given
        PagedCollectionResourceImpl<BaseCollectionRepresentation> equalObject = createPagedCollectionResource(restConnector, URL,
                PAGE_SIZE, MEDIA_TYPE, CLAZZ);

        //When
        boolean equals = target.equals(equalObject);

        //Then
        assertThat(equals, is(true));
    }

    @Test
    public void shouldNotEqualsIfAnyOfParamsIsDifferent() throws Exception {
        // Given
        String differentUrl = "differentUrl";
        PagedCollectionResourceImpl<BaseCollectionRepresentation> equalObject = createPagedCollectionResource(restConnector, differentUrl,
                PAGE_SIZE, MEDIA_TYPE, CLAZZ);

        //When
        boolean equals = target.equals(equalObject);

        //Then
        assertThat(equals, is(false));
    }

    @Test
    public void shouldNotEqualsIfDifferentClass() throws Exception {
        //When
        boolean equals = target.equals(new Object());

        //Then
        assertThat(equals, is(false));
    }

    @Test
    public void shouldNotEqualsIfAnyOfFieldsIsNull() throws Exception {
        target = createPagedCollectionResource(restConnector, null, PAGE_SIZE, MEDIA_TYPE, CLAZZ);

        //When
        boolean equals = target.equals(target);

        //Then
        assertThat(equals, is(false));
    }

    @Test
    public void shouldHaveProperHashCode() throws Exception {
        // Given
        int expectedHashCode = MEDIA_TYPE.hashCode() ^ CLAZZ.hashCode() ^ PAGE_SIZE ^ URL.hashCode();

        //When
        int hashCode = target.hashCode();

        //Then
        assertThat(hashCode, is(expectedHashCode));
    }

    private PagedCollectionResourceImpl<BaseCollectionRepresentation> createPagedCollectionResource(RestConnector restConnector,
            String url, int pageSize, final CumulocityMediaType mediaType, final Class<BaseCollectionRepresentation> clazz) {
        return new PagedCollectionResourceImpl<BaseCollectionRepresentation>(restConnector, url, pageSize) {
            @Override
            protected CumulocityMediaType getMediaType() {
                return mediaType;
            }

            @Override
            protected Class<BaseCollectionRepresentation> getResponseClass() {
                return clazz;
            }
        };
    }
}
