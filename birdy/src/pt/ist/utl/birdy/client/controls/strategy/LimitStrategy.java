package pt.ist.utl.birdy.client.controls.strategy;

import java.util.HashMap;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import pt.ist.utl.birdy.client.Icon;
import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;

public class LimitStrategy extends Strategy {

    private class LimitNugget extends Nugget {
        TextBox className = new TextBox();

        public LimitNugget(CanvasWorkbench cw) {
            super(cw, "Filtrar", "#5CFF00", 1, 1, LimitStrategy.this);
        }

        @Override
        public void createOptions(VerticalPanel vp) {
            HTML h = new HTML("<span style='font-size:12px'>Limite</span>");
            vp.add(h);
            vp.add(className);
        }

        @Override
        public void saveOptions() {
            // TODO Auto-generated method stub

        }

        @Override
        public HashMap<String, String> compiled() {
            HashMap<String, String> h = new HashMap<String, String>();
            h.put("type", "limit");
            h.put("value", className.getText());
            return h;
        }

    }

    @Override
    public Nugget createObject(CanvasWorkbench cw) {
        // TODO Auto-generated method stub
        return new LimitNugget(cw);
    }

    @Override
    public String getDetails() {
        return "Este elemento vai limitar o numero.";
    }

    @Override
    public String getIcon() {
        // TODO Auto-generated method stub
        return Icon.use("funnel");
    }

    @Override
    public String getName() {
        return "Limitar";
    }

}
