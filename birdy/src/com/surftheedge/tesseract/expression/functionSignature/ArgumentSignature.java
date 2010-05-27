package com.surftheedge.tesseract.expression.functionSignature;

public class ArgumentSignature {
    
    private String type;
    
    private String variableName;
    
    public ArgumentSignature(String type, String variableName) {
	super();
	this.type = type;
	this.variableName = variableName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getVariableName() {
        return variableName;
    }
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
