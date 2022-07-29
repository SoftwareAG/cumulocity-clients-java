package com.cumulocity.model.sms;


public class OutgoingMessageResponse {

    private SendMessageResponse outboundSMSMessageRequest;

    public OutgoingMessageResponse() {
    }

    public OutgoingMessageResponse(SendMessageResponse outgoingMessageRequest) {
        this.outboundSMSMessageRequest = outgoingMessageRequest;
    }

    public SendMessageResponse getOutboundSMSMessageRequest() {
        return outboundSMSMessageRequest;
    }

    public void setOutboundSMSMessageRequest(SendMessageResponse outgoingMessageRequest) {
        this.outboundSMSMessageRequest = outgoingMessageRequest;
    }
}
