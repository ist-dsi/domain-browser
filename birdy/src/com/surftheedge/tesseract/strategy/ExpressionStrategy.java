package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.stack.StackClass;

import javassist.CtClass;
import javassist.CtMember;
import javassist.CtMethod;

public class ExpressionStrategy extends Strategy implements Printable{

    public ExpressionStrategy() {
	super(Evaluator.EVALUATE);
    }

    @Override
    public Class execute() throws Exception {
	StackClass theClass = Stack.getEvaluationClass(); 
	String template = "public static Object $eval () { " + " return com.surftheedge.tesseract.utils.InternalRepresentation.convert("
		+ getExpression().getContent() + "); }";
	theClass.addStackMethod(template);
	return theClass.commit();
    }

  
    public void print(Object object) {
	System.out.println(object);
    }
}
