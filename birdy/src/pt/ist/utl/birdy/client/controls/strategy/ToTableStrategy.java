package pt.ist.utl.birdy.client.controls.strategy;

import java.util.HashMap;
import java.util.Stack;

import pt.ist.utl.birdy.client.GreetingService;
import pt.ist.utl.birdy.client.GreetingServiceAsync;
import pt.ist.utl.birdy.client.Icon;
import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;
import pt.ist.utl.birdy.client.controls.wiring.Terminal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToTableStrategy extends Strategy {
    final static GreetingServiceAsync a = GWT.create(GreetingService.class);
    private class ToTableNugget extends Nugget{

	public ToTableNugget(CanvasWorkbench cw) {
	    super(cw, "Gerar Tabela", "#FF18DF",1,0,ToTableStrategy.this);
	}
	
	@Override
	public void doubleClick() {
	    
	    Stack<HashMap<String, String>> stack = new Stack<HashMap<String,String>>();
	    stack.add(this.compiled());
	    Terminal t = null;
	    Nugget n = this;
	    do{
		t = n.getInputs().get(0);
		n = t.getLink().getEnd().getFather();
		stack.add(n.compiled());
	    }while(n.getInputs().size() != 0);
	    a.execute(stack, new AsyncCallback<String[][]>() {
	        
	        @Override
	        public void onSuccess(String[][] result) {

	        }
	        
	        @Override
	        public void onFailure(Throwable caught) {
	    	// TODO Auto-generated method stub
	    	
	        }
	    });
	}
	
	@Override
	public void createOptions(VerticalPanel vp) {
	    
	}

	@Override
	public void saveOptions() {
	    
	}
	
	@Override
	   public HashMap<String, String> compiled() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("type", "table");
		h.put("value", "[\"userUId\"]");
		return h;
	    }
    }
    
    @Override
    public Nugget createObject(CanvasWorkbench cw) {
	return new ToTableNugget(cw);
    }

    @Override
    public String getDetails() {
	return "Este modulo gera uma tabela com o conteudo dado com input.";
    }

    @Override
    public String getIcon() {
	return Icon.use("table");
    }

    @Override
    public String getName() {
	return "Tabela";
    }
}
