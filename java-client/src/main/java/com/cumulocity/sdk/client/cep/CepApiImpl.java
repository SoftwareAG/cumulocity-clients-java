package com.cumulocity.sdk.client.cep;

import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.cep.notification.CepCustomNotificationsSubscriber;

public class CepApiImpl implements CepApi {


    private final PlatformParameters platformParameters;

    public CepApiImpl(PlatformParameters platformParameters) {
        this.platformParameters = platformParameters;
    }

    @Override
    public CepCustomNotificationsSubscriber getCustomNotificationsSubscriber() {
        return new CepCustomNotificationsSubscriber(platformParameters);
    }

}
