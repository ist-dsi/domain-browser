package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.strategy.StatementStrategy;

public class StatementExpression extends Expression {

    public StatementExpression() {
	super(new StatementStrategy());
    }

}
