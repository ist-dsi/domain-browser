package com.surftheedge.tesseract.jsbridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

import com.surftheedge.tesseract.JSConsole;

public class TopLevelContext extends ImporterTopLevel {

    private static final long serialVersionUID = 1L;
    private JSConsole engine;
    public static String[] names = { "map", "reduce", "filter", "print", "printf", "whatis", "find","run","reloadRuntime" };
    public TopLevelContext() {
    }

    public TopLevelContext(Context cx) {
	super();
	init(cx);
    }

    public void init(Context cx) {
	initStandardObjects(cx, false);
	defineFunctionProperties(names, TopLevelContext.class, ScriptableObject.DONTENUM);
    }

    public static void run(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	String filename = (String) args[0];
	File file = new File(filename);
	FileReader fir;
	try {
	    fir = new FileReader(file);
	    BufferedReader br = new BufferedReader(fir);
	    JSConsole.instance.loopFile(br, filename);
	} catch (FileNotFoundException e) {
	    System.err.print("File '" + filename + "' was not found");
	}
    }
    public static void reloadRuntime(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	JSConsole.instance.loadResources();
    }

    public static Object map(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	NativeJavaObject njo = (NativeJavaObject) args[0];
	NativeArray array = (NativeArray) cx.newArray(thisObj, 0);
	Function f = (Function) args[1];
	Collection list = (Collection) njo.unwrap();
	int i = 0;
	for (Object o : list) {
	    Object[] argx = { o, Context.javaToJS(i, f), njo };
	    Object r = f.call(cx, thisObj, f, argx);
	    array.put((int) array.getLength(), array, Context.javaToJS(r, f));
	    i++;
	}
	return array;
    }

    public static void print(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	for (Object result : args) {
	    System.out.print(Context.toString(result));
	}
    }

    public static void printf(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	Object[] o = new Object[args.length - 1];
	System.arraycopy(args, 1, o, 0, args.length - 1);
	System.out.printf((String) args[0], o);
    }

    public static Object reduce(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	NativeJavaObject njo = (NativeJavaObject) args[0];
	Function f = (Function) args[1];
	Collection list = (Collection) njo.unwrap();
	int i = 0;
	Object or = null;
	for (Object o : list) {
	    if (i==1) { or = o; continue;}
	    Object[] argx = { or, o, i, njo };
	    or = f.call(cx, thisObj, f, argx);
	    i++;
	}
	return or;
    }

    public static Object filter(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	NativeArray array = (NativeArray) cx.newArray(thisObj, 0);
	NativeJavaObject njo = (NativeJavaObject) args[0];
	Function f = (Function) args[1];
	Collection list = (Collection) njo.unwrap();
	int i = 0;
	for (Object o : list) {
	    Object[] argx = { o, i, njo };
	    Object r = f.call(cx, thisObj, f, argx);
	    if (r != null && r.getClass() != Undefined.class && !r.equals(false)) {
		array.put((int) array.getLength(), array, o);
	    }
	}

	return array;
    }

    public static Object find(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	NativeJavaObject njo = (NativeJavaObject) args[0];
	Function f = (Function) args[1];
	Collection list = (Collection) njo.unwrap();
	int i = 0;
	for (Object o : list) {
	    Object[] argx = { o, i, njo };
	    Object r = f.call(cx, thisObj, f, argx);
	    if (r != null && r.getClass() != Undefined.class && !r.equals(false)) {
		return Context.javaToJS(o, f);
	    }
	}
	return null;
    }

    public static Object whatis(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
	return args[0].getClass().getName();
    }

}
