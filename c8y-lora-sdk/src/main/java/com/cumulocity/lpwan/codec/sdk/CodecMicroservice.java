package com.cumulocity.lpwan.codec.sdk;

import com.cumulocity.lpwan.codec.sdk.model.DeviceInfo;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * This abstract class will be implemented to provide a set of unique supported devices.
 */
@Component
public abstract class CodecMicroservice {
	/**
	 * This method should return a set of uniquely supported devices w.r.t the device manufacturer and the device model.
	 * @return Set<DeviceInfo>
	 */
	public Set<DeviceInfo> supportsDevices(){
		throw new UnsupportedOperationException("Needs implementation for supportsDevices()");
	}
}
