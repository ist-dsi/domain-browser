package com.surftheedge.tesseract.reader;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.reader.lex.EndOfFileException;
import com.surftheedge.tesseract.reader.lex.LexicalReader;

public class Reader {
    private static SudoParser parser = new SudoParser();
    private static LexicalReader lex = new LexicalReader();

    public static LexicalReader getLex() {
	return lex;
    }

    public static void setLex(LexicalReader lex) {
	Reader.lex = lex;
    }

    static public Expression read() throws EndOfFileException {
	String line = "";
	try {
	    while (true) {
		System.out.print("tes> ");
		line = lex.read(); 
		if (line.length() != 0) {
		    return getParser().parse(line);
		}
	    }
	}

	catch (java.io.IOException e) {
	    System.out.println(e);
	} catch (NumberFormatException e) {
	    System.out.println(e);
	}
	return null;
    }

    static public List<Expression> readMany(String expression) {
	LexicalReader tempLex = getLex();
	ArrayList<Expression> result = new ArrayList<Expression>();
	setLex(new LexicalReader(new ByteArrayInputStream(expression.getBytes())));
	try {
	    while (true) {
		String line = lex.read();
		if (line.length() != 0) {
		    result.add(getParser().parse(line));
		}
	    }
	} catch (IOException e) {
	    System.out.println(e);
	} catch (EndOfFileException e) {
	}
	setLex(tempLex);
	return result;

    }

    public static SudoParser getParser() {
	return parser;
    }
}
