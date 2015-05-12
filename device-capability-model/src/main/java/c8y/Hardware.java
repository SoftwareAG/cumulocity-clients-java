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


public class Hardware extends AbstractDynamicProperties {

	private String model;
	private String serialNumber;
	private String revision;

	public Hardware() {
	}

	public Hardware(String model, String serialNumber, String revision) {
		this.model = model;
		this.serialNumber = serialNumber;
		this.revision = revision;
	}

	public String getModel() {
		return model;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getRevision() {
		return revision;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Hardware))
			return false;

		Hardware rhs = (Hardware) obj;
		return (model == null ? rhs.getModel() == null : model.equals(rhs
				.getModel()))
				&& (serialNumber == null ? rhs.getSerialNumber() == null
						: serialNumber.equals(rhs.getSerialNumber()))
				&& (revision == null ? rhs.getRevision() == null : revision
						.equals(rhs.getRevision()));
	}

	@Override
	public int hashCode() {
		int result = model != null ? model.hashCode() : 0;
		result = 31 * result
				+ (serialNumber != null ? serialNumber.hashCode() : 0);
		result = 31 * result + (revision != null ? revision.hashCode() : 0);
		return result;
	}
}
