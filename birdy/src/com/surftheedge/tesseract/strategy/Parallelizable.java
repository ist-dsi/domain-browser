package com.surftheedge.tesseract.strategy;

import com.surftheedge.tesseract.stack.StackClass;

import javassist.CannotCompileException;

public interface Parallelizable {
    public void pre(StackClass evalClass) throws CannotCompileException;
    public void post(StackClass evalClass) throws CannotCompileException;
}
