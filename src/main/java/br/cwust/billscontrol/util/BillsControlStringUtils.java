package br.cwust.billscontrol.util;

public class BillsControlStringUtils {
	public static String hideValue(Object value) {
		if (value == null) {
			return "null";
		} else {
			return fillString(value.toString().length(), "*");
		}
	}
	
	public static String fillString(int numChars, String value) {
		StringBuilder sb = new StringBuilder();
		while (sb.length() < numChars) {
			sb.append(value);
		}
		return sb.substring(0, numChars - 1);
	}
}
