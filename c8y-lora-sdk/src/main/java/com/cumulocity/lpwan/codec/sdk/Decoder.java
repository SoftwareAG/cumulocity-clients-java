package com.cumulocity.lpwan.codec.sdk;

import com.cumulocity.lpwan.codec.sdk.exception.DecoderException;
import com.cumulocity.lpwan.codec.sdk.model.DecodeResponse;
import com.cumulocity.lpwan.codec.sdk.model.DeviceInfo;
import org.springframework.stereotype.Component;

/**
 * This interface will be implemented to provide a decoding logic.
 */
@Component
public interface Decoder {
    /**
     * This method should take the following params and should return the decoded response with mapping.
     * This method should provide a decoding logic w.r.t the device info.
     *
     * @param payload
     * @param deviceId
     * @param deviceInfo
     * @return
     * @throws DecoderException
     */
    DecodeResponse decode(String payload, String deviceId, DeviceInfo deviceInfo) throws DecoderException;
}
