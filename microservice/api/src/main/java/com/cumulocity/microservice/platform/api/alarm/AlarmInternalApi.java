package com.cumulocity.microservice.platform.api.alarm;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.buffering.Future;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service("alarmInternalApi")
public class AlarmInternalApi implements AlarmApi{

    @Autowired(required = false)
    @Qualifier("alarmApi")
    private AlarmApi alarmApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    @Override
    public AlarmRepresentation getAlarm(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AlarmRepresentation>() {
            @Override
            public AlarmRepresentation call() throws Exception {
                return alarmApi.getAlarm(gid);
            }
        });
    }

    @Override
    public AlarmRepresentation create(final AlarmRepresentation alarm) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AlarmRepresentation>() {
            @Override
            public AlarmRepresentation call() throws Exception {
                return alarmApi.create(alarm);
            }
        });
    }

    @Override
    public Future createAsync(final AlarmRepresentation alarm) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<Future>() {
            @Override
            public Future call() throws Exception {
                return alarmApi.createAsync(alarm);
            }
        });
    }

    @Override
    public AlarmRepresentation updateAlarm(AlarmRepresentation alarm) throws SDKException {
        return update(alarm);
    }

    @Override
    public AlarmRepresentation update(final AlarmRepresentation alarm) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AlarmRepresentation>() {
            @Override
            public AlarmRepresentation call() throws Exception {
                return alarmApi.update(alarm);
            }
        });
    }

    @Override
    public AlarmCollection getAlarms() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AlarmCollection>() {
            @Override
            public AlarmCollection call() throws Exception {
                return alarmApi.getAlarms();
            }
        });
    }

    @Override
    public AlarmCollection getAlarmsByFilter(final AlarmFilter filter) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<AlarmCollection>() {
            @Override
            public AlarmCollection call() throws Exception {
                return alarmApi.getAlarmsByFilter(filter);
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(alarmApi, "Bean of type: " + AlarmApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
