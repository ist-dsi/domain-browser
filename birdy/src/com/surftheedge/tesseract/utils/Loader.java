package com.surftheedge.tesseract.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.ojb.broker.PersistenceBrokerException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.UniqueTag;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;

public class Loader {
    public static String rootClass;

    public static String readFile(String path) {
        File file = new File(path);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        String s = "";

        try {
            fis = new FileInputStream(file);

            // Here BufferedInputStream is added for fast reading.
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            // dis.available() returns 0 if the file does not have more lines.
            while (dis.available() != 0) {

                // this statement reads the line from the file and print it to
                // the console.
                s += dis.readLine() + "\n";
            }

            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static Scriptable getConfig(String source) {
        Context cx = Context.enter();
        Scriptable scope = cx.initStandardObjects();
        return (Scriptable) cx.evaluateString(scope, "var x = " + source + "; x", "<config>", 0, null);
    }

    public static void init(String path) {
        final Scriptable cfg = getConfig(readFile(path));
        final Class r00tClass;
        Config config = null;
        Loader.rootClass = (String) cfg.get("rootClass", cfg);
        try {
            r00tClass = Class.forName(Loader.rootClass);
            config = new Config() {
                {
                    NativeArray na = (NativeArray) cfg.get("domainModelPaths", cfg);
                    int length = (int) na.getLength();
                    String[] array = new String[length];
                    for (int i = 0; i < length; i++) {
                        array[i] = (String) na.get(i, na);
                    }
                    domainModelPaths = array;
                    dbAlias = (String) cfg.get("dbAlias", cfg);
                    dbUsername = (String) cfg.get("dbUsername", cfg);
                    dbPassword = (String) cfg.get("dbPassword", cfg);
                    rootClass = r00tClass;
                    updateRepositoryStructureIfNeeded = (Boolean) cfg.get("updateRepositoryStructureIfNeeded", cfg);
                }
            };
        } catch (ClassNotFoundException e) {
            System.out.println("Class " + cfg.get("rootClass", cfg) + " wasn't in classPath.");
            System.out.println("Make sure that every class required is in the classpath.");
            System.exit(-1);
        }
        try {
            FenixFramework.initialize(config);
        } catch (Error e) {
            System.out.println("Initialization failed. Probably you don't have the Persistency Backend running. ");
            System.exit(-1);
        }
    }
}
