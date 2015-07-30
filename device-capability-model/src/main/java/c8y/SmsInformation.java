package c8y;

import org.svenson.AbstractDynamicProperties;

public class SmsInformation extends AbstractDynamicProperties {
    
    private String sender;
    private String receiver;
    private String operator;
    private String data;
    
    public SmsInformation() {
    }
    
    public SmsInformation(String sender, String receiver, String operator) {
        this.sender = sender;
        this.receiver = receiver;
        this.operator = operator;
    }
    
    public SmsInformation(String sender, String receiver, String operator, String data) {
        this(sender, receiver, operator);
        this.data = data;
    }
    
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.sender != null ? this.sender.hashCode() : 0);
        hash = 53 * hash + (this.receiver != null ? this.receiver.hashCode() : 0);
        hash = 53 * hash + (this.operator != null ? this.operator.hashCode() : 0);
        hash = 53 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SmsInformation other = (SmsInformation) obj;
        if ((this.sender == null) ? (other.sender != null) : !this.sender.equals(other.sender)) {
            return false;
        }
        if ((this.receiver == null) ? (other.receiver != null) : !this.receiver.equals(other.receiver)) {
            return false;
        }
        if ((this.operator == null) ? (other.operator != null) : !this.operator.equals(other.operator)) {
            return false;
        }
        if ((this.data == null) ? (other.data != null) : !this.data.equals(other.data)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SmsInformation{" + "sender=" + sender + ", receiver=" + receiver + ", operator=" + operator + ", data=" + data + '}';
    }

}
