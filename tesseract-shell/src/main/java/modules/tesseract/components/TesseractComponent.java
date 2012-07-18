/*
 * @(#)TesseractComponent.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Artur Ventura
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Tesseract Shell Module.
 *
 *   The Tesseract Shell Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Tesseract Shell Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Tesseract Shell Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
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
import pt.ist.bennu.core.util.Base64;

import org.mozilla.javascript.Context;
import org.vaadin.console.Console;
import org.vaadin.console.Console.Command;
import org.vaadin.console.Console.Handler;

import pt.ist.fenixframework.FenixFrameworkInitializer;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;

import com.surftheedge.tesseract.JSConsole;
import com.surftheedge.tesseract.config.Config;
import com.vaadin.ui.CustomComponent;

@EmbeddedComponent(path = { "tesseract" })
/**
 * 
 * @author Artur Ventura
 * @author Pedro Santos
 * @author Sérgio Silva
 * @author Luis Cruz
 * 
 */
public class TesseractComponent extends CustomComponent implements EmbeddedComponentContainer {

    public class EmbebedTesseract extends JSConsole {
	private final Console console;
	private PrintStream stream;

	public String decode(String string) throws IOException {
	    byte[] decodedBytes = Base64.decode(string);
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

	    getContext().evaluateString(getScope(), "importClass(Packages." + "pt.ist.bennu.core.domain.MyOrg" + ");", "<boot>", 0, null);
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

	@Override
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
    public boolean isAllowedToOpen(Map<String, String> arguments) {
	return true;
    }

    @Override
    public void setArguments(Map<String, String> arg0) {
    }

}
