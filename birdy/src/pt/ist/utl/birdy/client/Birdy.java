package pt.ist.utl.birdy.client;

import pt.ist.utl.birdy.client.controls.FluxWindow;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Birdy implements EntryPoint {
    
    Canvas drawingBoard = new Canvas();
    GreetingServiceAsync a = GWT.create(GreetingService.class);
    private void makeToolbak(ToolStrip toolbar) {
	toolbar.addSpacer(0);
	ToolStripButton newFlux = new ToolStripButton();
	newFlux.setTitle("Novo fluxo");
	newFlux.setIcon(Icon.use("blueprint","-plus"));
	newFlux.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		drawingBoard.addChild(new FluxWindow());
	    }
	});
	
	ToolStripButton showTerm = new ToolStripButton();
	showTerm.setTitle("Shell");
	showTerm.setIcon(Icon.use("terminal"));
	showTerm.addClickHandler(new ClickHandler() {
	    public void onClick(ClickEvent event) {
		a.greetServer("ok", new AsyncCallback<String>() {
		    
		    @Override
		    public void onSuccess(String result) {
			Window.alert("OK");
		    }
		    
		    @Override
		    public void onFailure(Throwable caught) {
			Window.alert(caught.toString());
		    }
		});
	    }
	});
	
	toolbar.addMember(newFlux);
	toolbar.addMember(showTerm);
	
    }
    
    public void onModuleLoad() {
	drawingBoard.setHeight100();
	drawingBoard.setBackgroundColor("#DDE1ED");
	
	
	VLayout layout = new VLayout();
	layout.setHeight100();
	layout.setWidth100();
	
	ToolStrip toolbar = new ToolStrip();
	toolbar.setWidth100();
	makeToolbak(toolbar);
	
	

	layout.addMember(toolbar);
	layout.addMember(drawingBoard);
	layout.draw();
    }
}