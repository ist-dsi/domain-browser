package com.surftheedge.tesseract;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jline.ClassNameCompletor;
import jline.Completor;
import jvstm.TransactionalCommand;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import pt.ist.fenixframework.pstm.Transaction;

import com.surftheedge.tesseract.utils.Loader;

public class SemanticIntrospection implements Completor {
    ArrayList<String> classNames;
    ArrayList<String> domainClasses;
    Context cx;
    Scriptable scope;

    private double score(String word, String abbreviation, int offset) {

	if (abbreviation.length() == 0)
	    return 0.9;
	if (abbreviation.length() > word.length())
	    return 0.0;

	for (int i = abbreviation.length(); i > 0; i--) {
	    String sub_abbreviation = abbreviation.substring(0, i);
	    int index = word.indexOf(sub_abbreviation);
	    if (index < 0)
		continue;
	    if (index + abbreviation.length() > word.length() + offset)
		continue;

	    String next_string = word.substring(index + sub_abbreviation.length());
	    String next_abbreviation = null;

	    if (i >= abbreviation.length())
		next_abbreviation = "";
	    else
		next_abbreviation = abbreviation.substring(i);

	    double remaining_score = score(next_string, next_abbreviation, offset + index);

	    if (remaining_score > 0) {
		double score = word.length() - next_string.length();

		if (index != 0) {
		    int j = 0;

		    int c = word.charAt(index - 1);
		    if (c == 32 || c == 9) {
			for (int k = (index - 2); k >= 0; k--) {
			    c = word.charAt(k);
			    score -= ((c == 32 || c == 9) ? 1 : 0.15);
			}

		    } else {
			score -= index;
		    }
		}

		score += remaining_score * next_string.length();
		score /= ((double) word.length());
		return score;
	    }
	}
	return 0.0;
    }

