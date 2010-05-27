package com.surftheedge.tesseract.expression.functionSignature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunctionSignature {
    private String returnType;
    private String name;
    private List<ArgumentSignature> arguments =  new ArrayList<ArgumentSignature>();

    private String parameters;
    private String onlyTypes;
    public String getOnlyTypes() {
        return onlyTypes;
    }
    public void setOnlyTypes(String onlyTypes) {
        this.onlyTypes = onlyTypes;
    }

    private String args;  
    
    public String getReturnType() {
        return returnType;
    }
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public List<ArgumentSignature> getArguments(){
	return arguments;
    }
    
    private static final String TYPE = "([^\\s]+|[^<]+[^>]+)";
    
    public static FunctionSignature createSignature (String stub){
	Pattern pattern = Pattern.compile( TYPE + "\\s+([^\\s]+)\\s*\\(([^)]*)\\).*");
	Matcher matcher = pattern.matcher(stub);
	matcher.matches();
	FunctionSignature f = new FunctionSignature();
	f.setReturnType(matcher.group(1));
	f.setName(matcher.group(2));
	String rawArguments = matcher.group(3);
	Pattern argumentsPattern = Pattern.compile(TYPE + "\\s+([^,$])+\\s*,?");
	Matcher argumentsMatcher = argumentsPattern.matcher(rawArguments);
	while(argumentsMatcher.find()) {
	    f.getArguments().add(new ArgumentSignature(argumentsMatcher.group(1),argumentsMatcher.group(2)));
	}
	return f;
    }
    
    public static FunctionSignature createSignatureForCanonical(String stub){
	FunctionSignature f = new FunctionSignature();
	f.setReturnType("Unknow");
	String[] split = stub.split("\\(");
	f.setName(split[0]);
	String rawArguments = split[1].substring(0,split[1].length()-1);
	Pattern argumentsPattern = Pattern.compile("([^,]+|[^<]+[^>]+),?");
	Matcher argumentsMatcher = argumentsPattern.matcher(rawArguments);
	int i=0;
	while(argumentsMatcher.find()) {
	    f.getArguments().add(new ArgumentSignature(argumentsMatcher.group(1),"arg" + i++));
	}
	return f;
    }
    
    private void generateParamsAndArgs() {
	parameters="";
	args="";
	for (Iterator<ArgumentSignature> iterator = getArguments().iterator(); iterator.hasNext();) {
	    ArgumentSignature type = iterator.next();
	    if (iterator.hasNext()) {
		parameters += type.getType() + " " + type.getVariableName() + ",";
		args += type.getVariableName() + ", ";
		onlyTypes += type.getType()+",";
	    } else {
		parameters += type.getType() + " " + type.getVariableName();
		args += type.getVariableName();
	    }
	}
    }
    
    public String getFunctionParameters() {
	if (parameters == null) {
	    generateParamsAndArgs();
	}
	return parameters;
    }

    public String getCallingArgs() {
	if (args == null) {
	    generateParamsAndArgs();
	}
	return args;
    }
    
    public String getStub(){
	return getReturnType() + " " + getName() + "(" + getFunctionParameters() + ")";
    }
    
    public String getMinimalStub(){
	return getName() + "(" + getFunctionParameters() + ")";
    }
    
    public byte a(){
	return 0;
    }
    
    public String getDefaultReturn()
    {
	if (getReturnType().equals("byte")) {
	    return "return 0;";
	} else if (getReturnType().equals("short")) {
	    return "return 0;";
	} else if (getReturnType().equals("int")) {
	    return "return 0;";
	} else if (getReturnType().equals("long")) {
	    return "return 0;";
	} else if (getReturnType().equals("float")) {
	    return "return 0F;";
	} else if (getReturnType().equals("double")) {
	    return "return 0;";
	} else if (getReturnType().equals("char")) {
	    return "return 0;";
	} else if (getReturnType().equals("boolean")) {
	    return "return false;";
	} else if (getReturnType().equals("void")) {
	    return "return;";
	} else {
	    return "return null;";
	}
    }
    
    public String cookBody(String body){
	int i=1;
	String externalBody = "{";
	for (ArgumentSignature arg : getArguments()) {	
	    externalBody += arg.getType() + " " + arg.getVariableName() + " = $" + i++ +";";
	}
	externalBody += body.substring(1, body.length()-2) + "}";
	return externalBody;
    }
}
