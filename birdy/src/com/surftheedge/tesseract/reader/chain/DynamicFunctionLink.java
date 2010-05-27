package com.surftheedge.tesseract.reader.chain;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.DynamicFunctionExpression;
import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;

public class DynamicFunctionLink extends ParsingLink {

    private Pattern dynaFunctionPattern = Pattern.compile("dynamic\\s+([^\\s]+\\s+[^\\s]+\\s*\\([^)]*\\))\\s*(\\{.*)", Pattern.MULTILINE | Pattern.DOTALL);

    private Pattern getDynaFunctionPattern() {
	return dynaFunctionPattern;
    }

    @Override
    public
    Expression execute(String expression) {
	Matcher matches = getDynaFunctionPattern().matcher(expression);
	matches.matches();
	DynamicFunctionExpression expr = new DynamicFunctionExpression();
	expr.setFunction(FunctionSignature.createSignature(matches.group(1)));
	expr.setBody(matches.group(2));
	expr.setContent(expression);
	return expr;
    }

    

    @Override
    public
    boolean filter(String expression) {
	return getDynaFunctionPattern().matcher(expression).find();
    }

}
