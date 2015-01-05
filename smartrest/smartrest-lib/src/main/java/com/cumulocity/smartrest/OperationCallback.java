package com.cumulocity.smartrest;

import com.cumulocity.smartrest.client.SmartResponse;

public interface OperationCallback {
	boolean execute(String xid, SmartResponse operation);
}
