package modules.tesseract.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import modules.tesseract.TesseractRuntime;

import org.mozilla.javascript.Context;
import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;
import org.vaadin.console.Console.Handler;

import pt.ist.fenixframework.FenixFrameworkInitializer;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import sun.misc.BASE64Decoder;

import com.surftheedge.tesseract.JSConsole;
import com.surftheedge.tesseract.config.Config;
import com.vaadin.ui.CustomComponent;

@EmbeddedComponent(path = { "tesseract" })
public class TesseractComponent extends CustomComponent implements EmbeddedComponentContainer {

    public class EmbebedTesseract extends JSConsole {
	private Console console;
	private PrintStream stream;

	public String decode(String string) throws IOException {
	    BASE64Decoder decoder = new BASE64Decoder();
	    byte[] decodedBytes = decoder.decodeBuffer(string);
	    return new String(decodedBytes);
	}

	public EmbebedTesseract(Console console) throws IOException {
	    super(new ArrayList<String>(), null, new String[] {});
	    Config.set("disableTransactions", true, getContext(), getScope());
	    this.console = console;

	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.stringsFix))), "stringsFix");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.linq))), "linq");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.range))), "range");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.writeMode))), "writeMode");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.table))), "table");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.fenixFix))), "fenixFix");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.prompt))), "prompt");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.arrayFixs))), "arrayFixs");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.autoComplete))), "autoComplete");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.fenixFramework))), "fenixFramework");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.color))), "color");
	    loopFile(new BufferedReader(new StringReader(decode(TesseractRuntime.time))), "time");
	    Context.enter();

	    getContext().evaluateString(getScope(), "importClass(Packages." + "myorg.domain.MyOrg" + ");", "<boot>", 0, null);
	    Context.exit();
	}

	@Override
	public PrintStream out() {
	    return console.getPrintStream();
	}

	@Override
	public PrintStream err() {
	    return console.getPrintStream();
	}

	public void finalize() {

	}
    }

    private static File getApplicationDir() {
	final URL url = FenixFrameworkInitializer.class.getResource("/configuration.properties");
	try {
	    return new File(url.toURI()).getParentFile();
	} catch (final URISyntaxException e) {
	    throw new Error(e);
	}
    }

    public TesseractComponent() throws IOException {
	Console console = new Console();
	console.setHeight("500px");
	final EmbebedTesseract et = new EmbebedTesseract(console);
	console.print("Tesseract Shell for the Fenix Framework\n");
	console.print("\n");
	console.prompt();
	console.setHandler(new Handler() {

	    @Override
	    public void inputReceived(Console console, String lastInput) {
		et.doIt(lastInput);
		console.prompt();
	    }

	    @Override
	    public void handleException(Console console, Exception e, Command cmd, String[] argv) {
		et.out().print(e.getMessage());
	    }

	    @Override
	    public Set<String> getSuggestions(Console console, String lastInput) {
		return new HashSet<String>();
	    }

	    @Override
	    public void commandNotFound(Console console, String[] argv) {
	    }
	});
	setCompositionRoot(console);
    }

    @Override
    public void setArguments(Map<String, String> arg0) {
    }

}
