package pt.ist.utl.birdy.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;

import pt.ist.fenixframework.pstm.Transaction;

import com.surftheedge.tesseract.jsbridge.EvaluationTransaction;
import com.surftheedge.tesseract.jsbridge.TopLevelContext;
import com.surftheedge.tesseract.utils.Loader;

public class QueryExecutionService {
    private static Context cx;
    private static TopLevelContext scope;

    private static List<String> importDirectory = new ArrayList<String>();
    private static HashMap<String, File> loadedFiles = new HashMap<String, File>();

    public static void loadDirectory(File f) {
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

    public static void loadResources() {
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

    private static String slurp(Reader in) throws IOException {
	StringBuffer out = new StringBuffer();
	char[] b = new char[4096];
	for (int n; (n = in.read(b)) != -1;) {
	    out.append(new String(b, 0, n));
	}
	return out.toString();
    }

    public static void loopFile(BufferedReader in, String file) {
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

    public static Object query(String query) {
	cx = Context.enter();
	EvaluationTransaction et = new EvaluationTransaction(cx, scope, query);
	Transaction.withTransaction(true, et);
	Context.exit();
	return et.getResult();
    }

    static {
	{
	    try {
		importDirectory.add("/home/nurv/tes/mjava/runtime");
		System.out.println("Tesseract Shell");
		Loader.init("/home/nurv/tes/mjava/etc/tesseract.json");
		cx = Context.enter();
		scope = new TopLevelContext(cx);
		cx.evaluateString(scope, "importClass(Packages." + Loader.rootClass + ");", "<boot>", 0, null);
		loadResources();
		System.out.println("Loaded");
		Context.exit();
	    } catch (Throwable e) {
		e.printStackTrace();
	    }
	}
    }
}
