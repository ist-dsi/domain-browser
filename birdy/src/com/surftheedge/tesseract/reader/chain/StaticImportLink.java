package com.surftheedge.tesseract.reader.chain;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.StaticImportExpression;

public class StaticImportLink extends ParsingLink {

    private Pattern pattern = Pattern.compile("static\\simport\\s+([^;]+);");

    @Override
    public
    Expression execute(String expression) {
	StaticImportExpression expr = new StaticImportExpression();
	Matcher matcher = pattern.matcher(expression);
	matcher.matches();
	expr.setContent(expression);
	expr.setPackage(matcher.group(1));
	return expr;
    }

    @Override
    public
    boolean filter(String expression) {
	return pattern.matcher(expression).find();
    }
}
