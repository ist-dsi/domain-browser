package pt.ist.utl.birdy.client.controls;

import pt.ist.utl.birdy.client.Icon;
import pt.ist.utl.birdy.client.controls.strategy.Strategy;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;
import pt.ist.utl.birdy.client.controls.wiring.Mouse;
import pt.ist.utl.birdy.client.utils.Log;

import com.google.gwt.user.client.ui.Image;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.DragRepositionStartEvent;
import com.smartgwt.client.widgets.events.DragRepositionStartHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class DetailedMenuItem extends VLayout{
    private Strategy strategy;
    public DetailedMenuItem(final CanvasWorkbench cw,String name,String content, String icon,Strategy s) {
	Label title = new Label("<b>" + name +"</b>");
	setAlign(VerticalAlignment.TOP);
	title.setWidth100();
	HLayout header = new HLayout();
	header.setAlign(Alignment.LEFT);
	Image img = new Image(icon);
	img.setHeight("16px");
	img.setWidth("16px");
	HLayout space = new HLayout();
	space.setWidth(10);
	
	header.addMember(img);
	header.addMember(space);
	header.addMember(title);
	
	Label description = new Label(content);
	description.setHeight(10);
	setWidth100();
	setHeight(10);
	setPadding(5);
	addMember(header);
	title.setHeight(20);

	
        setCanDragReposition(true);
        setCanDrop(true);
	setDragAppearance(DragAppearance.TARGET);
	addMember(description);
	setBackgroundColor("#ffffff");
	this.setStrategy(s);
	addDragRepositionStartHandler(new DragRepositionStartHandler() {
	    
	    @Override
	    public void onDragRepositionStart(DragRepositionStartEvent event) {
		Mouse.setStrategy(DetailedMenuItem.this);
	    }
	});
    }
    public void setStrategy(Strategy strategy) {
	this.strategy = strategy;
    }
    public Strategy getStrategy() {
	return strategy;
    }
}
