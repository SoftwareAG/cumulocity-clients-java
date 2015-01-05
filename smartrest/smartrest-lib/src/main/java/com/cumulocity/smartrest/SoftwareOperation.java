package com.cumulocity.smartrest;

import java.util.Hashtable;

import com.cumulocity.smartrest.client.SmartRow;

public class SoftwareOperation extends AbstractOperation {
	public static final OperationType TYPE = new OperationType("c8y_Software", DeviceManagement.XID, 203);

	private Hashtable software;
	
	public SoftwareOperation(SmartRow operation) {
		super(operation);
		this.software = deserialize(operation.getData(3));
	}

	public OperationType getOperationType() {
		return TYPE;
	}

	public Hashtable getSoftware() {
		return software;
	}
	
	private Hashtable deserialize(String data) {
		Hashtable result = new Hashtable();
		
		// TODO Unclear how this would work...
		
		return result;
	}
}
