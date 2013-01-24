package com.cumulocity.sdk.client.alarm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AlarmApiImpl.class)
public class AlarmApiImplTest {

    private static final String ALARM_COLLECTION_URL = "/alarm/alarms";

    private static final String PATH_TO_PLATFORM_API = "path_to_platforms";

    private static final int DEFAULT_PAGE_SIZE = 555;

    private AlarmApi alarmApi;

    private AlarmsApiRepresentation alarmsApiRepresentation = new AlarmsApiRepresentation();

    private Date from;

    private Date to;

    @Mock
    private RestConnector restConnector;

    private TemplateUrlParser templateUrlParser = new TemplateUrlParser();

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        alarmApi = new AlarmApiImpl(restConnector, templateUrlParser, PATH_TO_PLATFORM_API, DEFAULT_PAGE_SIZE);

        AlarmCollectionRepresentation alarmCollectionRepresentation = new AlarmCollectionRepresentation();
        alarmCollectionRepresentation.setSelf(ALARM_COLLECTION_URL);
        alarmsApiRepresentation.setAlarms(alarmCollectionRepresentation);

        alarmsApiRepresentation.setAlarmsForSource("/alarms?source={source}");
        alarmsApiRepresentation.setAlarmsForSourceAndStatus("/alarms?source={source}&status={status}");
        alarmsApiRepresentation
                .setAlarmsForSourceAndStatusAndTime("/alarms?source={source}&status={status}&dateFrom={dateFrom}&dateTo={dateTo}");
        alarmsApiRepresentation.setAlarmsForSourceAndTime("/alarms?source={source}&dateFrom={dateFrom}&dateTo={dateTo}");
        alarmsApiRepresentation.setAlarmsForStatus("/alarms?status={status}");
        alarmsApiRepresentation.setAlarmsForStatusAndTime("/alarms?status={status}&dateFrom={dateFrom}&dateTo={dateTo}");
        alarmsApiRepresentation.setAlarmsForTime("/alarms?dateFrom={dateFrom}&dateTo={dateTo}");

        PlatformApiRepresentation platformApiRepresentation = new PlatformApiRepresentation();
        platformApiRepresentation.setAlarm(alarmsApiRepresentation);
        when(restConnector.get(PATH_TO_PLATFORM_API, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class)).thenReturn(
                platformApiRepresentation);

