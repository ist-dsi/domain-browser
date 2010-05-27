package com.surftheedge.tesseract.reader.chain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.ImportExpression;


public class ImportLink extends ParsingLink {

    private Pattern importPattern = Pattern.compile("import\\s+([^;]+);");

    private Pattern getImportPattern() {
	return importPattern;
    }

    @Override
    public
    Expression execute(String expression) {
	Matcher matches = getImportPattern().matcher(expression.trim());
	matches.matches();
	ImportExpression expr = new ImportExpression(matches.group(1));
	expr.setContent(expression);
	return expr;
    }

    @Override
    public
    boolean filter(String expression) {
	return getImportPattern().matcher(expression.trim()).find();
    }

}
