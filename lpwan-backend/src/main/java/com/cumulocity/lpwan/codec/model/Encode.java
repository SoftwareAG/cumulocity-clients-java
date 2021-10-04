package com.cumulocity.lpwan.codec.model;

public class Encode {
	private String deviceIdentifier;
	private String operation;
	private String model;

	public Encode() {
	}

	public Encode(String devEui, String operation, String model) {
		super();
		this.deviceIdentifier = devEui;
		this.operation = operation;
		this.model = model;
	}
	
	public void setDeviceIdentifier(String deviceIdentifier) {
		this.deviceIdentifier = deviceIdentifier;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getDeviceIdentifier() {
		return deviceIdentifier;
	}

	public String getOperation() {
		return operation;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return "Encode [devEui=" + deviceIdentifier + ", operation=" + operation + ", model=" + model + "]";
	}
}