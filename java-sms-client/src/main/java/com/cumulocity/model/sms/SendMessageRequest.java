package com.cumulocity.model.sms;

import com.cumulocity.model.idtype.GId;
import org.svenson.JSONProperty;

import java.util.Collection;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkArgument;

public class SendMessageRequest {

    private Address senderAddress;

    private Collection<Address> address;

    private TextMessage message;

    private String senderName;

    private ReceiptRequest receiptRequest;

    private String clientCorrelator;
    
    private GId deviceId;

    public SendMessageRequest() {
    }

    private SendMessageRequest(Address senderAddress, Collection<Address> addresses, TextMessage message, ReceiptRequest receiptRequest,
            String senderName, String clientCorrelator, GId deviceId) {
        this.senderAddress = senderAddress;
        this.address = addresses;
        this.message = message;
        this.receiptRequest = receiptRequest;
        this.senderName = senderName;
        this.clientCorrelator = clientCorrelator;
        this.deviceId = deviceId;
    }

    public Address getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(Address senderAddress) {
        this.senderAddress = senderAddress;
    }

    public Collection<Address> getAddress() {
        return address;
    }

    public void setAddress(Collection<Address> addresses) {
        this.address = addresses;
    }

    @JSONProperty("outboundSMSTextMessage")
    public TextMessage getMessage() {
        return message;
    }

    public void setMessage(TextMessage message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public ReceiptRequest getReceiptRequest() {
        return receiptRequest;
    }

    public void setReceiptRequest(ReceiptRequest receiptRequest) {
        this.receiptRequest = receiptRequest;
    }

    public String getClientCorrelator() {
        return clientCorrelator;
    }

    public void setClientCorrelator(String clientCorrelator) {
        this.clientCorrelator = clientCorrelator;
    }

    public GId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(GId deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "SendMessageRequest [senderAddress=" + anonymizedSenderAddress() + ", address=" + address + ", message=" + message + ", senderName="
                + senderName + ", receiptRequest=" + receiptRequest + ", clientCorrelator=" + clientCorrelator + ", deviceId=" + deviceId + "]";
    }

    private String anonymizedSenderAddress() {
        return new LogMessageSanitizer(3)
                .sanitize(senderAddress.toString());
    }

    public static class OutgoingMessageRequestBuilder {
        private Address senderAddress;

        private Collection<Address> addresses = new LinkedList<>();

        private TextMessage message;

        private String senderName;

        private String callbackData;

        private String notifyURL;

        private String clientCorrelator;
        
        private GId deviceId;

        public OutgoingMessageRequestBuilder withSender(Address address) {
            this.senderAddress = address;
            return this;
        }

        public OutgoingMessageRequestBuilder withSenderName(String value) {
            this.senderName = value;
            return this;
        }

        public OutgoingMessageRequestBuilder withCallbackData(String value) {
            this.callbackData = value;
            return this;
        }

        public OutgoingMessageRequestBuilder withNotifyURL(String value) {
            this.notifyURL = value;
            return this;
        }

        public OutgoingMessageRequestBuilder withReceiver(Address address) {
            this.addresses.add(address);
            return this;
        }

        public OutgoingMessageRequestBuilder withMessage(String value) {
            return withMessage(new TextMessage(value));
        }

        public OutgoingMessageRequestBuilder withMessage(TextMessage textMessage) {
            this.message = textMessage;
            return this;
        }

        public OutgoingMessageRequestBuilder withClientCorrelator(String value) {
            this.clientCorrelator = value;
            return this;
        }
        
        public OutgoingMessageRequestBuilder withDeviceId(String id) {
            this.deviceId = new GId(id);
            return this;
        }

        public SendMessageRequest build() {
            validate();
            return new SendMessageRequest(senderAddress, addresses, message, new ReceiptRequest(notifyURL, callbackData), senderName,
                    clientCorrelator, deviceId);

        }

        private void validate() {
            checkArgument(senderAddress != null, "senderAdrress can't be null");
            checkArgument(!addresses.isEmpty(), "addresses can't be empty");
            checkArgument(message != null, "message can't be null");

        }
    }

    public static OutgoingMessageRequestBuilder builder() {
        return new OutgoingMessageRequestBuilder();
    }

}
