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

package com.cumulocity.lpwan.codec;

import com.cumulocity.lpwan.codec.model.DecodeResponse;
import com.cumulocity.lpwan.codec.model.DeviceInfo;
import com.cumulocity.lpwan.codec.exception.DecoderException;
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
