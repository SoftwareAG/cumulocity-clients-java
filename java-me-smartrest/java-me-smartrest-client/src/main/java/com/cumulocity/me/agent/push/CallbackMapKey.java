package com.cumulocity.me.agent.push;

public class CallbackMapKey {
    private final String xId;
    private final int messageId;

    public CallbackMapKey(String xId, int messageId) {
        this.xId = xId;
        this.messageId = messageId;
    }

    public String getxId() {
        return xId;
    }

    public int getMessageId() {
        return messageId;
    }

    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.xId != null ? this.xId.hashCode() : 0);
        hash = 31 * hash + this.messageId;
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CallbackMapKey other = (CallbackMapKey) obj;
        if ((this.xId == null) ? (other.xId != null) : !this.xId.equals(other.xId)) {
            return false;
        }
        if (this.messageId != other.messageId) {
            return false;
        }
        return true;
    }
}
