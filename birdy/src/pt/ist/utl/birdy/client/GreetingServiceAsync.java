package pt.ist.utl.birdy.client;

import java.util.HashMap;
import java.util.Stack;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
  void greetServer(String input, AsyncCallback<String> callback);
  void execute(Stack<HashMap<String, String>> name,AsyncCallback<String[][]> callback);
}