        from = new Date(10000L);
        to = new Date(20000L);
    }

    @Test
    public void shouldRetrieveAlarmRep() throws SDKException {
        //Given 
        GId gid = new GId("global_id");
        AlarmRepresentation alarmRep = new AlarmRepresentation();
        when(restConnector.get(ALARM_COLLECTION_URL + "/global_id", AlarmMediaType.ALARM, AlarmRepresentation.class)).thenReturn(alarmRep);

        //When
        AlarmRepresentation retrieved = alarmApi.getAlarm(gid);

        //Then
        assertThat(retrieved, sameInstance(alarmRep));
    }

    @Test
    public void shouldUpdateAlarmRep() throws SDKException {
        //Given
        AlarmRepresentation alarmToUpdate = new AlarmRepresentation();
        alarmToUpdate.setId(new GId("global_id"));
        alarmToUpdate.setStatus("STATUS");
        AlarmRepresentation alarmRep = new AlarmRepresentation();
        when(
                restConnector.put(eq(ALARM_COLLECTION_URL + "/global_id"), eq(AlarmMediaType.ALARM),
                        argThat(hasOnlyUpdatableFields(alarmToUpdate)))).thenReturn(alarmRep);

        //When
        AlarmRepresentation updated = alarmApi.updateAlarm(alarmToUpdate);

        //Then
        assertThat(updated, sameInstance(alarmRep));
    }

    private Matcher<AlarmRepresentation> hasOnlyUpdatableFields(final AlarmRepresentation alarm) {
        return new TypeSafeMatcher<AlarmRepresentation>() {

            @Override
            public boolean matchesSafely(AlarmRepresentation item) {
                if (item.getId() != null) {
                    return false;
                }
                if (!item.getStatus().equals(alarm.getStatus())) {
                    return false;
                }
                return true;

            }

            @Override
            public void describeTo(Description description) {
                description.appendText("an alarm representation having only updatable fields, as ").appendValue(alarm);
            }
        };
    }

    @Test
    public void shouldRetrieveAlarmCollectionResource() throws SDKException {
        // Given
        PagedCollectionResource<AlarmCollectionRepresentation> expected = new AlarmCollectionImpl(restConnector, ALARM_COLLECTION_URL,
                DEFAULT_PAGE_SIZE);

        //When
        PagedCollectionResource<AlarmCollectionRepresentation> alarmCollection = alarmApi.getAlarms();

        //Then
        assertThat(alarmCollection, is(expected));
    }

    @Test
    public void testCreateAlarmInCollection() throws SDKException {
        //Given
        AlarmRepresentation alarmRepresentation = new AlarmRepresentation();
        AlarmRepresentation created = new AlarmRepresentation();
        when(restConnector.post(ALARM_COLLECTION_URL, AlarmMediaType.ALARM, alarmRepresentation)).thenReturn(created);

        // When 
        AlarmRepresentation result = alarmApi.create(alarmRepresentation);

        // Then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void shouldGetCollectionByFilterStatusAndSource() throws Exception {
        // Given
        String gidValue = "mySource";
        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(new GId(gidValue));

        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(restConnector,
                "/alarms?source=" + gidValue + "&status=" + CumulocityAlarmStatuses.ACKNOWLEDGED.toString(), DEFAULT_PAGE_SIZE).thenReturn(
                expected);

        AlarmFilter alarmFilter = new AlarmFilter().byStatus(status).bySource(source);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByFilterStatus() throws Exception {
        // Given
        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;

        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(restConnector,
                "/alarms?status=" + CumulocityAlarmStatuses.ACKNOWLEDGED.toString(), DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byStatus(status);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByFilterSource() throws Exception {
        // Given
        String gidValue = "mySource";
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(new GId(gidValue));

        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(restConnector, "/alarms?source=" + gidValue, DEFAULT_PAGE_SIZE).thenReturn(
                expected);

        AlarmFilter alarmFilter = new AlarmFilter().bySource(source);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByTime() throws Exception {
        // Given
        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(
                restConnector,
                "/alarms?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byDate(from, to);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByFromTime() throws Exception {
        // Given
        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(restConnector,
                "/alarms?dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byFromDate(from);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByTimeAndStatus() throws Exception {
        // Given
        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;
        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(
                restConnector,
                "/alarms?status=" + status.toString() + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from))
                        + "&dateTo=" + TemplateUrlParser.encode(DateConverter.date2String(to)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byDate(from, to).byStatus(status);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByFromTimeAndStatus() throws Exception {
        // Given
        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;
        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(restConnector,
                "/alarms?status=" + status.toString() + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)),
                DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byFromDate(from).byStatus(status);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByTimeAndSource() throws Exception {
        // Given
        String gidValue = "mySource";
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(new GId(gidValue));

        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(
                restConnector,
                "/alarms?source=" + gidValue + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byDate(from, to).bySource(source);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByFromTimeAndSource() throws Exception {
        // Given
        String gidValue = "mySource";
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(new GId(gidValue));

        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(restConnector,
                "/alarms?source=" + gidValue + "&dateFrom=" + TemplateUrlParser.encode(DateConverter.date2String(from)), DEFAULT_PAGE_SIZE)
                .thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byFromDate(from).bySource(source);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByTimeAndSourceAndStatus() throws Exception {
        // Given
        String gidValue = "mySource";
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(new GId(gidValue));

        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;

        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(
                restConnector,
                "/alarms?source=" + gidValue + "&status=" + status.toString() + "&dateFrom="
                        + TemplateUrlParser.encode(DateConverter.date2String(from)) + "&dateTo="
                        + TemplateUrlParser.encode(DateConverter.date2String(to)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byDate(from, to).bySource(source).byStatus(status);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByFromTimeAndSourceAndStatus() throws Exception {
        // Given
        String gidValue = "mySource";
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(new GId(gidValue));

        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;

        AlarmCollectionImpl expected = new AlarmCollectionImpl(restConnector, "url", DEFAULT_PAGE_SIZE);
        whenNew(AlarmCollectionImpl.class).withArguments(
                restConnector,
                "/alarms?source=" + gidValue + "&status=" + status.toString() + "&dateFrom="
                        + TemplateUrlParser.encode(DateConverter.date2String(from)), DEFAULT_PAGE_SIZE).thenReturn(expected);

        AlarmFilter alarmFilter = new AlarmFilter().byFromDate(from).bySource(source).byStatus(status);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat((AlarmCollectionImpl) alarms, is(expected));
    }

    @Test
    public void shouldGetCollectionByEmptyFilter() throws Exception {
        // Given
        AlarmFilter alarmFilter = new AlarmFilter();

        PagedCollectionResource<AlarmCollectionRepresentation> expected = new AlarmCollectionImpl(restConnector, ALARM_COLLECTION_URL,
                DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<AlarmCollectionRepresentation> alarms = alarmApi.getAlarmsByFilter(alarmFilter);

        // Then
        assertThat(alarms, is(expected));
    }
}
