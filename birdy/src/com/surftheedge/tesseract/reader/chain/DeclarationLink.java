package com.surftheedge.tesseract.reader.chain;


import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.DeclarationExpression;
import com.surftheedge.tesseract.expression.Expression;

public class DeclarationLink extends ParsingLink {

    @Override
    public
    Expression execute(String expression) {
	Expression expr = new DeclarationExpression();
	expr.setContent(expression);
	return expr;
    }

    @Override
    public
    boolean filter(String expression) {
	return Pattern.compile("[^\\s]+\\s+[^\\s+\\-*/%-<>&^|]+\\s*=\\s*[^;]+;", Pattern.MULTILINE | Pattern.DOTALL).matcher(expression).matches();

    }

}
