package com.cumulocity.sms.client.model;

import org.svenson.JSONProperty;

import java.util.Collection;

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
}
