package pt.ist.utl.birdy.client.controls.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.Window;

public abstract class Strategy {
    
    private static List<Strategy> strategies = new ArrayList<Strategy>();
    static{
	getStrategies().add(new DomainEntityStrategy());
	getStrategies().add(new WhereStrategy());
	getStrategies().add(new LimitStrategy());
	getStrategies().add(new ToTableStrategy());
    }
    
    abstract public String getName();
    abstract public String getDetails();
    abstract public String getIcon();
    abstract public Nugget createObject(CanvasWorkbench cw);
    public static List<Strategy> getStrategies() {
	return strategies;
    }
}