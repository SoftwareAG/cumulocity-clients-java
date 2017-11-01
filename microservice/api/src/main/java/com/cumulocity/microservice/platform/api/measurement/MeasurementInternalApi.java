package com.cumulocity.microservice.platform.api.measurement;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.cumulocity.sdk.client.measurement.MeasurementCollection;
import com.cumulocity.sdk.client.measurement.MeasurementFilter;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class MeasurementInternalApi {

    @Autowired(required = false)
    private MeasurementApi measurementApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public MeasurementRepresentation getMeasurement(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<MeasurementRepresentation>() {
            @Override
            public MeasurementRepresentation call() throws Exception {
                return measurementApi.getMeasurement(gid);
            }
        });
    }

    public MeasurementRepresentation create(final MeasurementRepresentation measurement) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<MeasurementRepresentation>() {
            @Override
            public MeasurementRepresentation call() throws Exception {
                return measurementApi.create(measurement);
            }
        });
    }

    public void createWithoutResponse(final MeasurementRepresentation measurementRepresentation) throws SDKException {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                measurementApi.createWithoutResponse(measurementRepresentation);
                return null;
            }
        });
    }

    public Future createAsync(final MeasurementRepresentation measurement) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<Future>() {
            @Override
            public Future call() throws Exception {
                return measurementApi.createAsync(measurement);
            }
        });
    }

    public void delete(final MeasurementRepresentation measurement) throws SDKException {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                measurementApi.delete(measurement);
                return null;
            }
        });
    }

    public MeasurementCollection getMeasurements() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<MeasurementCollection>() {
            @Override
            public MeasurementCollection call() throws Exception {
                return measurementApi.getMeasurements();
            }
        });
    }

    public MeasurementCollection getMeasurementsByFilter(final MeasurementFilter filter) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<MeasurementCollection>() {
            @Override
            public MeasurementCollection call() throws Exception {
                return measurementApi.getMeasurementsByFilter(filter);
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(measurementApi, "Bean of type: " + MeasurementApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
