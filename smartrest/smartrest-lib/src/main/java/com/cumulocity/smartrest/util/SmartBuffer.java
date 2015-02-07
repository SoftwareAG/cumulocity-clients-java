package com.cumulocity.smartrest.util;

import java.util.Date;

import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.client.SmartRequest;
import com.cumulocity.smartrest.client.SmartResponse;
import com.cumulocity.smartrest.client.SmartRow;
import com.cumulocity.smartrest.client.impl.SmartRequestImpl;

public class SmartBuffer {
	private StringBuffer buffer;

	public SmartBuffer(int msg) {
		buffer = new StringBuffer().append(msg).append(StringUtils.SEP);
	}

	public SmartBuffer(int msg, String id) {
		buffer = new StringBuffer().append(msg).append(StringUtils.SEP)
				.append(id);
	}

	public SmartBuffer appendNow() {
		buffer.append(DateUtils.format(new Date()));
		return this;
	}

	public SmartBuffer append(StringBuffer value) {
		append(value.toString());
		return this;
	}

	public SmartBuffer append(String value) {
		if (value == null)
			value = "";
		buffer.append(StringUtils.SEP).append(value);
		return this;
	}

	public SmartBuffer append(int value) {
		buffer.append(StringUtils.SEP).append(value);
		return this;
	}

	public String toString() {
		return buffer.toString();
	}

	public SmartRow send(SmartConnection connection, String xid) {
		SmartRequest request = new SmartRequestImpl(toString());
		SmartResponse response = connection.executeRequest(xid, request);
		return response.getRow(0);
	}
}
