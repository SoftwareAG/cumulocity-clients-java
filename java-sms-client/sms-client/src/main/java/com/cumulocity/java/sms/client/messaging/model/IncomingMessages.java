package com.cumulocity.java.sms.client.messaging.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;

public class IncomingMessages implements Iterable<IncomingMessage> {

    @JsonProperty("inboundSMSMessageList")
    private List<IncomingMessage> messages = new LinkedList<IncomingMessage>();

    public IncomingMessages() {
    }

    public IncomingMessages(List<IncomingMessage> messages) {
        this.messages = messages;
    }

    public List<IncomingMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<IncomingMessage> messages) {
        this.messages = messages;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return messages == null || messages.isEmpty();
    }

    @JsonIgnore
    public IncomingMessage getLastByDateTime() {
        return Ordering.from(new Comparator<IncomingMessage>() {
            public int compare(IncomingMessage o1, IncomingMessage o2) {
                return o1.getMessage().getDateTime().compareTo(o2.getMessage().getDateTime());
            };
        }).max(this);
    }

    @Override
    public Iterator<IncomingMessage> iterator() {
        return messages != null ? messages.iterator() : Iterators.<IncomingMessage> emptyIterator();
    }

    @Override
    public String toString() {
        return "IncomingMessages [messages=" + messages + "]";
    }

}
