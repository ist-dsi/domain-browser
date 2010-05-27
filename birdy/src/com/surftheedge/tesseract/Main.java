package com.surftheedge.tesseract;

import java.util.ArrayList;
import java.util.List;

import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.utils.Loader;

public class Main {
    public static void main(String[] args) throws Exception{
	System.out.println("Tesseract Shell for the Fenix Framework");
	List<String> runtimeEnv = new ArrayList<String>(); 
	runtimeEnv.add("runtime");
	boolean useJavaScript = true;
	String configPath = "etc/tesseract.json";
	
	for(int i=0; i<args.length; i++){
	    if (args[i].equals("-mjava")){
		useJavaScript = false;
	    }
	    if (args[i].equals("-c")){
		configPath = args[++i];
	    }
	}
	Loader.init(configPath);
	Stack.init();
	if (useJavaScript){
	    System.out.println("Using JavaScript interface");
	    new JSConsole(runtimeEnv).exec();
	}else{
	    System.out.println("Using MJava interface");
	    MJava.main();
	}
	
    }

}
