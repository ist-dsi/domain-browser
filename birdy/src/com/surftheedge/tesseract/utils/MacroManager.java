package com.surftheedge.tesseract.utils;


import java.util.ArrayList;
import java.util.List;

import com.surftheedge.tesseract.expression.MacroExpression;
import com.surftheedge.tesseract.reader.Reader;
import com.surftheedge.tesseract.reader.chain.ParsingLink;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

public class MacroManager {

    public static class MacroBean {
	private ParsingLink link;
	private String regex;
	private String body;
	
	public MacroBean(ParsingLink link, String regex, String body) {
	    super();
	    this.link = link;
	    this.regex = regex;
	    this.body = body;
	}

	public void setLink(ParsingLink link) {
	    this.link = link;
	}

	public ParsingLink getLink() {
	    return link;
	}

	public void setRegex(String regex) {
	    this.regex = regex;
	}

	public String getRegex() {
	    return regex;
	}

	public void setBody(String body) {
	    this.body = body;
	}

	public String getBody() {
	    return body;
	}
    }

    private static int macros = 0;
    private static List<MacroBean> macroList = new ArrayList<MacroBean>();

    public static void setMacros(int macros) {
	MacroManager.macros = macros;
    }

    public static int getMacros() {
	return macros;
    }

    public static ParsingLink createNewLink(MacroExpression macroExpression) throws CannotCompileException, RuntimeException, NotFoundException, InstantiationException, IllegalAccessException{
	ClassPool pool = ClassPool.getDefault();
	for (String aClass : ImportManager.getImports()) {
	    pool.importPackage(aClass);
	}
	// this is weird, hard to understand, and heavy code. But is sooooooo cooooool
	CtClass aClass = pool.makeClass("$MacroLink" + macros++, pool.get("com.surftheedge.tesseract.reader.chain.ParsingLink"));
	aClass.setModifiers(aClass.getModifiers() & ~Modifier.ABSTRACT);
	String template = "public java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(" + InternalRepresentation.convert(macroExpression.getRegex())+ ");";
	CtField field = CtField.make(template, aClass);
	aClass.addField(field);
	template = "private String warpCoreBreach = " + InternalRepresentation.convert(macroExpression.getBody().trim())+ ";";
	field = CtField.make(template, aClass);
	aClass.addField(field);
	
	template = "public com.surftheedge.tesseract.expression.Expression execute(String expression) {" +
	"java.util.regex.Matcher matcher = pattern.matcher(expression);" +
	"java.lang.StringBuffer buf = new java.lang.StringBuffer(\"\");" +
	"java.lang.String trigger = new String(warpCoreBreach);" +
	"while(matcher.find()){" +
	    "String temp = \"\";" +
	    "for(int j=1; j <= matcher.groupCount(); j++){"+
		"java.util.regex.Matcher coreReplacer = java.util.regex.Pattern.compile(\"\\\\$\" + j + \"\\\\$\").matcher(trigger);"+
		"while(coreReplacer.find()){" +
		    "temp = coreReplacer.replaceAll(matcher.group(j));" +
		"}" +
	    "}" +
	    "matcher.appendReplacement(buf,temp);"+
	    "trigger = temp;" +
	"}" +
	"matcher.appendTail(buf);" + 
	"String result = buf.toString();"+
	"if (result.length() == 0) result = warpCoreBreach;" +
	"String evalPattern = " +InternalRepresentation.convert("(%|&)\\{(([^}]*\\\\\\}[^}]*)+|[^}]+)\\}")+";" +
	"java.util.regex.Matcher coreReplacer = java.util.regex.Pattern.compile(evalPattern).matcher(result);"+
	
	"buf = new java.lang.StringBuffer(\"\");" +
	"while(coreReplacer.find()){" +	
		"Object o = com.surftheedge.tesseract.evaluator.Evaluator.eval(com.surftheedge.tesseract.reader.Reader.getParser().parse(coreReplacer.group(2)));" +
		"if (coreReplacer.group(1).equals(\"%\")){" +
		"coreReplacer.appendReplacement(buf,o.toString());" +
		"}else{" +
		"coreReplacer.appendReplacement(buf,com.surftheedge.tesseract.utils.InternalRepresentation.revert(o.toString()));" +
		"}" +
	"}" +
	"coreReplacer.appendTail(buf);" +
	"String result2 = buf.toString();"+
	"if (result2.length() == 0) result2 = result;" + 
	"return com.surftheedge.tesseract.reader.Reader.getParser().parse(result2);}";
	CtMethod method = CtMethod.make(template, aClass);
	aClass.addMethod(method);
	
	template = "public boolean filter(String expression){return pattern.matcher(expression).find();}";
	method = CtMethod.make(template,aClass);
	aClass.addMethod(method);
	
	Class theClass = aClass.toClass();
	ParsingLink link = (ParsingLink) theClass.newInstance();
	
	MacroBean bean = new MacroBean(link,macroExpression.getRegex(),macroExpression.getBody());
	getMacroList().add(bean);
	
	return link;
    }

    public static void macros() {
	int i = 0;
	for (MacroBean macro : getMacroList()) {
	    System.out.println(++i + " : macro " + macro.getRegex() + " => " + macro.getBody());
	}
    }

    public static void deleteMacro(int i) {
	MacroBean bean = getMacroList().get(i-1);
	Reader.getParser().getChain().remove(bean.getLink());
	getMacroList().remove(i-1);
    }

    private static List<MacroBean> getMacroList() {
	return macroList;
    }
}
