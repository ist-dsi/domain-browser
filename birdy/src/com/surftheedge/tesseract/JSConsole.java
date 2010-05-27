package com.surftheedge.tesseract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;

import jline.ConsoleReader;
import jline.SimpleCompletor;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Scriptable;

import pt.ist.fenixframework.pstm.Transaction;

import com.surftheedge.tesseract.jsbridge.EvaluationTransaction;
import com.surftheedge.tesseract.jsbridge.TopLevelContext;
import com.surftheedge.tesseract.utils.Loader;

public class JSConsole {
    private List<String> importDirectory;
    private Context cx;
    private final String[] prompts = { "tes> ", "     " };
    private Scriptable scope;
    private PrintStream ps = new PrintStream(System.out);
    private HashMap<String, File> loadedFiles = new HashMap<String, File>();
    public static JSConsole instance;
    public ConsoleReader reader;

    public JSConsole(List<String> importDirectory) throws IOException {
	this.importDirectory = importDirectory;
	cx = Context.enter();
	scope = new TopLevelContext(cx);
	JSConsole.instance = this;
	if (System.console() != null) {
	    reader = new ConsoleReader();
	    reader.addCompletor(new SemanticIntrospection(cx,scope));
	}
	cx.evaluateString(scope, "importClass(Packages." + Loader.rootClass + ");", "<boot>", 0, null);
    }

    public String reader(BufferedReader in) {
	String source = "";
	boolean wrote = false;
	while (true) {
	    String newline;
	    try {
		if (System.console() != null) {
		    newline = reader.readLine(wrote ?  "" : prompts[0]);
		    wrote = true;
		} else {
		    newline = in.readLine();
		}
	    } catch (IOException ioe) {
		ps.println(ioe.toString());
		break;
	    }
	    if (newline == null) {
		return null;
	    }
	    source = source + newline + "\n";
	    if (cx.stringIsCompilableUnit(source))
		return source;
	}
	return null;
    }

    private String slurp(Reader in) throws IOException {
	StringBuffer out = new StringBuffer();
	char[] b = new char[4096];
	for (int n; (n = in.read(b)) != -1;) {
	    out.append(new String(b, 0, n));
	}
	return out.toString();
    }

    public void loopFile(BufferedReader in, String file) {
	try {
	    while (true) {
		String source = slurp(in);
		try {
		    cx.evaluateString(scope, source, file, 1, null);
		    return;
		} catch (EvaluatorException e) {
		    System.out.println("Error while loading runtime: " + e.getMessage());
		    System.exit(-1);
		}
	    }
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
    }

    public void loopSource(BufferedReader in) {
	while (true) {
	    String source = reader(in);
	    if (source == null) {
		break;
	    }

	    Transaction.withTransaction(true, new EvaluationTransaction(cx, scope, source));
	}
	if (System.console() != null) {
	    ps.println();
	}
    }

    public void loadDirectory(File f) {
	String[] children = f.list();
	if (children == null) {
	} else {
	    for (int i = 0; i < children.length; i++) {
		File child = new File(f.getPath() + "/" + children[i]);
		if (child.isDirectory()) {
		    loadDirectory(child);
		} else if (child.isFile() && !child.isHidden()) {
		    if (children[i].endsWith(".js") && !loadedFiles.containsKey(f.getPath() + "/" + children[i])) {
			loadedFiles.put(f.getPath() + "/" + children[i], child);
		    }
		}
	    }
	}
    }

    public void loadResources() {
	for (String r : importDirectory) {
	    loadDirectory(new File(r));
	}
	for (java.util.Map.Entry<String, File> f : loadedFiles.entrySet()) {
	    File file = f.getValue();
	    try {
		FileReader fir = new FileReader(file);
		BufferedReader br = new BufferedReader(fir);
		System.out.println("loaded: " + f.getKey());
		loopFile(br, f.getKey());
	    } catch (FileNotFoundException e) {
		System.out.println("file not found: " + f.getKey());
	    }
	}
    }

    public void exec() {
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	loadResources();
	loopSource(in);
    }
}
