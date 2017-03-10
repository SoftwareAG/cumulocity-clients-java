package com.cumulocity.sms.client;

import com.cumulocity.model.sms.*;

public interface SmsMessagingApi {

    /**
     * Sends the sms message
     * @param request
     */
    void sendMessage(SendMessageRequest request);
    /**
     * Gets the list of sms messages for the given address
     * @param receiveAddress
     * @return the list of incoming messages
     */
    IncomingMessages getAllMessages(Address receiveAddress);
    /**
     * Gets the sms message with given the id, null if message with the id does not exist.
     * @param receiveAddress
     * @param messageId
     * @return the message with the given the id
     */
    IncomingMessage getMessage(Address receiveAddress, String messageId);
    
}
