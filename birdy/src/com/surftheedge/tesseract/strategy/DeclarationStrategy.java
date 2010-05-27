package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMember;
import javassist.CtMethod;

public class DeclarationStrategy extends Strategy implements Parallelizable,Classable{

    public DeclarationStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }
    
    public void prepare(StackClass stackTop) throws CannotCompileException{
	String template = "static public " + getExpression().getContent();
	stackTop.addStackField(template);
    }

    @Override
    public Class execute() throws Exception {
	StackClass theClass = Stack.getEvaluationClass();
	prepare(theClass);
	return theClass.commit();
    }

    public void pre(StackClass stackTop) throws CannotCompileException {
	prepare(stackTop);
    }

    public void post(StackClass evalClass) throws CannotCompileException {
    }

    public void add(CtClass class1) throws CannotCompileException {
	String template = "public " + getExpression().getContent();
	CtField method = CtField.make(template, class1);
	class1.addField(method);
    }
}
