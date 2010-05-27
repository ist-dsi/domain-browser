package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.expression.Expression;

public abstract class Strategy {

    private Expression expression;
    private boolean canEval;

    public Strategy(boolean canEval) {
	setCanEval(canEval);
    }

    public boolean canEval() {
	return canEval;
    }

    private void setCanEval(boolean evalueable) {
	this.canEval = evalueable;
    }

    public Expression getExpression() {
	return expression;
    }

    public void setExpression(Expression expression) {
	this.expression = expression;
    }

    abstract public Class execute() throws Exception;
}
