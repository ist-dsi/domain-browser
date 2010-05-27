package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.WrappableFunctionExpression;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;
import com.surftheedge.tesseract.stack.StackMethod;
import com.surftheedge.tesseract.utils.WrapperFunctionManager;


public class WrappableFunctionStrategy extends Strategy{
    
    public WrappableFunctionStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }

    @Override
    public Class execute() throws Exception {
	StackClass theClass = Stack.getEvaluationClass();
	StackMethod method = theClass.addStackMethod("public static " + ((WrappableFunctionExpression) getExpression()).getTemplate());
	WrapperFunctionManager.wrapMethod(theClass,method);
	return theClass.commit();
    }

}
