package com.surftheedge.tesseract.expression;

import com.surftheedge.tesseract.strategy.ImportStrategy;

public class ImportExpression extends Expression {

    private String aPackage;

    public ImportExpression(String aPackage) {
	super(new ImportStrategy());
	setPackage(aPackage);
    }

    public String getPackage() {
	return aPackage;
    }

    public void setPackage(String package1) {
	aPackage = package1;
    }
}
