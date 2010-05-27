package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;
import com.surftheedge.tesseract.strategy.WrappableFunctionStrategy;


public class WrappableFunctionExpression extends Expression {

    FunctionSignature function = new FunctionSignature();
    String template = "";
    
    public FunctionSignature getFunction() {
        return function;
    }
    public void setFunction(FunctionSignature function) {
        this.function = function;
    }
    public String getTemplate() {
        return template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }
    public WrappableFunctionExpression() {
	super(new WrappableFunctionStrategy());
    }
}
