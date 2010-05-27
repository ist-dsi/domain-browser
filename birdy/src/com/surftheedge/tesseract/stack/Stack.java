package com.surftheedge.tesseract.stack;

import com.surftheedge.tesseract.utils.ImportManager;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;

public class Stack {

    private static int EVALUATION_LEVEL = 0;
    private static CtClass topLevelEval;
    private static CtClass lastTopEval;
    
    static public void init(){
	ClassPool pool = ClassPool.getDefault();
	if (getTopLevelEval() == null) {
	    setTopLevelEval(pool.makeClass("TopLevelEval"));
	    setLastTopEval(null);
	    try {
		getTopLevelEval().toClass();
	    } catch (CannotCompileException e) {
		e.printStackTrace();
	    }
	}
    }
    
    static public void failedCompilation() {
	EVALUATION_LEVEL--;
	setTopLevelEval(getLastTopEval());
    }
    
    public static CtClass getLastTopEval() {
	return lastTopEval;
    }

    public static void setLastTopEval(CtClass lastTopEval) {
	Stack.lastTopEval = lastTopEval;
    }

    public static CtClass getTopLevelEval() {
	return topLevelEval;
    }

    public static void setTopLevelEval(CtClass topLevelEval) {
	Stack.topLevelEval = topLevelEval;
    }
    
    public static String getCurrentEvalLevel(){
	return getTopLevelEval().getName();
    }

    static public StackClass getEvaluationClass() {
	ClassPool pool = ClassPool.getDefault();
	for (String aClass : ImportManager.getImports()) {
	    pool.importPackage(aClass);
	}
	setLastTopEval(getTopLevelEval());
	setTopLevelEval(pool.makeClass("$Eval" + EVALUATION_LEVEL++, topLevelEval));
	return new StackClass(getTopLevelEval());
    }
}
