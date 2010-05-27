package com.surftheedge.tesseract.reader.chain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.RegisterWrapFunctionExpression;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;
import com.surftheedge.tesseract.reader.chain.ParsingLink;


public class RegisterWrapFunctionLink extends ParsingLink{

    private Pattern pattern = Pattern.compile("\\%(before|after)\\s+([^)]+\\))+\\s+(add|del)\\s+([^)]+\\));");
    
    @Override
    public Expression execute(String expression) {
	RegisterWrapFunctionExpression expr = new RegisterWrapFunctionExpression();
	Matcher mm = pattern.matcher(expression);
	mm.find();
	expr.setPlace(mm.group(1));
	expr.setOperation(mm.group(3));
	expr.setContent(expression);
	expr.setOrigin(FunctionSignature.createSignatureForCanonical(mm.group(2)));
	expr.setInterceptor(FunctionSignature.createSignatureForCanonical(mm.group(4)));
	return expr;
    }

    @Override
    public boolean filter(String expression) {
	return pattern.matcher(expression).find();
    }

}
