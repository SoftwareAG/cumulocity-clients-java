package com.cumulocity.sms.client.model;

public class ReceiptRequest {

    private String notifyURL;

    private String callbackData;

    public ReceiptRequest() {
    }

    public ReceiptRequest(String notifyURL, String callbackData) {
        this.notifyURL = notifyURL;
        this.callbackData = callbackData;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((callbackData == null) ? 0 : callbackData.hashCode());
        result = prime * result + ((notifyURL == null) ? 0 : notifyURL.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ReceiptRequest other = (ReceiptRequest) obj;
        if (callbackData == null) {
            if (other.callbackData != null)
                return false;
        } else if (!callbackData.equals(other.callbackData))
            return false;
        if (notifyURL == null) {
            if (other.notifyURL != null)
                return false;
        } else if (!notifyURL.equals(other.notifyURL))
            return false;
        return true;
    }
    
    
}
