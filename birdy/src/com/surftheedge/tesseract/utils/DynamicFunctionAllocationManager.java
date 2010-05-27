package com.surftheedge.tesseract.utils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.surftheedge.tesseract.stack.StackMethod;

import javassist.CtClass;
import javassist.NotFoundException;

public class DynamicFunctionAllocationManager {

    private static HashMap<String, Method> indirectionTable = new HashMap<String, Method>();

    public static HashMap<String, Method> getIndirectionTable() {
	return indirectionTable;
    }

    public static void defineMethod(String id, Method method) {
	getIndirectionTable().put(id, method);
    }

    public static String generateIdentifier(StackMethod method) throws NotFoundException {
	StringBuffer buffer = new StringBuffer();
	buffer.append(method.getMethod().getName() + "(");

	for (CtClass ctClass : method.getMethod().getParameterTypes()) {
	    buffer.append(ctClass.getSimpleName() + ",");
	}
	buffer.append(")");

	return buffer.toString();
    }
    
    public static boolean alreadyExists(StackMethod method){
	try {
	    return getIndirectionTable().containsKey(generateIdentifier(method));
	} catch (NotFoundException e) {
	    return false;
	}
    }

    public static Object invoke(String id, Object[] params) throws IllegalArgumentException, IllegalAccessException,
	    InvocationTargetException {
	return getIndirectionTable().get(id).invoke(null, params);
    }
}
