package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;


public class StatementStrategy extends Strategy {

    public StatementStrategy() {
	super(Evaluator.EVALUATE);
    }

    @Override
    public Class execute() throws Exception {
	StackClass theClass = Stack.getEvaluationClass();

	String template = "public static void $eval () { " + getExpression().getContent() + " }";
	theClass.addStackMethod(template);
	return theClass.commit();
    }
}
