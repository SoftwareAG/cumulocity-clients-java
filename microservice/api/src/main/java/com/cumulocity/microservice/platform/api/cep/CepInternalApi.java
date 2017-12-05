package com.cumulocity.microservice.platform.api.cep;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.cep.CepApi;
import com.cumulocity.sdk.client.cep.CepModuleCollection;
import com.cumulocity.sdk.client.cep.notification.CepCustomNotificationsSubscriber;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class CepInternalApi {

    @Autowired(required = false)
    private CepApi cepApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public CepCustomNotificationsSubscriber getCustomNotificationsSubscriber() {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepCustomNotificationsSubscriber>() {
            @Override
            public CepCustomNotificationsSubscriber call() throws Exception {
                return cepApi.getCustomNotificationsSubscriber();
            }
        });
    }

    public CepModuleRepresentation get(final String id) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.get(id);
            }
        });
    }

    public String getText(final String id) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return cepApi.getText(id);
            }
        });
    }

    public CepModuleRepresentation create(final String content) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.create(content);
            }
        });
    }

    public CepModuleRepresentation update(final String id, final String content) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.update(id, content);
            }
        });
    }

    public CepModuleRepresentation update(final CepModuleRepresentation module) {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleRepresentation>() {
            @Override
            public CepModuleRepresentation call() throws Exception {
                return cepApi.update(module);
            }
        });
    }

    public CepModuleCollection getModules() {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<CepModuleCollection>() {
            @Override
            public CepModuleCollection call() throws Exception {
                return cepApi.getModules();
            }
        });
    }

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

    public String health() {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<String>() {
            public String call() {
                return cepApi.health();
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(cepApi, "Bean of type: " + CepApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
