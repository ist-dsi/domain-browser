package com.surftheedge.tesseract.expression;


import java.util.ArrayList;
import java.util.List;

import com.surftheedge.tesseract.strategy.ClassDeclarationStrategy;

public class ClassDeclarationExpression extends Expression {

    private String className;
    private String body;
    private String superClass;
    private List<Expression> statements = new ArrayList<Expression>();
    
    public List<Expression> getStatements() {
        return statements;
    }
    
    public ClassDeclarationExpression() {
	super(new ClassDeclarationStrategy());
    }

    public void setSuperClass(String superClass) {
	this.superClass = superClass;
    }

    public String getSuperClass() {
	return superClass;
    }

    public void setClassName(String className) {
	this.className = className;
    }

    public String getClassName() {
	return className;
    }

    public void setBody(String body) {
	this.body = body;
    }

    public String getBody() {
	return body;
    }

}
