/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package c8y;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

public class DHCP extends AbstractDynamicProperties {

    private boolean enabled;
    private String dns1;
    private String dns2;
    private String domainName;
    private AddressRange addressRange;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    @JSONProperty(ignoreIfNull = true)
    public AddressRange getAddressRange() {
        return addressRange;
    }

    public void setAddressRange(AddressRange addressRange) {
        this.addressRange = addressRange;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((addressRange == null) ? 0 : addressRange.hashCode());
        result = prime * result + ((dns1 == null) ? 0 : dns1.hashCode());
        result = prime * result + ((dns2 == null) ? 0 : dns2.hashCode());
        result = prime * result + ((domainName == null) ? 0 : domainName.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
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
        DHCP other = (DHCP) obj;
        if (addressRange == null) {
            if (other.addressRange != null)
                return false;
        } else if (!addressRange.equals(other.addressRange))
            return false;
        if (dns1 == null) {
            if (other.dns1 != null)
                return false;
        } else if (!dns1.equals(other.dns1))
            return false;
        if (dns2 == null) {
            if (other.dns2 != null)
                return false;
        } else if (!dns2.equals(other.dns2))
            return false;
        if (domainName == null) {
            if (other.domainName != null)
                return false;
        } else if (!domainName.equals(other.domainName))
            return false;
        if (enabled != other.enabled)
            return false;
        return true;
    }

}
