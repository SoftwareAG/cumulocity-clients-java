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


public class Mobile extends AbstractDynamicProperties {
    
	private String imei;
	private String cellId;
	private String iccid;
	private String mcc;
	private String mnc;
	private String imsi;
	private String lac;
	private String msisdn;

	public Mobile() {
	}

	public Mobile(String imei, String cellId, String iccid) {
		this.imei = imei;
		this.cellId = cellId;
		this.iccid = iccid;
	}

    @JSONProperty(ignoreIfNull = true)
	public String getImei() {
		return imei;
	}

    @JSONProperty(ignoreIfNull = true)
	public String getCellId() {
		return cellId;
	}

    @JSONProperty(ignoreIfNull = true)
	public String getIccid() {
		return iccid;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	
	@JSONProperty(ignoreIfNull = true)
	public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }
    
    @JSONProperty(ignoreIfNull = true)
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
        result = prime * result + ((iccid == null) ? 0 : iccid.hashCode());
        result = prime * result + ((imei == null) ? 0 : imei.hashCode());
        result = prime * result + ((imsi == null) ? 0 : imsi.hashCode());
        result = prime * result + ((lac == null) ? 0 : lac.hashCode());
        result = prime * result + ((mcc == null) ? 0 : mcc.hashCode());
        result = prime * result + ((mnc == null) ? 0 : mnc.hashCode());
        result = prime * result + ((msisdn == null) ? 0 : msisdn.hashCode());
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
        Mobile other = (Mobile) obj;
        if (cellId == null) {
            if (other.cellId != null)
                return false;
        } else if (!cellId.equals(other.cellId))
            return false;
        if (iccid == null) {
            if (other.iccid != null)
                return false;
        } else if (!iccid.equals(other.iccid))
            return false;
        if (imei == null) {
            if (other.imei != null)
                return false;
        } else if (!imei.equals(other.imei))
            return false;
        if (imsi == null) {
            if (other.imsi != null)
                return false;
        } else if (!imsi.equals(other.imsi))
            return false;
        if (lac == null) {
            if (other.lac != null)
                return false;
        } else if (!lac.equals(other.lac))
            return false;
        if (mcc == null) {
            if (other.mcc != null)
                return false;
        } else if (!mcc.equals(other.mcc))
            return false;
        if (mnc == null) {
            if (other.mnc != null)
                return false;
        } else if (!mnc.equals(other.mnc))
            return false;
        if (msisdn == null) {
            if (other.msisdn != null)
                return false;
        } else if (!msisdn.equals(other.msisdn))
            return false;
        return true;
    }

}
