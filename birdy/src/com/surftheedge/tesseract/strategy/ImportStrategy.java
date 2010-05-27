package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.ImportExpression;
import com.surftheedge.tesseract.utils.ImportManager;


public class ImportStrategy extends Strategy {

    public ImportStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }

    @Override
    public Class execute() throws Exception {
	ImportManager.addImport(((ImportExpression) getExpression()).getPackage());
	return null;
    }
}
