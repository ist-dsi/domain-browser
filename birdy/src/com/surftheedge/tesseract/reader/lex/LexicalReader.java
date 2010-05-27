package com.surftheedge.tesseract.reader.lex;

import java.io.IOException;
import java.io.InputStream;

public class LexicalReader {
    private InputStream stream;

    public InputStream getStream() {
	return stream;
    }

    public void setStream(InputStream stream) {
	this.stream = stream;
    }

    public LexicalReader() {
	setStream(System.in);
    }

    public LexicalReader(InputStream stream) {
	setStream(stream);
    }

    private char readChar() throws IOException, EndOfFileException {
	int check = getStream().read();
	if (check == -1) {
	    throw new EndOfFileException();
	}
	return (char) check;
    }

    private void readString(StringBuffer buffer) throws IOException, EndOfFileException {
	while (true) {
	    char c = (char) readChar();
	    buffer.append(c);
	    switch (c) {
	    case '"':
		return;
	    case '\\':
		buffer.append((char) readChar());
		break;
	    }
	}
    }

    private void readChar(StringBuffer buffer) throws IOException, EndOfFileException {
	while (true) {
	    char c = (char) readChar();
	    buffer.append(c);
	    switch (c) {
	    case '\'':
		return;
	    case '\\':
		buffer.append((char) readChar());
		break;
	    }
	}
    }

    private String readBlock() throws IOException, EndOfFileException {
	StringBuffer buffer = new StringBuffer();
	int depthLevel = 0;
	boolean curly = false;
	while (true) {
	    char c = readChar(); 
	    buffer.append(c);
	    switch (c) {
	    case '{':
	    case '(':
		depthLevel++;
		curly = true;
		break;
	    case '}':
	    case ')':
		depthLevel--;
		break;
	    case '\n':
		
		if (!curly || (curly && depthLevel == 0)) {
		    return buffer.toString();
		}
		break;

	    case '"':
		readString(buffer);
		break;
	    case '\'':
		readChar(buffer);
		break;
	    }
	}
    }

    public String read() throws IOException, EndOfFileException {
	return readBlock().trim();
    }
}
