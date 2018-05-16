package com.cumulocity.sdk.client.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.tenant.*;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TenantOptionApiImplTest {
    private static final String HOST = "http://test";
    private static final String TENANT_OPTION_COLLECTION_URL = "/tenant/options";
    private static final String TENANT_OPTION_FOR_CATEGORY = TENANT_OPTION_COLLECTION_URL + "/{category}";
    private static final String TENANT_OPTION_FOR_CATEGORY_AND_KEY = TENANT_OPTION_COLLECTION_URL + "/{category}/{key}";


    private static final String CATEGORY = "test.category";
    private static final String KEY = "test.key";
    private static final String KEY2 = "test.key2";


    private static final int DEFAULT_PAGE_SIZE = 555;

    private TenantOptionApi optionApi;

    private TenantApiRepresentation tenantsApiRepresentation = new TenantApiRepresentation();

    @Mock
    private RestConnector restConnector;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        OptionCollectionRepresentation optionCollectionRepresentation = new OptionCollectionRepresentation();
        optionCollectionRepresentation.setSelf(TENANT_OPTION_COLLECTION_URL);

        tenantsApiRepresentation.setOptions(optionCollectionRepresentation);
        tenantsApiRepresentation.setTenantOptionForCategoryAndKey(TENANT_OPTION_FOR_CATEGORY_AND_KEY);
        tenantsApiRepresentation.setTenantOptionsForCategory(TENANT_OPTION_FOR_CATEGORY);

        optionApi = new TenantOptionApiImpl(restConnector, HOST, tenantsApiRepresentation, DEFAULT_PAGE_SIZE);
    }

    @Test
    public void shouldRetrieveOptionRep() throws SDKException {
        //Given
        OptionPK optionPK = new OptionPK(CATEGORY, KEY);
        OptionRepresentation optionRep = new OptionRepresentation();
        when(restConnector.get(TENANT_OPTION_COLLECTION_URL + "/" + CATEGORY + "/" + KEY, OptionMediaType.OPTION, OptionRepresentation.class)).thenReturn(optionRep);

        //When
        OptionRepresentation retrieved = optionApi.getOption(optionPK);

        //Then
        assertThat(retrieved, sameInstance(optionRep));
    }

    @Test
    public void shouldRetrieveOptionCollectionResource() throws SDKException {
        // Given
        TenantOptionCollection expected = new TenantOptionCollectionImpl(restConnector, TENANT_OPTION_COLLECTION_URL,
                DEFAULT_PAGE_SIZE);

        //When
        TenantOptionCollection optionCollection = optionApi.getOptions();

        //Then
        assertThat(optionCollection, is(expected));
    }

    @Test
    public void shouldRetrieveOptionsForCategory() {
        // Given
        List<OptionRepresentation> expectedOptions = getExpectedOptions();

        when(restConnector.get(
                TENANT_OPTION_FOR_CATEGORY.replace("{category}", CATEGORY),
                CumulocityMediaType.APPLICATION_JSON_TYPE,
                OptionsRepresentation.class))
                .thenReturn(getOptionsRepresentation());

        // when
        List<OptionRepresentation> returnedOptions = optionApi.getAllOptionsForCategory(CATEGORY);

        // then
        assertEquals(expectedOptions, returnedOptions);
    }

    private List<OptionRepresentation> getExpectedOptions() {
        List<OptionRepresentation> expectedOptions = new ArrayList();
        expectedOptions.add(OptionRepresentation.asOptionRepresetation(CATEGORY, KEY, "val1"));
        expectedOptions.add(OptionRepresentation.asOptionRepresetation(CATEGORY, KEY2, "val2"));
        return expectedOptions;
    }

    private OptionsRepresentation getOptionsRepresentation() {
        Map<String, String> categoryOptions = new HashMap<>();
        categoryOptions.put(KEY, "val1");
        categoryOptions.put(KEY2, "val2");
        return OptionsRepresentation.builder().properties(categoryOptions).build();
    }

    @Test
    public void shouldCreateOptionInCollection() throws SDKException {
        //Given
        OptionRepresentation optionRepresentation = new OptionRepresentation();
        OptionRepresentation created = new OptionRepresentation();
        when(restConnector.post(TENANT_OPTION_COLLECTION_URL, OptionMediaType.OPTION, optionRepresentation)).thenReturn(created);

        // When
        OptionRepresentation result = optionApi.save(optionRepresentation);

        // Then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void shouldDeleteOption() throws SDKException {
        //Given
        OptionPK optionPK = new OptionPK(CATEGORY, KEY);

        // When
        optionApi.delete(optionPK);

        // Then
        verify(restConnector, times(1)).delete(TENANT_OPTION_COLLECTION_URL + "/" + CATEGORY + "/" + KEY);
    }

}
