package com.cumulocity.me.util;

import java.util.Date;

public class SmartBuffer {
	private StringBuffer buffer;

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
}
