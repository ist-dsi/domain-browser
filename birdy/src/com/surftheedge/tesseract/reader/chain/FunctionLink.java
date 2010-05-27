package com.surftheedge.tesseract.reader.chain;

import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.FunctionExpression;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;


public class FunctionLink extends ParsingLink {

    @Override
    public
    Expression execute(String expression) {
	FunctionExpression expr = new FunctionExpression();
	expr.setContent(expression);
	expr.setFunction(FunctionSignature.createSignature(expression.split("\\{")[0]));
	return expr;
    }

    @Override
    public
    boolean filter(String expression) {
	return Pattern.compile("[^\\s]+\\s+[^\\s]+\\s*\\([^)]*\\)\\s*\\{.*",Pattern.MULTILINE | Pattern.DOTALL).matcher(expression).find();
    }

}
