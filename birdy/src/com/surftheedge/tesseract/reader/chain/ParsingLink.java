package com.surftheedge.tesseract.reader.chain;

import com.surftheedge.tesseract.expression.Expression;

abstract public class ParsingLink {
    public abstract boolean filter(String expression);

    public abstract Expression execute(String expression);
}
