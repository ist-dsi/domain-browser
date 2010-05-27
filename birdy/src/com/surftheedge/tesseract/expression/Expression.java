package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.strategy.Strategy;

abstract public class Expression {

    private String content;
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
	this.strategy = strategy;
	this.strategy.setExpression(this);
    }

    public Strategy getStrategy() {
	return strategy;
    }

    public Expression(Strategy strategy) {
	setStrategy(strategy);
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

}
