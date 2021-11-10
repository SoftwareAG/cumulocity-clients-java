/*
~ Copyright (c) 2012-2021 Cumulocity GmbH
~ Copyright (c) 2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA,
~ and/or its subsidiaries and/or its affiliates and/or their licensors.
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~     http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
*/

package com.cumulocity.lpwan.codec.service;

import com.cumulocity.lpwan.codec.model.DecodeRequest;
import com.cumulocity.lpwan.codec.model.DecodeResponse;
import com.cumulocity.lpwan.codec.Decoder;
import com.cumulocity.lpwan.codec.exception.DecoderException;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionRemovedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CodecService {

    @Autowired
    private Decoder decoder;

    /**
     * This method should decode the payload received by a particular device.
     *
     * @param decode
     * @return DecodedData
     */
    public DecodeResponse decode(DecodeRequest decode) throws DecoderException {
        log.debug("Forwarding decoding request for the device with Id {} with payload {}", decode.getDeviceId(), decode.getPayload());
        return decoder.decode(null, null, null);
    }

    /**
     * This method should register a device type upon subscribing the codec microservice.
     *
     * @param event
     */
    @EventListener
    private void registerDeviceTypes(MicroserviceSubscriptionAddedEvent event) {
        log.info("Creating device types on codec microservice subscription");
    }

    /**
     * This method should delete a device type upon unsubscribing the codec microservice.
     *
     * @param event
     */
    @EventListener
    private void unregisterDeviceTypes(MicroserviceSubscriptionRemovedEvent event) {
        log.info("Deleting device types on codec microservice unsubscription");
    }
}
