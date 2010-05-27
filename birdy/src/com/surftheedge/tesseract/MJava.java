package com.surftheedge.tesseract;

import com.surftheedge.tesseract.evaluator.Evaluator;
import com.surftheedge.tesseract.expression.Expression;
import com.surftheedge.tesseract.reader.Reader;
import com.surftheedge.tesseract.reader.lex.EndOfFileException;
import com.surftheedge.tesseract.stack.Stack;
import com.surftheedge.tesseract.utils.ExceptionHandler;
import com.surftheedge.tesseract.utils.Loader;
import com.surftheedge.tesseract.utils.Printer;

import javassist.CannotCompileException;
import jvstm.TransactionalCommand;
import pt.ist.fenixframework.pstm.Transaction;

public class MJava {
    public static void main() throws Exception {
	while (true) {
	    Transaction.withTransaction(true, new TransactionalCommand() {
		public void doIt() {
		    Expression expression;
		    try {
			expression = Reader.read();
			if (expression != null) {
			    Object result = Evaluator.eval(expression);
			    Printer.print(com.surftheedge.tesseract.utils.InternalRepresentation.convert(result), expression);
			}
		    } catch (CannotCompileException e) {
			ExceptionHandler.failedCompilation(e);
		    } catch (EndOfFileException e) {
			System.exit(0);
		    } catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		    }
		}
	    });
	}
    }

}
