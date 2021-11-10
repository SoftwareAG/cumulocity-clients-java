package com.cumulocity.lpwan.codec.sdk.service;

import com.cumulocity.lpwan.codec.sdk.Decoder;
import com.cumulocity.lpwan.codec.sdk.exception.DecoderException;
import com.cumulocity.lpwan.codec.sdk.model.DecodeRequest;
import com.cumulocity.lpwan.codec.sdk.model.DecodeResponse;
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
     * @param decode
     * @return DecodedData
     */
    public DecodeResponse decode(DecodeRequest decode) throws DecoderException {
        log.debug("Forwarding decoding request for the device with Id {} with payload {}",decode.getDeviceId(),decode.getPayload());
        return decoder.decode(null, null, null);
    }

    /**
     * This method should register a device type upon subscribing the codec microservice.
     * @param event
     */
    @EventListener
    private void registerDeviceTypes(MicroserviceSubscriptionAddedEvent event) {
        log.info("Creating device types on codec microservice subscription");
    }

    /**
     * This method should delete a device type upon unsubscribing the codec microservice.
     * @param event
     */
    @EventListener
    private void unregisterDeviceTypes(MicroserviceSubscriptionRemovedEvent event){
        log.info("Deleting device types on codec microservice unsubscription");
    }
}
