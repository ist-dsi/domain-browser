package com.surftheedge.tesseract.utils;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.RegisterWrapFunctionExpression;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;

public class TracingManager {

    private static final String TRACE_BEFORE = "com.surftheedge.tesseract.utils.TracingManager.traceBefore(java.lang.String,[Ljava.lang.Object;)";
    private static final String TRACE_AFTER = "com.surftheedge.tesseract.utils.TracingManager.traceAfter(java.lang.String,[Ljava.lang.Object;,java.lang.Object)";

    private static int depth = 0; 
    public static void traceBefore(String function, Object[] objects) {
	System.out.print("//");
	for (int i = 0; i<depth; i++) {
	    System.out.print("  ");
	}
	depth++;
	System.out.print(function.split("\\(")[0] + "(");
	for (int i = 0; i < objects.length;) {
	    System.out.print(objects[i].toString());
	    i++;
	    if (i != objects.length) {
		System.out.print(",");
	    }
	}
	System.out.println(")" + " =>");
    }

    public static void traceAfter(String function, Object[] objects, Object result) {
	System.out.print("//");
	depth--;
	for (int i = 0; i<depth; i++) {
	    System.out.print("  ");
	}
	System.out.print(function.split("\\(")[0] + "(");
	for (int i = 0; i < objects.length;) {
	    System.out.print(objects[i].toString());
	    i++;
	    if (i != objects.length) {
		System.out.print(",");
	    }
	}
	System.out.println(")" + " <= " + result.toString());

    }

    private static List<Expression> wrappersFor(String action, String function) {
	List<Expression> result = new ArrayList<Expression>();
	RegisterWrapFunctionExpression registerWrapFunctionBefore = new RegisterWrapFunctionExpression();
	registerWrapFunctionBefore.setContent("%before " + function + " " + action +" " + TRACE_BEFORE + ";");
	registerWrapFunctionBefore.setOperation(action);
	registerWrapFunctionBefore.setPlace("before");
	registerWrapFunctionBefore.setOrigin(FunctionSignature.createSignatureForCanonical(function));
	registerWrapFunctionBefore.setInterceptor(FunctionSignature.createSignatureForCanonical(TRACE_BEFORE));
	result.add(registerWrapFunctionBefore);
	
	RegisterWrapFunctionExpression registerWrapFunctionAfter = new RegisterWrapFunctionExpression();
	registerWrapFunctionAfter.setContent("%after " + function + " " + action + " " + TRACE_AFTER + ";");
	registerWrapFunctionAfter.setOperation(action);
	registerWrapFunctionAfter.setPlace("after");
	registerWrapFunctionAfter.setOrigin(FunctionSignature.createSignatureForCanonical(function));
	registerWrapFunctionAfter.setInterceptor(FunctionSignature.createSignatureForCanonical(TRACE_AFTER));
	result.add(registerWrapFunctionAfter);
	return result;
    }

    public static void trace(String function) throws Exception {
	for (Expression expression : wrappersFor("add", function)) {
	    Evaluator.eval(expression);
	}
    }
    
    public static void untrace(String function) throws Exception {
	for (Expression expression : wrappersFor("del", function)) {
	    Evaluator.eval(expression);
	}
    }

    public static void test() throws ClassNotFoundException {
	Class c = Class.forName("com.surftheedge.tesseract.utils.TracingManager");
	for (Method method : c.getDeclaredMethods()) {
	    System.out.println(method.getName());
	    for (Class param : method.getParameterTypes()) {
		System.out.println(param);
	    }
	}
    }
}
