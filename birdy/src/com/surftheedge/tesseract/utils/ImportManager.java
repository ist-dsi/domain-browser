package com.surftheedge.tesseract.utils;

import java.util.ArrayList;
import java.util.List;

public class ImportManager {
    private static List<String> imports = new ArrayList<String>();

    static {
	imports.add("eu.ist.fears.server.domain");
    };
   
    public static List<String> getImports() {
	return imports;
    }

    public static void addImport(String name) {
	getImports().add(name);
    }

    public static void removeImport(String name) {
	getImports().remove(name);
    }

}
