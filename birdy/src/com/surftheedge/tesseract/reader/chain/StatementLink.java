package com.surftheedge.tesseract.reader.chain;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.StatementExpression;

public class StatementLink extends ParsingLink {

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
	return expression.charAt(expression.length() - 1) == ';' || 
	(expression.charAt(0) == '{' && expression.charAt(expression.length() - 1) == '}');

    }

}
