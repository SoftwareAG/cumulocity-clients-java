package com.cumulocity.model.sms;

import org.svenson.JSONProperty;

import java.util.Collection;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkArgument;

public class SendMessageResponse {

    private Address senderAddress;

    private Collection<Address> address;

    private TextMessage message;

    private String senderName;

    private ReceiptRequest receiptRequest;

    private String clientCorrelator;

    private ResourceReference resourceReference;

    public SendMessageResponse() {
    }

    private SendMessageResponse(Address senderAddress, Collection<Address> addresses, TextMessage message, ReceiptRequest receiptRequest,
            String senderName, String clientCorrelator, ResourceReference resourceReference) {
        this.senderAddress = senderAddress;
        this.address = addresses;
        this.message = message;
        this.receiptRequest = receiptRequest;
        this.senderName = senderName;
        this.clientCorrelator = clientCorrelator;
        this.resourceReference = resourceReference;
    }

    public SendMessageResponse(ResourceReference resourceReference) {
        this.resourceReference = resourceReference;
    }

    public ResourceReference getResourceReference() {
        return resourceReference;
    }

    public void setResourceReference(ResourceReference resourceReference) {
        this.resourceReference = resourceReference;
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
        return "SendMessageResponse [senderAddress=" + senderAddress + ", address=" + address + ", message=" + message + ", senderName="
                + senderName + ", receiptRequest=" + receiptRequest + ", clientCorrelator=" + clientCorrelator + ", resourceReference="
                + resourceReference + "]";
    }



    public static class OutgoingMessageReponseBuilder {
        private Address senderAddress;

        private Collection<Address> addresses = new LinkedList<Address>();

        private TextMessage message;

        private String senderName;

        private String callbackData;

        private String notifyURL;

        private String clientCorrelator;

        private ResourceReference resourceReference;

        public OutgoingMessageReponseBuilder withSender(Address address) {
            this.senderAddress = address;
            return this;
        }

        public OutgoingMessageReponseBuilder withSenderName(String value) {
            this.senderName = value;
            return this;
        }

        public OutgoingMessageReponseBuilder withCallbackData(String value) {
            this.callbackData = value;
            return this;
        }

        public OutgoingMessageReponseBuilder withNotifyURL(String value) {
            this.notifyURL = value;
            return this;
        }

        public OutgoingMessageReponseBuilder withReciver(Address address) {
            this.addresses.add(address);
            return this;
        }

        public OutgoingMessageReponseBuilder withMessage(String value) {
            return withMessage(new TextMessage(value));
        }

        public OutgoingMessageReponseBuilder withMessage(TextMessage textMessage) {
            this.message = textMessage;
            return this;
        }

        public OutgoingMessageReponseBuilder withClientCorrelator(String value) {
            this.clientCorrelator = value;
            return this;
        }

        public OutgoingMessageReponseBuilder withResourceReference(ResourceReference resourceReference) {
            this.resourceReference = resourceReference;
            return this;
        }

        public SendMessageResponse build() {
            validate();
            return new SendMessageResponse(senderAddress, addresses, message, new ReceiptRequest(notifyURL, callbackData), senderName,
                    clientCorrelator, resourceReference);

        }

        private void validate() {
            checkArgument(senderAddress != null, "senderAdrress can't be null");
            checkArgument(!addresses.isEmpty(), "addresses can't be empty");
            checkArgument(message != null, "message can't be null");

        }
    }

    public static OutgoingMessageReponseBuilder from(SendMessageRequest request) {
        final OutgoingMessageReponseBuilder response = new OutgoingMessageReponseBuilder();
        if (request.getReceiptRequest() != null) {
            response.withCallbackData(request.getReceiptRequest().getCallbackData());
        }
        response.withClientCorrelator(request.getClientCorrelator());
        response.withMessage(request.getMessage());
        response.withNotifyURL(request.getReceiptRequest().getNotifyURL());
        response.withSender(request.getSenderAddress());
        response.addresses.addAll(request.getAddress());
        return response;
    }

    public static OutgoingMessageReponseBuilder builder() {
        return new OutgoingMessageReponseBuilder();
    }

}
