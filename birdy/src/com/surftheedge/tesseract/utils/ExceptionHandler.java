package com.surftheedge.tesseract.utils;

import com.surftheedge.tesseract.stack.Stack;

import javassist.CannotCompileException;
import javassist.compiler.CompileError;
import javassist.compiler.NoFieldException;
import javassist.compiler.SyntaxError;

public class ExceptionHandler {

    public static void failedCompilation(CannotCompileException e) {
	Stack.failedCompilation();

	if (e.getCause().getClass() == NoFieldException.class) {
	    System.out.println("undeclared variable " + ((NoFieldException) e.getCause()).getField());
//	    e.printStackTrace();
	}else
	if (e.getCause().getClass() == SyntaxError.class) {
	    System.out.println(((SyntaxError) e.getCause()).getMessage());
//	    e.printStackTrace();
	} else
	if(e.getCause().getClass() == CompileError.class){
	    System.out.println(e.getMessage());
	}else{
	    e.printStackTrace();
	    System.out.println(e.getCause().getClass());
	}
    }
}
