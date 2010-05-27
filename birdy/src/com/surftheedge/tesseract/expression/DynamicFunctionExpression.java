package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;
import com.surftheedge.tesseract.strategy.DynamicFunctionStrategy;


public class DynamicFunctionExpression extends Expression{

    private String body;
    
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private FunctionSignature signature;
    
    public FunctionSignature getFunction() {
        return signature;
    }

    public void setFunction(FunctionSignature f) {
        this.signature = f;
    }
    
    public DynamicFunctionExpression() {
	super(new DynamicFunctionStrategy());
    }

}
