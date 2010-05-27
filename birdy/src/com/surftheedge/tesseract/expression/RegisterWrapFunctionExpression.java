package com.surftheedge.tesseract.expression;

import static com.surftheedge.tesseract.utils.WrapperFunctionManager.addAfter;
import static com.surftheedge.tesseract.utils.WrapperFunctionManager.addBefore;
import static com.surftheedge.tesseract.utils.WrapperFunctionManager.deleteAfter;
import static com.surftheedge.tesseract.utils.WrapperFunctionManager.deleteBefore;

import java.lang.reflect.Method;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.functionSignature.ArgumentSignature;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.strategy.Strategy;
import com.surftheedge.tesseract.utils.Boxing;


public class RegisterWrapFunctionExpression extends Expression {

    private FunctionSignature origin;
    private FunctionSignature interceptor;
    private String operation;
    private String place;

    public FunctionSignature getInterceptor() {
	return interceptor;
    }

    public void setInterceptor(FunctionSignature interceptor) {
	this.interceptor = interceptor;
    }

    public String getPlace() {
	return place;
    }

    public void setPlace(String place) {
	this.place = place;
    }

    public String getOperation() {
	return operation;
    }

    public void setOperation(String operation) {
	this.operation = operation;
    }

    public FunctionSignature getOrigin() {
	return origin;
    }

    public void setOrigin(FunctionSignature origin) {
	this.origin = origin;
    }

    public RegisterWrapFunctionExpression() {
	super(new Strategy(Evaluator.DONT_EVALUATE) {

	    @Override
	    public Class execute() throws Exception {
		Class topLevel = Class.forName(Stack.getTopLevelEval().getName());
		RegisterWrapFunctionExpression rf = ((RegisterWrapFunctionExpression) getExpression());
		Class[] origArgs = new Class[rf.getOrigin().getArguments().size()];
		int i = 0;
		for (ArgumentSignature argument : rf.getOrigin().getArguments()) {
		    origArgs[i] = Boxing.classForName(argument.getType());
		}
		Method originMethod = topLevel.getMethod(rf.getOrigin().getName(), origArgs);

		Class theOtherClass;
		if (rf.getInterceptor().getName().split("\\.").length > 1) {
		    String packageAndClass = rf.getInterceptor().getName().substring(0,
			    rf.getInterceptor().getName().lastIndexOf("."));
		    theOtherClass = Class.forName(packageAndClass);
		} else {
		    theOtherClass = Class.forName(Stack.getTopLevelEval().getName());
		}
		Class[] interceptorArgs = new Class[rf.getInterceptor().getArguments().size()];
		int j = 0;
		;
		for (ArgumentSignature argument : rf.getInterceptor().getArguments()) {
		    interceptorArgs[j++] = Boxing.classForName(argument.getType());
		}
		Method interceptor = theOtherClass.getMethod(rf.getInterceptor().getName().substring(
			rf.getInterceptor().getName().lastIndexOf(".") + 1), interceptorArgs);
		
		if (rf.getPlace().equals("before")) {
		    if (rf.getOperation().equals("add")) {
			addBefore(originMethod, interceptor);
		    } else {
			deleteBefore(originMethod, interceptor);
		    }
		} else {
		    if (rf.getOperation().equals("add")) {
			addAfter(originMethod, interceptor);
		    } else {
			deleteAfter(originMethod, interceptor);
		    }
		}
		return null;
	    }

	});
    }

}
