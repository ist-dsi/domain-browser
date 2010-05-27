package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.FunctionExpression;
import com.surftheedge.tesseract.expression.functionSignature.FunctionSignature;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;
import com.surftheedge.tesseract.stack.StackMethod;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

public class FunctionStrategy extends Strategy implements Parallelizable, Classable{
    StackMethod method;
    
    public StackMethod getMethod() {
        return method;
    }

    public void setMethod(StackMethod method) {
        this.method = method;
    }

    public FunctionStrategy() {
	super(Evaluator.DONT_EVALUATE);
    }
    
    @Override
    public Class execute() throws Exception {
	StackClass theClass = Stack.getEvaluationClass();
	prepare(theClass);
	return theClass.commit();
    }

    private void prepare(StackClass theClass) throws CannotCompileException {
	String template = "public static " + getExpression().getContent();
	theClass.addStackMethod(template);
    }

    public void pre(StackClass theClass) throws CannotCompileException {
	FunctionSignature func = ((FunctionExpression) getExpression()).getFunction();
	setMethod(theClass.addAbstractStackMethod("public static " + func.getStub() + "{ " + func.getDefaultReturn() + " }"));
    }
    
    public void post(StackClass evalClass) throws CannotCompileException {
	String body = getExpression().getContent().substring(getExpression().getContent().indexOf('{'));
	FunctionSignature func = ((FunctionExpression) getExpression()).getFunction();
	getMethod().addBody(func.cookBody(body));
	getMethod().removeAbstract();
    }

    public void add(CtClass class1) throws CannotCompileException {
	CtMethod method = CtMethod.make("public " + getExpression().getContent(),class1);
	class1.addMethod(method);
    }

}
