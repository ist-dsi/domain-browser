package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;
import com.surftheedge.tesseract.strategy.FunctionStrategy;


public class FunctionExpression extends Expression {

    FunctionSignature function;

    public FunctionSignature getFunction() {
	return function;
    }

    public void setFunction(FunctionSignature function) {
	this.function = function;
    }

    public FunctionExpression() {
	super(new FunctionStrategy());
    }

}
