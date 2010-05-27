
package com.surftheedge.tesseract.reader;


import java.util.ArrayList;
import java.util.List;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.expression.RegisterWrapFunctionExpression;
import com.surftheedge.tesseract.reader.chain.BlockLink;
import com.surftheedge.tesseract.reader.chain.ClassDeclarationLink;
import com.surftheedge.tesseract.reader.chain.DeclarationLink;
import com.surftheedge.tesseract.reader.chain.DynamicFunctionLink;
import com.surftheedge.tesseract.reader.chain.ExpressionLink;
import com.surftheedge.tesseract.reader.chain.FlowControlLink;
import com.surftheedge.tesseract.reader.chain.FunctionLink;
import com.surftheedge.tesseract.reader.chain.ImportLink;
import com.surftheedge.tesseract.reader.chain.MacroLink;
import com.surftheedge.tesseract.reader.chain.ParsingLink;
import com.surftheedge.tesseract.reader.chain.RegisterWrapFunctionLink;
import com.surftheedge.tesseract.reader.chain.StatementLink;
import com.surftheedge.tesseract.reader.chain.StaticImportLink;
import com.surftheedge.tesseract.reader.chain.WrappablFunctioneLink;

public class SudoParser {
    private List<ParsingLink> chain = new ArrayList<ParsingLink>();

    public SudoParser() {
	getChain().add(new MacroLink());
	getChain().add(new RegisterWrapFunctionLink());
	getChain().add(new BlockLink());
	getChain().add(new ClassDeclarationLink());
	getChain().add(new StaticImportLink());
	getChain().add(new ImportLink());
	getChain().add(new WrappablFunctioneLink());
	getChain().add(new DynamicFunctionLink());
	getChain().add(new FunctionLink());
	getChain().add(new DeclarationLink());
	getChain().add(new FlowControlLink());
	getChain().add(new StatementLink());
	getChain().add(new ExpressionLink());
    }

    public List<ParsingLink> getChain() {
	return chain;
    }

    public void setChain(List<ParsingLink> chain) {
	this.chain = chain;
    }

    public Expression parse(String expression) {
	for (ParsingLink link : getChain()) {
	    if (link.filter(expression)) {
		//System.out.println(link.getClass());
		return link.execute(expression);
	    }
	}
	return null;
    }
}
