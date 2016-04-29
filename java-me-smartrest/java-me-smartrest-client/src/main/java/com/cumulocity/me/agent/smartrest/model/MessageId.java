package com.cumulocity.me.agent.smartrest.model;

public interface MessageId {
    public static final MessageId SET_XID_REQUEST = new MessageIdImpl(15);
    public static final MessageId SET_XID_RESPONSE = new MessageIdImpl(87);
    
    public int getValue();
    
    class MessageIdImpl implements MessageId{

        private final int value;
        private MessageIdImpl(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