    public SemanticIntrospection(Context cx, Scriptable scope) throws IOException {
	classNames = new ArrayList<String>();
	domainClasses = new ArrayList<String>();
	this.cx = cx;
	this.scope = scope;
	String domainPackage = Loader.rootClass.substring(0, Loader.rootClass.lastIndexOf('.'));
	for (String i : classNames) {
	    classNames.add(i);
	    if (i.startsWith(domainPackage)) {
		domainClasses.add(i);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    public static String[] getClassNames() throws IOException {
	Set urls = new HashSet();

	for (ClassLoader loader = ClassNameCompletor.class.getClassLoader(); loader != null; loader = loader.getParent()) {
	    if (!(loader instanceof URLClassLoader)) {
		continue;
	    }

	    urls.addAll(Arrays.asList(((URLClassLoader) loader).getURLs()));
	}

	// Now add the URL that holds java.lang.String. This is because
	// some JVMs do not report the core classes jar in the list of
	// class loaders.
	Class[] systemClasses = new Class[] { String.class, javax.swing.JFrame.class };

	for (int i = 0; i < systemClasses.length; i++) {
	    URL classURL = systemClasses[i].getResource("/" + systemClasses[i].getName().replace('.', '/') + ".class");

	    if (classURL != null) {
		URLConnection uc = (URLConnection) classURL.openConnection();

		if (uc instanceof JarURLConnection) {
		    urls.add(((JarURLConnection) uc).getJarFileURL());
		}
	    }
	}

	Set classes = new HashSet();

	for (Iterator i = urls.iterator(); i.hasNext();) {
	    URL url = (URL) i.next();
	    File file = new File(url.getFile());

	    if (file.isDirectory()) {
		Set files = getClassFiles(file.getAbsolutePath(), new HashSet(), file, new int[] { 200 });
		classes.addAll(files);

		continue;
	    }

	    if ((file == null) || !file.isFile()) // TODO: handle directories
	    {
		continue;
	    }

	    JarFile jf;
	    try {
		jf = new JarFile(file);
	    } catch (IOException e) {
		continue;
	    }

	    for (Enumeration e = jf.entries(); e.hasMoreElements();) {
		JarEntry entry = (JarEntry) e.nextElement();

		if (entry == null) {
		    continue;
		}

		String name = entry.getName();

		if (!name.endsWith(".class")) // only use class files
		{
		    continue;
		}

		classes.add(name);
	    }
	}

	// now filter classes by changing "/" to "." and trimming the
	// trailing ".class"
	Set classNames = new TreeSet();

	for (Iterator i = classes.iterator(); i.hasNext();) {
	    String name = (String) i.next();
	    classNames.add(name.replace('/', '.').substring(0, name.length() - 6));
	}

	return (String[]) classNames.toArray(new String[classNames.size()]);
    }

    @SuppressWarnings("unchecked")
    private static Set getClassFiles(String root, Set holder, File directory, int[] maxDirectories) {
	// we have passed the maximum number of directories to scan
	if (maxDirectories[0]-- < 0) {
	    return holder;
	}

	File[] files = directory.listFiles();

	for (int i = 0; (files != null) && (i < files.length); i++) {
	    String name = files[i].getAbsolutePath();

	    if (!(name.startsWith(root))) {
		continue;
	    } else if (files[i].isDirectory()) {
		getClassFiles(root, holder, files[i], maxDirectories);
	    } else if (files[i].getName().endsWith(".class")) {
		holder.add(files[i].getAbsolutePath().substring(root.length() + 1));
	    }
	}

	return holder;
    }

    @SuppressWarnings("unchecked")
    public int complete(final String buffer, int cursor, final List candidates) {
	if (buffer.trim().equals("") || buffer.equals(".")) {
	    return 0;
	}
	String sect = buffer.substring(0, cursor);
	final String rest = buffer.substring(cursor);
	int split = reverseParser(sect);
	String element = sect.substring(sect.length() - split);
	String begin = sect.substring(0, sect.length() - split);
	int sep = element.lastIndexOf('.');
	final String searchPart;
	final String sugestion;
	final ArrayList<String> values = new ArrayList<String>();
	if (sep == -1) {
	    sugestion = element;
	    searchPart = "";
	    Transaction.withTransaction(true, new TransactionalCommand() {
		public void doIt() {
		    for (Object k : scope.getIds()) {
			String s = cx.toString(k);
			values.add(s);
		    }
		    if (scope.getParentScope() != null) {
			for (Object k : scope.getParentScope().getIds()) {
			    String s = cx.toString(k);
			    values.add(s);
			}
		    }
		    if (scope.getPrototype() != null) {
			for (Object k : scope.getPrototype().getIds()) {
			    String s = cx.toString(k);
			    values.add(s);
			}
		    }

		}
	    });
	} else if (element.charAt(element.length() - 1) == '.') {
	    searchPart = element.substring(0, sep);
	    sugestion = "";
	    Transaction.withTransaction(true, new TransactionalCommand() {
		public void doIt() {
		    Object o = cx.evaluateString(scope, searchPart, "<auto-complete>", 0, null);
		    for (Object k : ((Scriptable) o).getIds()) {
			String s = cx.toString(k);
			values.add(s);
		    }
		    if (((Scriptable) o).getPrototype() != null) {
			for (Object k : ((Scriptable) o).getPrototype().getIds()) {
			    String s = cx.toString(k);
			    values.add(s);
			}
		    }
		}
	    });
	} else {
	    searchPart = element.substring(0, sep);
	    sugestion = element.substring(sep + 1);
	    Transaction.withTransaction(true, new TransactionalCommand() {
		public void doIt() {
		    Object o = cx.evaluateString(scope, searchPart, "<auto-complete>", 0, null);
		    for (Object k : ((Scriptable) o).getIds()) {
			String s = cx.toString(k);
			values.add(s);
		    }
		    if (((Scriptable) o).getPrototype() != null) {
			for (Object k : ((Scriptable) o).getPrototype().getIds()) {
			    String s = cx.toString(k);
			    values.add(s);
			}
		    }
		}
	    });
	}

	for (String s : values) {
	    if (!s.contains("$")) {
		if (s.equals(sugestion)) {
		    candidates.clear();
		    if (sep == -1) {
			candidates.add(begin + s);
		    } else {
			candidates.add(begin + searchPart + "." + s);
		    }
		    break;
		} else {
		    double d = score(s, sugestion, 0);
		    if (d != 0.0) {
			if (sep == -1) {
			    candidates.add(begin + s);
			} else {
			    candidates.add(begin + searchPart + "." + s);
			}
		    }
		}
	    }
	}
	return 0;
    }

    private int reverseParser(String sect) {
	int size = 0;
	int paren = 0;
	for (int i = sect.length() - 1; i >= 0; i--) {
	    char c = sect.charAt(i);
	    if (c == ')' || c == '}' || c == '[') {
		paren++;
		size++;
	    } else if (c == '(' || c == '{' || c == ']') {
		if (paren == 0) {
		    break;
		} else {
		    paren--;
		    size++;
		}
	    } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '$' || c == '.') {
		size++;
	    } else {
		break;
	    }
	}
	return size;
    }
}
