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
        Vector resultBuilder = new Vector();

        int currentIndex;
        int previousIndex = 0;

        while ( source.indexOf(delimiter, previousIndex) > -1 ) {
            currentIndex = source.indexOf(delimiter, previousIndex);
            resultBuilder.addElement(source.substring(previousIndex, currentIndex));
            previousIndex = currentIndex + delimiter.length();
        }

        // also add everything after the final delimiter occurrence
        resultBuilder.addElement( source.substring(previousIndex) );

        String[] result = new String[resultBuilder.size()];
        resultBuilder.copyInto(result);

        return result;
    }
    
    public static final String[] splitQuoted(String source, String delimiter) {
        Vector resultBuilder = new Vector();

        int currentIndex;
        int previousIndex = 0;
        int substringStart = 0;
        Integer[] quoteIndexes = getQuoteIndexes(source);
        
        while ( source.indexOf(delimiter, previousIndex) > -1 ) {
        	currentIndex = source.indexOf(delimiter, previousIndex);
        	if (!isInsideQuotes(currentIndex, quoteIndexes)) {
        		resultBuilder.addElement(source.substring(substringStart, currentIndex));
        		substringStart = currentIndex + delimiter.length();
        		previousIndex = substringStart;
			} else {
				previousIndex = currentIndex + delimiter.length();
			}
        }

        // also add everything after the final delimiter occurrence
        resultBuilder.addElement( source.substring(previousIndex) );

        String[] result = new String[resultBuilder.size()];
        resultBuilder.copyInto(result);

        return result;
    }

    public static final Integer[] getQuoteIndexes(String source){
        Vector indexes = new Vector();
        int currentIndex;
        int previousIndex = 0;
    	while (source.indexOf('"', previousIndex) > -1) {
    		currentIndex = source.indexOf('"', previousIndex);
    		indexes.addElement(new Integer(currentIndex));
    		previousIndex = currentIndex + 1;
		}
    	Integer[] indexesArray = new Integer[indexes.size()];
    	indexes.copyInto(indexesArray);
    	return indexesArray;
    }
    
    public static final boolean isInsideQuotes(int index, Integer[] quoteIndexes){
    	int i;
        for (i = 0; i < quoteIndexes.length; i++) {
			int currentQuoteIndex = quoteIndexes[i].intValue();
        	if (currentQuoteIndex > index) {
        		return (i & 1) == 1;
			}
		}
        return false;
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
    
    public static final String stringArrayToCsv(String[] array) {
    	if (array.length < 1) {
    		return "";
    	} else if (array.length == 1) {
    		return array[0];
    	}
    	String result = "";
    	for (int i = 0; i < array.length - 1; i++) {
    		result += array[0] + ",";
    	}
    	result += array[array.length - 1];
    	return result;
    }
}
