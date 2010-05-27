package com.surftheedge.tesseract.reader.chain;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.ExpressionExpression;

public class ExpressionLink extends ParsingLink {

    @Override
    public
    Expression execute(String expression) {
	Expression expr = new ExpressionExpression();
	expr.setContent(expression);
	return expr;
    }

    @Override
    public
    boolean filter(String expression) {
	return expression.charAt(expression.length() - 1) != ';';
    }

}
