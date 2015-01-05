package com.cumulocity.smartrest;


public interface SmartObjectFactory {
	SmartObject createOrGet(String extIdType, String extId, String type,
			String defaultName, boolean isAgent, boolean isDevice);
}
