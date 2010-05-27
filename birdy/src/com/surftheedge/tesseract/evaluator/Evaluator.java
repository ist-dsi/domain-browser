package com.surftheedge.tesseract.evaluator;


import java.lang.reflect.Method;

import com.surftheedge.tesseract.expression.Expression;

public class Evaluator {

    public static final boolean EVALUATE = true;
    public static final boolean DONT_EVALUATE = false;
    @SuppressWarnings("unchecked")
    public static Object eval(Expression expression) throws Exception {
	Class evaluationContext = expression.getStrategy().execute();

	if (expression.getStrategy().canEval()) {
	    Method meth = evaluationContext.getDeclaredMethod("$eval");
	    return meth.invoke(null);
	} else {
	    return null;
	}

    }
}
