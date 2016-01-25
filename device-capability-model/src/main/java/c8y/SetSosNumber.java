package c8y;

import java.io.Serializable;

import org.svenson.AbstractDynamicProperties;

public class SetSosNumber extends AbstractDynamicProperties implements Serializable {

    private static final long serialVersionUID = 1608763081931360921L;

    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumber3;
    
    public SetSosNumber() {}
    
    public SetSosNumber(String phoneNumber1, String phoneNumber2, String phoneNumber3) {
	this.phoneNumber1 = phoneNumber1;
	this.phoneNumber2 = phoneNumber2;
	this.phoneNumber3 = phoneNumber3;
    }

    public String getPhoneNumber1() {
	return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
	this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
	return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
	this.phoneNumber2 = phoneNumber2;
    }

    public String getPhoneNumber3() {
	return phoneNumber3;
    }

    public void setPhoneNumber3(String phoneNumber3) {
	this.phoneNumber3 = phoneNumber3;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((phoneNumber1 == null) ? 0 : phoneNumber1.hashCode());
	result = prime * result + ((phoneNumber2 == null) ? 0 : phoneNumber2.hashCode());
	result = prime * result + ((phoneNumber3 == null) ? 0 : phoneNumber3.hashCode());
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
	SetSosNumber other = (SetSosNumber) obj;
	if (phoneNumber1 == null) {
	    if (other.phoneNumber1 != null)
		return false;
	} else if (!phoneNumber1.equals(other.phoneNumber1))
	    return false;
	if (phoneNumber2 == null) {
	    if (other.phoneNumber2 != null)
		return false;
	} else if (!phoneNumber2.equals(other.phoneNumber2))
	    return false;
	if (phoneNumber3 == null) {
	    if (other.phoneNumber3 != null)
		return false;
	} else if (!phoneNumber3.equals(other.phoneNumber3))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "SetSosNumber [phoneNumber1=" + phoneNumber1 + ", phoneNumber2=" + phoneNumber2 + ", phoneNumber3=" + phoneNumber3 + "]";
    }

}
