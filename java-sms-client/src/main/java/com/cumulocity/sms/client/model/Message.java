package com.cumulocity.sms.client.model;

import java.util.Date;

public class Message {

    private Date dateTime;

    private Address destinationAddress;

    private String messageId;

    private String message;

    private String resourceURL;

    private Address senderAddress;

    public static IncomingMessageBuilder builder() {
        return new IncomingMessageBuilder();
    }

    public Message() {
    }

    private Message(Date dateTime, Address destinationAddress, Address senderAddress, String messageId, String message, String resourceURL) {
        this.dateTime = dateTime;
        this.destinationAddress = destinationAddress;
        this.senderAddress = senderAddress;
        this.messageId = messageId;
        this.message = message;
        this.resourceURL = resourceURL;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public Address getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(Address senderAddress) {
        this.senderAddress = senderAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = prime * result + ((destinationAddress == null) ? 0 : destinationAddress.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result + ((resourceURL == null) ? 0 : resourceURL.hashCode());
        result = prime * result + ((senderAddress == null) ? 0 : senderAddress.hashCode());
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
        Message other = (Message) obj;
        if (dateTime == null) {
            if (other.dateTime != null)
                return false;
        } else if (!dateTime.equals(other.dateTime))
            return false;
        if (destinationAddress == null) {
            if (other.destinationAddress != null)
                return false;
        } else if (!destinationAddress.equals(other.destinationAddress))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } else if (!messageId.equals(other.messageId))
            return false;
        if (resourceURL == null) {
            if (other.resourceURL != null)
                return false;
        } else if (!resourceURL.equals(other.resourceURL))
            return false;
        if (senderAddress == null) {
            if (other.senderAddress != null)
                return false;
        } else if (!senderAddress.equals(other.senderAddress))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Message [dateTime=" + dateTime + ", destinationAddress=" + destinationAddress + ", messageId=" + messageId + ", message="
                + message + ", resourceURL=" + resourceURL + ", senderAddress=" + senderAddress + "]";
    }

    public static class IncomingMessageBuilder {

        private Date dateTime;

        private Address destinationAddress;

        private String messageId;

        private String message;

        private String resourceURL;

        private Address senderAddress;

        public IncomingMessageBuilder withDateTime(Date dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public IncomingMessageBuilder withDestinationAddress(Address destinationAddress) {
            this.destinationAddress = destinationAddress;
            return this;
        }

        public IncomingMessageBuilder withSenderAddress(Address senderAddress) {
            this.senderAddress = senderAddress;
            return this;
        }

        public IncomingMessageBuilder withMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public IncomingMessageBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public IncomingMessageBuilder withResourceURL(String resourceURL) {
            this.resourceURL = resourceURL;
            return this;
        }

        public Message build() {
            return new Message(dateTime, destinationAddress, senderAddress, messageId, message, resourceURL);
        }

    }
}
