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

import org.svenson.JSONProperty;

public class Mobile {

	private String imei;
	private String cellId;
	private String iccid;

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

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Mobile))
			return false;

		Mobile rhs = (Mobile) obj;
		boolean result = (imei == null) ? (rhs.imei == null) : imei.equals(rhs.imei);
		result = result && ((cellId == null) ? (rhs.cellId == null) : cellId.equals(rhs.cellId));
		result = result && ((iccid == null) ? (rhs.iccid == null) : iccid.equals(rhs.iccid));
		return result;
	}

	@Override
	public int hashCode() {
        int result = imei == null ? 0 : imei.hashCode();
        result = 31 * result + (cellId == null ? 0 : cellId.hashCode());
        result = 31 * result + (iccid == null ? 0 : iccid.hashCode());
        return result;
	}
}
