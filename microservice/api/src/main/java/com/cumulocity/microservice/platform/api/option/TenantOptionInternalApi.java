package com.cumulocity.microservice.platform.api.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import com.cumulocity.sdk.client.option.TenantOptionCollection;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

public class TenantOptionInternalApi {

    @Autowired(required = false)
    private TenantOptionApi optionApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public OptionRepresentation getOption(final OptionPK optionPK) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<OptionRepresentation>() {
            @Override
            public OptionRepresentation call() throws Exception {
                return optionApi.getOption(optionPK);
            }
        });
    }

    public OptionRepresentation save(final OptionRepresentation option) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<OptionRepresentation>() {
            @Override
            public OptionRepresentation call() throws Exception {
                return optionApi.save(option);
            }
        });
    }

    public Future saveAsync(final OptionRepresentation option) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<Future>() {
            @Override
            public Future call() throws Exception {
                return optionApi.saveAsync(option);
            }
        });
    }

    public TenantOptionCollection getOptions() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<TenantOptionCollection>() {
            @Override
            public TenantOptionCollection call() throws Exception {
                return optionApi.getOptions();
            }
        });
    }

    public List<OptionRepresentation> getAllOptionsForCategory(final String category) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<List<OptionRepresentation>>() {
            @Override
            public List<OptionRepresentation> call() throws Exception {
                return optionApi.getAllOptionsForCategory(category);
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(optionApi, "Bean of type: " + TenantOptionApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }
}
