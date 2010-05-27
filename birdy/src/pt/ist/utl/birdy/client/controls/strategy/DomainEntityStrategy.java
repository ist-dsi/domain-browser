package pt.ist.utl.birdy.client.controls.strategy;

import java.util.HashMap;

import pt.ist.utl.birdy.client.Icon;
import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;

import com.google.gwt.resources.client.CssResource.ClassName;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DomainEntityStrategy extends Strategy {

    private class DomainEntityNugget extends Nugget{
	TextBox className = new TextBox(); 
	public DomainEntityNugget(CanvasWorkbench cw) {
	    super(cw, "Entidade de\nDominio", "white",0,1,DomainEntityStrategy.this);
	}

	@Override
	public void createOptions(VerticalPanel vp) {
	    HTML h = new HTML("<span style='font-size:12px'>Entidade de dominio:</span>");
	    vp.add(h);
	    vp.add(className);
	}

	@Override
	public void saveOptions() {
	    setProperty("className", className.getText());
	}
	
	@Override
	   public HashMap<String, String> compiled() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("type", "entity");
		h.put("value", className.getText());
		return h;
	    }

	
    }
    @Override
    public DomainEntityNugget createObject(CanvasWorkbench cw) {
	return new DomainEntityNugget(cw);
    }

    @Override
    public String getDetails() {
	return "Este elemento vai recolher todos as instancias de uma entidades do dominio.";
    }

    @Override
    public String getIcon() {
	return Icon.use("database");
    }

    @Override
    public String getName() {
	return "Entidade de Dominio";
    }


}
