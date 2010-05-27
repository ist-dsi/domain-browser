package com.surftheedge.tesseract.reader.chain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.BlockExpression;
import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.reader.Reader;


public class BlockLink extends ParsingLink {

    private Pattern pattern = Pattern.compile("block\\s*\\{(.+)\\}",Pattern.MULTILINE | Pattern.DOTALL);

    @Override
    public Expression execute(String expression) {
	BlockExpression expr = new BlockExpression();
	Matcher matcher = pattern.matcher(expression);
	matcher.find();
	expr.setContent(expression);
	expr.getStatements().addAll(Reader.readMany(matcher.group(1)));
	return expr;
    }

    @Override
    public boolean filter(String expression) {
	return pattern.matcher(expression).find();
    }

}
