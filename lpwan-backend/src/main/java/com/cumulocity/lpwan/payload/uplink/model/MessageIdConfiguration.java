package com.cumulocity.lpwan.payload.uplink.model;

import lombok.Data;

@Data
public class MessageIdConfiguration {

    private String source;
    private MessageIdMapping messageIdMapping; 
}
