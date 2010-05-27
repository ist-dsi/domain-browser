package com.surftheedge.tesseract.strategy;


import java.util.Iterator;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.DynamicFunctionExpression;
import com.surftheedge.tesseract.expression.functionSignature.ArgumentSignature;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;
import com.surftheedge.tesseract.stack.StackMethod;
import com.surftheedge.tesseract.utils.Boxing;
import com.surftheedge.tesseract.utils.DynamicFunctionAllocationManager;
import com.surftheedge.tesseract.utils.InternalRepresentation;


import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public class DynamicFunctionStrategy extends Strategy implements Parallelizable {
    public DynamicFunctionStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }

    private String invokeString(String id) {
	FunctionSignature function = ((DynamicFunctionExpression) getExpression()).getFunction();
	String invoke = "com.surftheedge.tesseract.DynamicFunctionAllocationManager.invoke(" + InternalRepresentation.convert(id)
		+ ", $args)";

	String prefix = "";
	String postfix = "";
	if (function.getReturnType().equals("byte")) {
	    prefix = "Byte";
	    postfix = ".byteValue()";
	} else if (function.getReturnType().equals("short")) {
	    prefix = "Short";
	    postfix = ".shortValue()";
	} else if (function.getReturnType().equals("int")) {
	    prefix = "Integer";
	    postfix = ".intValue()";
	} else if (function.getReturnType().equals("long")) {
	    prefix = "Long";
	    postfix = ".longValue()";
	} else if (function.getReturnType().equals("float")) {
	    prefix = "Float";
	    postfix = ".floatValue()";
	} else if (function.getReturnType().equals("double")) {
	    prefix = "Double";
	    postfix = ".doubleValue()";
	} else if (function.getReturnType().equals("char")) {
	    prefix = "Character";
	    postfix = ".charValue()";
	} else if (function.getReturnType().equals("boolean")) {
	    prefix = "Boolean";
	    postfix = ".booleanValue()";
	} else {
	    prefix = function.getReturnType();
	}
	return "((" + prefix + ") " + invoke + " )" + postfix;
    }

    private String generateAnonymousName() {
	return "$" + ((DynamicFunctionExpression) getExpression()).getFunction().getName();
    }

    private String anonymousFunction() {
	FunctionSignature function = ((DynamicFunctionExpression) getExpression()).getFunction();
	return "public static " + function.getReturnType() + " " + generateAnonymousName() + "(" + function.getFunctionParameters() + ")"
		+ ((DynamicFunctionExpression) getExpression()).getBody();
    }

    private String callableWrapperFunction(String id) {
	FunctionSignature function = ((DynamicFunctionExpression) getExpression()).getFunction();
	String args = "";
	for (Iterator<ArgumentSignature> iterator = function.getArguments().iterator(); iterator.hasNext();) {
	    ArgumentSignature type = iterator.next();
	    if (iterator.hasNext()) {
		args += "com.surftheedge.tesseract.utils.Boxing.box(" + type.getVariableName() + "), ";
	    } else {
		args += "com.surftheedge.tesseract.utils.Boxing.box(" + type.getVariableName() + ")";
	    }
	}
	String baseTemplate = "public static " + function.getStub() + "{"
		+ "Object[] $args = " + ((function.getArguments().size() > 0) ? "{" + args + "}" : "null") + ";" +

		"return " + invokeString(id) + "; }";
//	System.out.println(baseTemplate);
	return baseTemplate;
    }

    @SuppressWarnings("unchecked")
    public void prepare(StackClass theClass) throws CannotCompileException, NotFoundException, ClassNotFoundException,
	    SecurityException, NoSuchMethodException {

	StackMethod method = theClass.addStackMethod(anonymousFunction());
	String id = DynamicFunctionAllocationManager.generateIdentifier(method);
	if (!DynamicFunctionAllocationManager.alreadyExists(method)) {
	    theClass.addStackMethod(callableWrapperFunction(id));
	}

	Class aClass = theClass.commit();
	Class[] params = new Class[method.getMethod().getParameterTypes().length];
	int i = 0;
	for (CtClass aParamClass : method.getMethod().getParameterTypes()) {
	    params[i++] = Boxing.classForName(aParamClass.getName());
	}

	DynamicFunctionAllocationManager.getIndirectionTable().put(id, aClass.getDeclaredMethod(generateAnonymousName(), params));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class execute() throws Exception {
	StackClass theClass = Stack.getEvaluationClass();
	prepare(theClass);
	return theClass.getCompiledClass();
    }
    
    public void pre(StackClass theClass) throws CannotCompileException {
	try {
	    prepare(theClass);
	} catch (SecurityException e) {
	    e.printStackTrace();
	} catch (NotFoundException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	}
    }

    public void post(StackClass evalClass) throws CannotCompileException {
    }

}
