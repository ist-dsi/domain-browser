package com.surftheedge.tesseract.utils;

public class Boxing {
    public static Boolean box(boolean v) {
	return new Boolean(v);
    }

    public static Byte box(byte v) {
	return new Byte(v);
    }

    public static Double box(double v) {
	return new Double(v);
    }

    public static Float box(float v) {
	return new Float(v);
    }

    public static Integer box(int v) {
	return new Integer(v);
    }

    public static Long box(long v) {
	return new Long(v);
    }

    public static Character box(char v) {
	return new Character(v);
    }

    @SuppressWarnings("unchecked")
    public static Class classForName(String name) throws ClassNotFoundException {
	if (name.equals("byte")) {	
	    return Byte.TYPE;
	} else if (name.equals("short")) {
	    return Short.TYPE;
	} else if (name.equals("int")) {
	    return Integer.TYPE;
	} else if (name.equals("long")) {
	    return Long.TYPE;
	} else if (name.equals("float")) {
	    return Float.TYPE;
	} else if (name.equals("double")) {
	    return Double.TYPE;
	} else if (name.equals("char")) {
	    return Character.TYPE;
	} else if (name.equals("String")) {
	    return java.lang.String.class;
	} else if (name.equals("boolean")) {
	    return Boolean.TYPE;
	} else {
	    return Class.forName(name);
	}
    }
}
