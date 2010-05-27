package com.surftheedge.tesseract.utils;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.strategy.Printable;


public class Printer {
    
    public static void print(Object object, Expression expression) {
	try {
	    // I can't find a way to this in a not stupid way!!!
	    ((Printable) expression.getStrategy()).print(object);
	} catch (ClassCastException e) {
	}
    }
}
