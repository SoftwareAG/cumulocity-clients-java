package com.cumulocity.model.sms;

public class LogMessageSanitizer {

    private final int leadingLength;
    private final int endingLength;
    private static final char replacingChar = '*';

    public LogMessageSanitizer(int leadingLength, int endingLength) {
        this.leadingLength = leadingLength;
        this.endingLength = endingLength;
    }

    public LogMessageSanitizer(int leftoversLength) {
        this(leftoversLength, leftoversLength);
    }

    public String sanitize(String message) {
        if (message != null && message.length() > leadingLength + endingLength) {
            message = unchangedLeading(message) +
                    blurredMiddlePart(message.substring(leadingLength, message.length() - endingLength)) +
                    unchangedEnding(message);
        }
        return message;
    }

    private String unchangedLeading(String number) {
        return number.substring(0, leadingLength);
    }

    private String blurredMiddlePart(String message) {
        return message.replaceAll(".", Character.toString(replacingChar));
    }

    private String unchangedEnding(String number) {
        return number.substring(number.length() - endingLength);
    }
}
