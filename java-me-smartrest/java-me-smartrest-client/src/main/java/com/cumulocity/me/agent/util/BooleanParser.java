package com.cumulocity.me.agent.util;

public class BooleanParser {
	
	private static final String[] falseStrings = new String[]{
			"0",
			"false"
	};
	
	private static final String[] trueStrings = new String[]{
			"1",
			"true"
	};
	
	public static Boolean parse(String string){
		if (string == null) {
			return Boolean.FALSE;
		}
		for (int i = 0; i < falseStrings.length; i++) {
			if (falseStrings[i].equals(string.toLowerCase())) {
				return Boolean.FALSE;
			}
		}
		
		for (int i = 0; i < trueStrings.length; i++) {
			if (trueStrings[i].equals(string.toLowerCase())) {
				return Boolean.TRUE;
			}
		}
		return null;
	}
	
	public static String parse(boolean bool){
		if (bool) {
			return "true";
		}
		return "false";
	}
}
