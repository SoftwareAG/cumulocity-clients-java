package com.cumulocity.sms.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IncomingMessages implements Iterable<IncomingMessage> {

    private List<IncomingMessage> inboundSMSMessageList = new LinkedList<IncomingMessage>();

    public IncomingMessages() {
    }

    public IncomingMessages(List<IncomingMessage> inboundSMSMessageList) {
        this.inboundSMSMessageList = inboundSMSMessageList;
    }
    
    public List<IncomingMessage> getInboundSMSMessageList() {
        return inboundSMSMessageList;
    }

    public void setInboundSMSMessageList(List<IncomingMessage> inboundSMSMessageList) {
        this.inboundSMSMessageList = inboundSMSMessageList;
    }

    public boolean isEmpty() {
        return inboundSMSMessageList == null || inboundSMSMessageList.isEmpty();
    }

    @Override
    public Iterator<IncomingMessage> iterator() {
        return inboundSMSMessageList != null ? inboundSMSMessageList.iterator() : new ArrayList<IncomingMessage>().iterator();
    }

    @Override
    public String toString() {
        return "IncomingMessages [messages=" + inboundSMSMessageList + "]";
    }

}
