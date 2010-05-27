package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.strategy.MacroStrategy;

public class MacroExpression extends Expression{
    
    private String regex;
    private String body;
    
    public MacroExpression() {
	super(new MacroStrategy());
    }

    public void setRegex(String regex) {
	this.regex = regex;
    }

    public String getRegex() {
	return regex;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public String getBody() {
	return body;
    }

}
