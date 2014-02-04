package com.cumulocity.sdk.client.cep;

import com.cumulocity.sdk.client.cep.notification.CepCustomNotificationsSubscriber;

public interface CepApi {

    CepCustomNotificationsSubscriber getCustomNotificationsSubscriber();
}
