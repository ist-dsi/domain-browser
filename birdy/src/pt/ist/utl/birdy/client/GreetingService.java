package pt.ist.utl.birdy.client;

import java.util.HashMap;
import java.util.Stack;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
    String greetServer(String name);

    String[][] execute(Stack<HashMap<String, String>> name);
}
