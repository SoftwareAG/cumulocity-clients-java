/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.model;

import com.cumulocity.model.idtype.GId;

/**
 * Defines a device.
 */
public interface Device {

	/**
	 * @return the global ID of the device on the <tt>Cumulocity</tt> platform.
	 */
	GId getGlobalId();
}
