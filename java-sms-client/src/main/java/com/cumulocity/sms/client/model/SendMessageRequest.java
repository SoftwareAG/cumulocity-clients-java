package com.cumulocity.sms.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.svenson.JSONProperty;

import java.util.Collection;
import java.util.LinkedList;

@Data
@Builder
@Wither
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

    private Address senderAddress;

    private Collection<Address> address;

    private TextMessage message;

    private String senderName;

    private ReceiptRequest receiptRequest;

    private String clientCorrelator;

    private String deviceId;

    private SendMessageRequest(Address senderAddress, Collection<Address> addresses, TextMessage message, ReceiptRequest receiptRequest,
                               String senderName, String clientCorrelator, String deviceId) {
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

    @Override
    public String toString() {
        return "SendMessageRequest [senderAddress=" + senderAddress + ", address=" + address + ", message=" + message + ", senderName="
                + senderName + ", receiptRequest=" + receiptRequest + ", clientCorrelator=" + clientCorrelator + ", deviceId=" + deviceId + "]";
    }

    public static class OutgoingMessageRequestBuilder {
        private Address senderAddress;

        private Collection<Address> addresses = new LinkedList<Address>();

        private TextMessage message;

        private String senderName;

        private String callbackData;

        private String notifyURL;

        private String clientCorrelator;

        private String deviceId;

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
            this.deviceId = id;
            return this;
        }

        public SendMessageRequest build() {
            validate();
            return new SendMessageRequest(senderAddress, addresses, message, new ReceiptRequest(notifyURL, callbackData), senderName, clientCorrelator, deviceId);

        }

        private void validate() {
            if (senderAddress == null) {
                throw new IllegalArgumentException("senderAdrress can't be null");
            }
            if (addresses.isEmpty()) {
                throw new IllegalArgumentException("addresses can't be empty");
            }
            if (message == null) {
                throw new IllegalArgumentException("message can't be null");
            }
        }
    }

    public static OutgoingMessageRequestBuilder builder() {
        return new OutgoingMessageRequestBuilder();
    }

}
