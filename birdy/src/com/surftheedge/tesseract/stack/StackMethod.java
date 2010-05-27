package com.surftheedge.tesseract.stack;

import javassist.CannotCompileException;
import javassist.CtMethod;
import javassist.Modifier;

public class StackMethod {
    public CtMethod method;

    public StackMethod(CtMethod method) {
	super();
	this.method = method;
    }

    public CtMethod getMethod() {
	return method;
    }

    public void addBody(String body) throws CannotCompileException {
	getMethod().setBody(body);
    }

    public void removeAbstract() {
	//getMethod().setModifiers(getMethod().getModifiers() & ~Modifier.ABSTRACT);
	getMethod().setModifiers(getMethod().getModifiers() | Modifier.STATIC);
    }

}
