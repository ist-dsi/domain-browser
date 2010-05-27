package com.surftheedge.tesseract.reader.chain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.WrappableFunctionExpression;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;


public class WrappablFunctioneLink extends ParsingLink {

    private Pattern wrappFunctionPattern = Pattern.compile("wrappable\\s+([^\\s]+\\s+[^\\s]+\\s*\\([^)]*\\))\\s*(\\{.*)", Pattern.MULTILINE | Pattern.DOTALL);

    private Pattern getWrappFunctionPattern() {
	return wrappFunctionPattern;
    }

    @Override
    public Expression execute(String expression) {
	Matcher matches = getWrappFunctionPattern().matcher(expression);
	matches.matches();
	WrappableFunctionExpression expr = new WrappableFunctionExpression();
	expr.setFunction(FunctionSignature.createSignature(matches.group(1)));
	expr.setContent(expression);
	expr.setTemplate(matches.group(1) + matches.group(2));
	return expr;
    }

    @Override
    public boolean filter(String expression) {
	return getWrappFunctionPattern().matcher(expression).find();
    }
}
