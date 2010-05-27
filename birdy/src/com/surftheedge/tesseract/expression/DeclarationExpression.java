package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.strategy.DeclarationStrategy;

public class DeclarationExpression extends Expression {

    public DeclarationExpression() {
	super(new DeclarationStrategy());
    }

}
