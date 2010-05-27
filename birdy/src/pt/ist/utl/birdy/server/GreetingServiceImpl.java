package pt.ist.utl.birdy.server;

import java.util.HashMap;
import java.util.Stack;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;

import pt.ist.utl.birdy.client.GreetingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    public String greetServer(String input) {
	return "";
    }

    @Override
    public String[][] execute(Stack<HashMap<String, String>> name) {
	String s = "";
	while (name.size() != 0){
	    HashMap<String, String> element = name.pop();
	    if (element.get("type").equals("entity")){
		s = "$(" + element.get("value") + ").";
	    }else if (element.get("type").equals("where")){
		s +=  "where(" + element.get("value") + ").";
	    }else if (element.get("type").equals("table")){
		s +=  "table(" + element.get("value") + ");";
	    }else if (element.get("type").equals("limit")){
		s +=  "limit(" + element.get("value") + ").";
	    }
	}
	
	
	/*NativeArray array = (NativeArray) QueryExecutionService.query(s);
	String[][] result = new String[(int)((NativeArray) array.get(1,null)).getLength()][(int)((NativeArray) array.get(0,null)).getLength()];
	for (int i = 0; i < ((NativeArray) array.get(0,null)).getLength(); i++) {
	    result[0][i] = Context.toString(((NativeArray) array.get(0,null)).get(i, null));
	    System.out.println(">" + result[0][i]);
	}
	for (int i = 0; i < ((NativeArray) array.get(1,null)).getLength(); i++) {
	    for (int j = 0; i < ((NativeArray) array.get(1,null)).getLength(); i++) {
	    result[i][j] = Context.toString(((NativeArray) ((NativeArray) array.get(1,null)).get(i, null)).get(j, null));
	    System.out.println("$" + result[i][j]);
	    }
	}*/
	 QueryExecutionService.query(s);
	return null;
    }
}
