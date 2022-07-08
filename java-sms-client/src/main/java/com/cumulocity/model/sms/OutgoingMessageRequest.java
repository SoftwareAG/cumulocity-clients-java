package com.cumulocity.model.sms;



public class OutgoingMessageRequest {

    private SendMessageRequest outboundSMSMessageRequest;

    public OutgoingMessageRequest() {
    }

    public OutgoingMessageRequest(SendMessageRequest outgoingMessageRequest) {
        this.outboundSMSMessageRequest = outgoingMessageRequest;
    }

    public SendMessageRequest getOutboundSMSMessageRequest() {
        return outboundSMSMessageRequest;
    }

    public void setOutboundSMSMessageRequest(SendMessageRequest outgoingMessageRequest) {
        this.outboundSMSMessageRequest = outgoingMessageRequest;
    }
}
