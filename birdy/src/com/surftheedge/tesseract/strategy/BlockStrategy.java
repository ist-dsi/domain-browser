package com.surftheedge.tesseract.strategy;


import java.util.List;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.BlockExpression;
import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;

public class BlockStrategy extends Strategy {

    public BlockStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class execute() throws Exception {
	List<Expression> list = ((BlockExpression) getExpression()).getStatements();
	StackClass theClass = Stack.getEvaluationClass();
	for (Expression expression : list) {
	    Parallelizable e = (Parallelizable) expression.getStrategy();
	    e.pre(theClass);
	}
	for (Expression expression : list) {
	    Parallelizable e = (Parallelizable) expression.getStrategy();
	    e.post(theClass);
	}
	theClass.removeAbstract();
	return theClass.commit();
    }

}
