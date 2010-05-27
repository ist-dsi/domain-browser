package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.MacroExpression;
import com.surftheedge.tesseract.reader.Reader;
import com.surftheedge.tesseract.reader.chain.ParsingLink;
import com.surftheedge.tesseract.utils.MacroManager;


public class MacroStrategy extends Strategy {

    public MacroStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }

    @Override
    public Class execute() throws Exception {
	ParsingLink link = MacroManager.createNewLink(((MacroExpression) getExpression()));
	Reader.getParser().getChain().add(0, link);
	return null;
    }

}
