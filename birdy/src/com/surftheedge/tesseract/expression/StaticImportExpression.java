package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.strategy.StaticImportStrategy;

public class StaticImportExpression extends Expression {

    private String aPackage;

    public StaticImportExpression() {
	super(new StaticImportStrategy());
    }

    public String getPackage() {
	return aPackage;
    }

    public void setPackage(String package1) {
	aPackage = package1;
    }

}
