package com.surftheedge.tesseract.reader.chain;


import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.StatementExpression;

public class FlowControlLink extends ParsingLink {

    @Override
    public
    Expression execute(String expression) {
	Expression expr = new StatementExpression();
	expr.setContent(expression);
	return expr;
    }

    @Override
    public
    boolean filter(String expression) {
	return Pattern.compile("(if|for|while|do|switch)\\s*(\\(|\\{).+",Pattern.MULTILINE | Pattern.DOTALL).matcher(expression).find();

    }

}
