package pt.ist.utl.birdy.client.controls;

import pt.ist.utl.birdy.client.Icon;
import pt.ist.utl.birdy.client.controls.strategy.Strategy;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class FluxWindow extends Window {

    CanvasWorkbench workplace = new CanvasWorkbench();

    Window toolbox = new Window();

    private void fillToolbox(Layout l) {
        for (Strategy s : Strategy.getStrategies()) {
            l.addMember(new DetailedMenuItem(workplace, s.getName(), s.getDetails(), s.getIcon(), s));
        }
    }

    public FluxWindow() {
        super();
        setTitle("Fluxo");
        setWidth(800);
        setHeight(600);
        setShowMaximizeButton(true);
        setCanDragReposition(true);
        setCanDragResize(true);
        VLayout layout = new VLayout();
        layout.setWidth100();
        layout.setHeight100();
        setKeepInParentRect(true);
        ToolStrip toolstrip = new ToolStrip();
        toolstrip.setWidth100();

        ToolStripButton toolBoxButton = new ToolStripButton();
        toolBoxButton.setIcon(Icon.use("block"));
        toolBoxButton.setTitle("ToolBox");
        toolBoxButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (toolbox.isVisible()) {
                    toolbox.hide();
                } else {
                    toolbox.show();
                }

            }
        });

        toolstrip.addSeparator();
        toolstrip.addMember(toolBoxButton);

        toolstrip.addSeparator();

        ToolStripButton play = new ToolStripButton();
        play.setIcon(Icon.use("control"));
        play.setTitle("Executar");
        toolstrip.addMember(play);

        layout.addMember(toolstrip);

        toolbox.setTitle("ToolBox");
        toolbox.setHeight(400);
        toolbox.setWidth(200);
        toolbox.setTop(25);
        toolbox.setLeft(25);
        toolbox.setKeepInParentRect(true);
        toolbox.setIsModal(true);

        VLayout widgetList = new VLayout();
        widgetList.setWidth100();
        widgetList.setHeight100();
        widgetList.setAlign(VerticalAlignment.TOP);
        fillToolbox(widgetList);
        toolbox.addItem(widgetList);
        workplace.setBackgroundImage("/birdy/resources/background.png");
        layout.addMember(workplace);
        workplace.addChild(toolbox);
        addItem(layout);

        addResizedHandler(new ResizedHandler() {
            public void onResized(ResizedEvent event) {
                workplace.setHeight100();
                workplace.setWidth100();

            }
        });

        centerInPage();
    }
}
