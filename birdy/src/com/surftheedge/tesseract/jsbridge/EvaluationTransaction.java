package com.surftheedge.tesseract.jsbridge;

import jvstm.TransactionalCommand;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.ToolErrorReporter;

public class EvaluationTransaction implements TransactionalCommand {
    String source;
    Context cx;
    Scriptable scope;
    private Object result;

    public EvaluationTransaction(Context cx, Scriptable scope, String source) {
        this.source = source;
        this.cx = cx;
        this.scope = scope;
    }

    public void doIt() {
        setResult(cx.evaluateString(scope, source, "<cmd>", 1, null));
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

}
