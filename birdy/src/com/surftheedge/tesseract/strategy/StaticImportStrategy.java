package com.surftheedge.tesseract.strategy;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.StaticImportExpression;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;

import javassist.CannotCompileException;

public class StaticImportStrategy extends Strategy {

    public StaticImportStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }

    @Override
    public Class execute() throws Exception {
	String staticClassName = ((StaticImportExpression) getExpression()).getPackage();
	StackClass theClass = Stack.getEvaluationClass();

	Class importClass = Class.forName(staticClassName);
	for (Field field : importClass.getFields()) {
	    if (Modifier.isStatic(field.getModifiers())) {
		createFieldForClass(staticClassName, theClass, field);
	    }
	}

	for (Method method : importClass.getMethods()) {
	    if (Modifier.isStatic(method.getModifiers())) {
		createMethodForClass(staticClassName, theClass, method);
	    }
	}

	return theClass.commit();
    }

    private void createMethodForClass(String staticClassName, StackClass theClass, Method method) throws CannotCompileException {
	// Stub
	String template = "public static " + method.getGenericReturnType().toString() + " " + method.getName();
	
	// Function params
	int i = 0;
	String methodParamList = "(";
	String invokeParamList = "(";
	int size = method.getGenericParameterTypes().length;
	for (Class type : method.getParameterTypes()) {
	    methodParamList += type.getCanonicalName() + " arg" + i;
	    invokeParamList += "arg" + i++;
	    if (i != size) {
		methodParamList += ",";
		invokeParamList += ",";
	    }

	}
	methodParamList += ")";
	invokeParamList += ")";

	template += methodParamList;
	i = 0;
	if (method.getExceptionTypes().length > 0) {
	    template += " throws ";
	    for (Class type : method.getExceptionTypes()) {
		template += type.getCanonicalName();
		i++;
		if (i != method.getExceptionTypes().length) {
		    template += ", ";
		}
	    }
	}
	// Body
	template += "{";
	if (method.getReturnType() == Void.class) {
	    template += staticClassName + "." + method.getName() + invokeParamList + ";";
	} else {
	    template += "return " + staticClassName + "." + method.getName() + invokeParamList + ";";
	}
	template += "}";
	
	theClass.addStackMethod(template);
    }

    private void createFieldForClass(String staticClassName, StackClass theClass, Field field) throws CannotCompileException {
	theClass.addStackField("static public " + ((Class) field.getGenericType()).getCanonicalName() + " " + field.getName() + " = "
		+ staticClassName + "." + field.getName() + ";");
    }

}
