package pt.ist.utl.birdy.client.controls.wiring;

import pt.ist.utl.birdy.client.controls.DetailedMenuItem;
import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.svg.SVGCanvas;

import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;

public class CanvasWorkbench extends SVGCanvas {
    
    public CanvasWorkbench() {
	super();
	setHeight100();
	setWidth100();
	setCanAcceptDrop(true);
	addDropHandler(new DropHandler() {
	    @Override
	    public void onDrop(DropEvent event) {
		DetailedMenuItem d = Mouse.getStrategy(); 
		Mouse.setStrategy(null);
		Nugget n = d.getStrategy().createObject(CanvasWorkbench.this);
		n.createWindow(CanvasWorkbench.this);
		n.moveTo(d.getAbsoluteLeft() - CanvasWorkbench.this.getAbsoluteLeft(),d.getAbsoluteTop() - CanvasWorkbench.this.getAbsoluteTop());
	    }
	});
    }
}
