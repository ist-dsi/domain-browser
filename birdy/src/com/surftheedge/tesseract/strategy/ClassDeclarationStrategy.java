package com.surftheedge.tesseract.strategy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.ClassDeclarationExpression;
import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.utils.ImportManager;

public class ClassDeclarationStrategy extends Strategy {

    public ClassDeclarationStrategy() {
        super(Evaluator.DONT_EVALUATE);
    }

    @Override
    public Class execute() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        for (String aClass : ImportManager.getImports()) {
            pool.importPackage(aClass);
        }
        ClassDeclarationExpression expression = ((ClassDeclarationExpression) getExpression());
        CtClass aClass = pool.makeClass(expression.getClassName());
        if (expression.getSuperClass() != null) {
            CtClass superClass = pool.get(expression.getSuperClass());
            aClass.setSuperclass(superClass);
        }

        Class importClass = Class.forName(Stack.getCurrentEvalLevel());
        for (Field field : importClass.getFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                CtField aField =
                        CtField.make(
                                "static public " + ((Class) field.getGenericType()).getCanonicalName() + " " + field.getName()
                                        + " = " + Stack.getCurrentEvalLevel() + "." + field.getName() + ";", aClass);
                aClass.addField(aField);
            }
        }

        for (Method method : importClass.getMethods()) {
            if (Modifier.isStatic(method.getModifiers()) && !method.getName().equals("$eval")) {
                createMethodForClass(Stack.getCurrentEvalLevel(), aClass, method);
            }
        }

        for (Expression expr : expression.getStatements()) {
            Classable e = (Classable) expr.getStrategy();
            e.add(aClass);
        }

        return aClass.toClass();
    }

    private void createMethodForClass(String staticClassName, CtClass theClass, Method method) throws CannotCompileException {
        // Stub
        String template = "public static " + ((Class) method.getGenericReturnType()).getCanonicalName() + " " + method.getName();

        // Function params
        int i = 0;
        String methodParamList = "(";
        String invokeParamList = "(";
        int size = method.getGenericParameterTypes().length;
        for (Class type : method.getParameterTypes()) {
            methodParamList += type.getCanonicalName() + " arg" + i;
            invokeParamList += "arg" + i++;
            if (i != size) {
                methodParamList += ",";
                invokeParamList += ",";
            }

        }
        methodParamList += ")";
        invokeParamList += ")";

        template += methodParamList;
        i = 0;
        if (method.getExceptionTypes().length > 0) {
            template += " throws ";
            for (Class type : method.getExceptionTypes()) {
                template += type.getCanonicalName();
                i++;
                if (i != method.getExceptionTypes().length) {
                    template += ", ";
                }
            }
        }
        // Body
        template += "{";
        if (method.getReturnType() == Void.class) {
            template += staticClassName + "." + method.getName() + invokeParamList + ";";
        } else {
            template += "return " + staticClassName + "." + method.getName() + invokeParamList + ";";
        }
        template += "}";
        CtMethod ctMethod = CtNewMethod.make(template, theClass);
        theClass.addMethod(ctMethod);
    }
}
