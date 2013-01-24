package com.cumulocity.me.util;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.cumulocity.me.sdk.SDKException;

public abstract class StringUtils {

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
        StringBuffer token = new StringBuffer();
        Vector tokens = new Vector();
        // split
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (delimiter.indexOf(chars[i]) != -1) {
                if (token.length() > 0) {
                    tokens.addElement(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(chars[i]);
            }
        }
        if (token.length() > 0) {
            tokens.addElement(token.toString());
        }
        String[] splitArray = new String[tokens.size()];
        for (int i = 0; i < splitArray.length; i++) {
            splitArray[i] = (String) tokens.elementAt(i);
        }
        return splitArray;
    }

    public static final String replaceAll(String source, String pattern, String replacement) {
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
        return source.substring(0, position) + extra + source.substring(position, source.length());
    }
}
