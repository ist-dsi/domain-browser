package com.surftheedge.tesseract.utils;

public class InternalRepresentation {
    public static String convert(boolean v) {
	return new Boolean(v).toString();
    }

    public static String convert(byte v) {
	return new Byte(v).toString();
    }

    public static String convert(Object v) {
	if (v == null) {
	    return "null";
	} else {
	    return v.toString();
	}
    }

    public static String convert(double v) {
	return new Double(v).toString();
    }

    public static String convert(float v) {
	return (new Float(v).toString()) + "F";
    }

    public static String convert(int v) {
	return new Integer(v).toString();
    }

    public static String convert(long v) {
	return new Long(v).toString() + "L";
    }

    public static String convert(char v) {
	return "'" + stringForCharInString(v) + "'";
    }

    private static String stringForCharInString(char c) {
	switch (c) {
	case '\b':
	    return "\\b";

	case '\f':
	    return "\\f";

	case '\n':
	    return "\\n";

	case '\r':
	    return "\\r";

	case '\t':
	    return "\\t";

	case '\"':
	    return "\\\"";

	case '\\':
	    return "\\\\";

	default:
	    return "" + c;

	}
    }

    public static String revert(Object c){
	String sa = (c.toString()).substring(1, c.toString().length() - 1);
	
	char[] chars = sa.toCharArray();
	String s = "";
	for (int i=0; i<chars.length; i++){
	    if (chars[i] == '\\'){
		i++;
		switch (chars[i]) {
		case 'b':
		    s += "\b";

		case 'f':
		    s += "\f";

		case 'n':
		    s += "\n";

		case 'r':
		    s += "\r";

		case 't':
		    s += "\t";

		case '\"':
		    s += "\"";

		case '\\':
		    s += "\\";
		}
	    }else{
		 s += "" + chars[i];
	    }
	}
	return s;
    }
}
