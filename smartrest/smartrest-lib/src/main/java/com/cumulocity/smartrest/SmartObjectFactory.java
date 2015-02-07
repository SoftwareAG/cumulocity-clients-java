package com.cumulocity.smartrest;

/**
 * This factory creates handles to objects in the inventory.
 * 
 * @see SmartObjectFactoryImpl
 */
public interface SmartObjectFactory {
	/**
	 * Gets a handle to an object, or creates the object if it does not exist
	 * yet.
	 * 
	 * The object is referenced by an external ID such as a MAC address,
	 * hardware serial or similar. If no object with that ID has been previously
	 * registered, a new object is created. If an object has been previously
	 * registered, a handle to that object is returned.
	 * 
	 * @param extIdType Type of external ID.
	 * @param extId The external ID.
	 * @param type When creating a new object, this type is used.
	 * @param defaultName When creating a new object, this is the human-readable name that is used.
	 * @param isAgent When creating a new object, it is marked as agent.
	 * @param isDevice When creating a new object, it is marked as device.
	 * @return A handle to the object.
	 */
	SmartObject createOrGet(String extIdType, String extId, String type,
			String defaultName, boolean isAgent, boolean isDevice);

	/**
	 * Get a handle to an object using a Cumulocity ID.
	 * @param moId
	 * @return A handle to the object.
	 */
	SmartObject get(String moId);
}
