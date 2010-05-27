package com.surftheedge.tesseract.utils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.surftheedge.tesseract.stack.StackClass;
import com.surftheedge.tesseract.stack.StackMethod;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class WrapperFunctionManager {

    private static HashMap<String, ArrayList<Method>> beforeMappingTable = new HashMap<String, ArrayList<Method>>();
    private static HashMap<String, ArrayList<Method>> afterMappingTable = new HashMap<String, ArrayList<Method>>();

    private static HashMap<String, ArrayList<Method>> getAfterMappingTable() {
	return afterMappingTable;
    }

    private static HashMap<String, ArrayList<Method>> getBeforeMappingTable() {
	return beforeMappingTable;
    }

    public static void before(String function, Object[] args) throws IllegalArgumentException, IllegalAccessException,
	    InvocationTargetException {
	Object[] array = new Object[args.length + 1];
	array[0] = function;
	array[1] = args;
	for (Method method : getBeforeMappingTable().get(function)) {
	    method.invoke(null, array);
	}
    }

    public static void after(String function,Object result, Object[] args) throws IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException {
	Object[] array = new Object[3];
	array[0] = function;
	array[1] = args;
	array[2] = result;
	for (Method method : getAfterMappingTable().get(function)) {
	    method.invoke(null, array);
	}
    }

    private static String signature(Method mm) {
	String args = "";
	for (Type type : mm.getParameterTypes()) {
	    Class aClass = (Class) type;
	    args += aClass.getName() + ",";
	}
	if(args.length() > 0){
	    args = args.substring(0,args.length() - 1);
	}
	return mm.getName() + "(" + args + ")";
    }
    
    private static String signature(CtMethod mm) throws NotFoundException {
	String args = "";
	for (CtClass type : mm.getParameterTypes()){
	    args += type.getName() + ",";
	}
	if(args.length() > 0){
	    args = args.substring(0,args.length() - 1);
	}
	return mm.getName() + "(" + args + ")";
    }

    public static void wrapMethod(StackClass theClass, StackMethod method) throws CannotCompileException, NotFoundException,
	    ClassNotFoundException, SecurityException, NoSuchMethodException {

	String sig = signature(method.getMethod());
	CtMethod ctMethod = method.getMethod();
	ctMethod.insertBefore("{ com.surftheedge.tesseract.utils.WrapperFunctionManager.before(" + InternalRepresentation.convert(sig)
		+ ", $args); }");
	getBeforeMappingTable().put(sig, new ArrayList<Method>());
	ctMethod.insertAfter("{ com.surftheedge.tesseract.utils.WrapperFunctionManager.after(" + InternalRepresentation.convert(sig)
		+ ",($w)$_ , $args); }");
	getAfterMappingTable().put(sig, new ArrayList<Method>());
    }
    
    public static void addBefore (Method origin, Method interceptor){
	getBeforeMappingTable().get(signature(origin)).add(interceptor);
    }
    
    public static void addAfter (Method origin, Method interceptor){
	getAfterMappingTable().get(signature(origin)).add(interceptor);
    }
    
    public static void deleteBefore (Method origin, Method interceptor){
	getBeforeMappingTable().get(signature(origin)).remove(interceptor);
    }
    
    public static void deleteAfter (Method origin, Method interceptor){
	getAfterMappingTable().get(signature(origin)).remove(interceptor);
    }
}
