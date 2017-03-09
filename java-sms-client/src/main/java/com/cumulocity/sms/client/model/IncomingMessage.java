package com.cumulocity.sms.client.model;

public class IncomingMessage {

    private Message inboundSMSMessage;

    public IncomingMessage() {
    }

    public IncomingMessage(Message inboundSMSMessage) {
        this.inboundSMSMessage = inboundSMSMessage;
    }

    public Message getInboundSMSMessage() {
        return inboundSMSMessage;
    }

    public void setInboundSMSMessage(Message inboundSMSMessage) {
        this.inboundSMSMessage = inboundSMSMessage;
    }

    @Override
    public String toString() {
        return "IncomingMessage [message=" + inboundSMSMessage + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((inboundSMSMessage == null) ? 0 : inboundSMSMessage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IncomingMessage other = (IncomingMessage) obj;
        if (inboundSMSMessage == null) {
            if (other.inboundSMSMessage != null)
                return false;
        } else if (!inboundSMSMessage.equals(other.inboundSMSMessage))
            return false;
        return true;
    }
}
