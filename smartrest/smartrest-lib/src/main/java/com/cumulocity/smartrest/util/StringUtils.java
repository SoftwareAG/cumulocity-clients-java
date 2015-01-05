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
package com.cumulocity.smartrest.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.cumulocity.SDKException;

public abstract class StringUtils {
	public static final char SEP = ',';
	public static final char QUOTE = '\"';
	public static final String DQUOTE = "\"\"";

	protected StringUtils() {
	}

	public static final String toString(Object obj) {
		return obj == null ? null : obj.toString();
	}

	public static final String valueOf(byte[] data) {
		if (data == null) {
			return null;
		}
		try {
			return new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new SDKException("UTF-8 is unpuspported!", e);
		}
	}

	public static final String ensureTail(String s, String tail) {
		if (s == null) {
			return null;
		}
		if (tail == null || tail.length() == 0) {
			return s;
		}
		if (s.endsWith(tail)) {
			return s;
		} else {
			return s + tail;
		}
	}

	public static final String[] split(String source, String delimiter) {
		Vector resultBuilder = new Vector();

		int currentIndex;
		int previousIndex = 0;

		while (source.indexOf(delimiter, previousIndex) > -1) {
			currentIndex = source.indexOf(delimiter, previousIndex);
			resultBuilder.addElement(source.substring(previousIndex,
					currentIndex));
			previousIndex = currentIndex + delimiter.length();
		}

		// also add everything after the final delimiter occurrence
		resultBuilder.addElement(source.substring(previousIndex));

		String[] result = new String[resultBuilder.size()];
		resultBuilder.copyInto(result);

		return result;
	}

	public static final String replaceAll(String source, String pattern,
			String replacement) {
		if (source == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		int idx = 0;

		String workingSource = source;

		while ((idx = workingSource.indexOf(pattern, idx)) != -1) {
			sb.append(workingSource.substring(0, idx));
			sb.append(replacement);
			sb.append(workingSource.substring(idx + pattern.length()));

			workingSource = sb.toString();

			sb.delete(0, sb.length());

			idx += replacement.length();
		}

		return workingSource;
	}

	public static final String insert(String source, int position, String extra) {
		return source.substring(0, position) + extra
				+ source.substring(position, source.length());
	}

	public static String escape(String text) {
		StringBuffer buffer = new StringBuffer().append(QUOTE);
		char c;
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (c == QUOTE) {
				buffer.append(c);
			}
			buffer.append(c);
		}
		buffer.append(QUOTE);
		return buffer.toString();
	}

	public static String escape(String[] texts) {
		StringBuffer buffer = new StringBuffer().append(QUOTE);
		for (int i = 0; i < texts.length; i++) {
			buffer.append(DQUOTE).append(texts[i]).append(DQUOTE);
			if (i < texts.length - 1) {
				buffer.append(SEP);
			}
		}
		buffer.append(QUOTE);
		return buffer.toString();
	}

	public static String escape(Hashtable table) {
		StringBuffer buffer = new StringBuffer().append(QUOTE);

		for (Enumeration e = table.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = (String) table.get(key);

			buffer.append(DQUOTE).append(key).append(DQUOTE);
			buffer.append(":");
			buffer.append(DQUOTE).append(value).append(DQUOTE);
			
			if (e.hasMoreElements()) {
				buffer.append(SEP);
			}
		}

		buffer.append(QUOTE);
		return buffer.toString();
	}
}
