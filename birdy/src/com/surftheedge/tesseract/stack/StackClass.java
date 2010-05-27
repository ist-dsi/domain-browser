package com.surftheedge.tesseract.stack;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;

public class StackClass{
    
    private CtClass aClass;
    private Class actualClass;
    public StackClass(CtClass theClass){
	aClass = theClass;
    }
    
    private CtClass getCtClass() {
        return aClass;
    }
    
    public Class getCompiledClass(){
	return actualClass;
    }
    
    private void setCompiledClass(Class aClass){
	actualClass = aClass;
    }

    public StackMethod addAbstractStackMethod(String stub) throws CannotCompileException{
	CtMethod m = CtNewMethod.make(stub, getCtClass());
	getCtClass().addMethod(m);
	return new StackMethod(m);
    }
    
    public StackMethod addStackMethod(String template) throws CannotCompileException{
	CtMethod ctMethod = CtNewMethod.make(template, getCtClass());
	getCtClass().addMethod(ctMethod);
	return new StackMethod(ctMethod);
    }
    
    public void addStackField(String template) throws CannotCompileException{
	CtField field = CtField.make(template, getCtClass());
	getCtClass().addField(field);
    }

    
    public Class commit() throws CannotCompileException{
	setCompiledClass(getCtClass().toClass());
	return getCompiledClass();
    }
    
    public void removeAbstract(){
	getCtClass().setModifiers(getCtClass().getModifiers() & ~Modifier.ABSTRACT);
    }
}
