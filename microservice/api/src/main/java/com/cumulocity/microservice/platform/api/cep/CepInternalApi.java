package com.cumulocity.microservice.platform.api.cep;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.cep.CepApi;
import com.cumulocity.sdk.client.cep.CepModuleCollection;
import com.cumulocity.sdk.client.cep.notification.CepCustomNotificationsSubscriber;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service("cepInternalApi")
public class CepInternalApi implements CepApi{

    @Autowired(required = false)
    @Qualifier("cepApi")
    private CepApi cepApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    @Override
    public CepCustomNotificationsSubscriber getCustomNotificationsSubscriber() {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepCustomNotificationsSubscriber>() {
            @Override
            public CepCustomNotificationsSubscriber call() throws Exception {
                return cepApi.getCustomNotificationsSubscriber();
            }
        });
    }

    @Override
    public CepModuleRepresentation get(final String id) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.get(id);
            }
        });
    }

    @Override
    public String getText(final String id) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return cepApi.getText(id);
            }
        });
    }

    @Override
    public CepModuleRepresentation create(final InputStream content) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.create(content);
            }
        });
    }

    @Override
    public CepModuleRepresentation create(final String content) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.create(content);
            }
        });
    }

    @Override
    public CepModuleRepresentation update(final String id, final InputStream content) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.update(id, content);
            }
        });
    }

    @Override
    public CepModuleRepresentation update(final String id, final String content) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.update(id, content);
            }
        });
    }

    @Override
    public CepModuleRepresentation update(final CepModuleRepresentation module) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.update(module);
            }
        });
    }

    @Override
    public CepModuleCollection getModules() {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleCollection>() {
            @Override
            public CepModuleCollection call() throws Exception {
                return cepApi.getModules();
            }
        });
    }

    @Override
    public void delete(final CepModuleRepresentation module) {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                cepApi.delete(module);
                return null;
            }
        });
    }

    @Override
    public void delete(final String id) {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                cepApi.delete(id);
                return null;
            }
        });
    }

    @Override
    public <T> T health(final Class<T> clazz) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<T>() {
            public T call() {
                return cepApi.health(clazz);
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(cepApi, "Bean of type: " + CepApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
