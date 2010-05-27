package com.surftheedge.tesseract.expression;

import java.util.ArrayList;
import java.util.List;

import com.surftheedge.tesseract.strategy.BlockStrategy;

public class BlockExpression extends Expression {

    private List<Expression> statements = new ArrayList<Expression>();
    
    public List<Expression> getStatements() {
        return statements;
    }

    public BlockExpression() {
	super(new BlockStrategy());
    }

}
