package com.surftheedge.tesseract.strategy;

import javassist.CannotCompileException;
import javassist.CtClass;

public interface Classable {
    public void add(CtClass aClass) throws CannotCompileException;
}
