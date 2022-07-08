package com.cumulocity.model.sms;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;

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

    public IncomingMessage getLastByDateTime() {
        return Ordering.from(new Comparator<IncomingMessage>() {
            public int compare(IncomingMessage o1, IncomingMessage o2) {
                return o1.getInboundSMSMessage().getDateTime().compareTo(o2.getInboundSMSMessage().getDateTime());
            };
        }).max(this);
    }

    @Override
    public Iterator<IncomingMessage> iterator() {
        return inboundSMSMessageList != null ? inboundSMSMessageList.iterator() : ImmutableList.<IncomingMessage>of().iterator();
    }

    @Override
    public String toString() {
        return "IncomingMessages [messages=" + inboundSMSMessageList + "]";
    }

}
