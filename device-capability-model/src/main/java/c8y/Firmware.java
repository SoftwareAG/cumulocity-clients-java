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

public class Firmware extends AbstractDynamicProperties {
	
	private String name;
	private String version;
	private String url;

	public Firmware() {
	}

	public Firmware(String name, String version, String url) {
		this.name = name;
		this.version = version;
		this.url = url;
	}

	@JSONProperty(ignoreIfNull = true)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Hardware))
			return false;

		Firmware rhs = (Firmware) obj;
		boolean result = name == null ? rhs.name == null : name
				.equals(rhs.name);
		result = result
				&& (version == null ? (rhs.version == null) : version
						.equals(rhs.version));
		result = result
				&& (url == null ? (rhs.url == null) : url.equals(rhs.url));
		return result;
	}

	@Override
	public int hashCode() {
		int result = name == null ? 0 : name.hashCode();
		result = 31 * result + (version == null ? 0 : version.hashCode());
		result = 31 * result + (url == null ? 0 : url.hashCode());
		return result;
	}
}
