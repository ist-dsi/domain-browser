package com.surftheedge.tesseract.reader.chain;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.MacroExpression;

public class MacroLink extends ParsingLink{
    
    Pattern pattern = Pattern.compile("macro\\s+/([^/]*|([^/]*\\\\/[^/]*)*)/\\s*\\{(.*)\\}", Pattern.MULTILINE | Pattern.DOTALL);

    @Override
    public Expression execute(String expression) {
	MacroExpression macroExpression = new MacroExpression();
	Matcher matcher = pattern.matcher(expression);
	matcher.find();
	macroExpression.setBody(matcher.group(3).trim());
	macroExpression.setRegex(matcher.group(1));
	macroExpression.setContent(expression);
	return macroExpression;
    }

    @Override
    public boolean filter(String expression) {
	return pattern.matcher(expression).find();
    }

}
