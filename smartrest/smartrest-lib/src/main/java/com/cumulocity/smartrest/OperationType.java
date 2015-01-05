package com.cumulocity.smartrest;

public class OperationType {
	private String type;
	private String xid;
	private int messageId;

	public OperationType(String type, String xid, int messageId) {
		super();
		this.type = type;
		this.xid = xid;
		this.messageId = messageId;
	}

	public String getType() {
		return type;
	}

	public String getXid() {
		return xid;
	}

	public int getMessageId() {
		return messageId;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + messageId;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((xid == null) ? 0 : xid.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationType other = (OperationType) obj;
		if (messageId != other.messageId)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (xid == null) {
			if (other.xid != null)
				return false;
		} else if (!xid.equals(other.xid))
			return false;
		return true;
	}
}
