package com.surftheedge.tesseract.reader.chain;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.ClassDeclarationExpression;
import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.reader.Reader;

public class ClassDeclarationLink extends ParsingLink {

    private Pattern pattern = Pattern.compile("class\\s+([^\\\\s]+)\\s+(extends\\s+([^\\s{]+))?\\s*\\{(.*)", Pattern.MULTILINE | Pattern.DOTALL );

    @Override
    public Expression execute(String expression) {
	ClassDeclarationExpression expr = new ClassDeclarationExpression();
	Matcher matcher = pattern.matcher(expression);
	matcher.find();
	expr.setContent(expression);
	expr.setClassName(matcher.group(1));
	if (matcher.group(2) != null){
	    expr.setSuperClass(matcher.group(3));
	}
	expr.setBody(matcher.group(4));
	expr.getStatements().addAll(Reader.readMany(expr.getBody()));
	return expr;
    }

    @Override
    public boolean filter(String expression) {
	return pattern.matcher(expression).find();
    }
}
