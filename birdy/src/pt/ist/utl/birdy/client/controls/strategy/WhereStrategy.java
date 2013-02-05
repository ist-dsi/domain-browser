package pt.ist.utl.birdy.client.controls.strategy;

import java.util.HashMap;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import pt.ist.utl.birdy.client.Icon;
import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;

public class WhereStrategy extends Strategy {

    private class WhereNugget extends Nugget {
        TextArea ta = new TextArea();

        public WhereNugget(CanvasWorkbench cw) {
            super(cw, "Onde", "#5CFF00", 1, 1, WhereStrategy.this);
        }

        @Override
        public void createOptions(VerticalPanel vp) {
            HTML h = new HTML("<span style='font-size:12px'>Codigo JavaScript:</span>");
            vp.add(h);
            vp.add(ta);
        }

        @Override
        public void saveOptions() {
            // TODO Auto-generated method stub

        }

        @Override
        public HashMap<String, String> compiled() {
            HashMap<String, String> h = new HashMap<String, String>();
            h.put("type", "where");
            h.put("value", ta.getText());
            return h;
        }

    }

    @Override
    public Nugget createObject(CanvasWorkbench cw) {
        return new WhereNugget(cw);
    }

    @Override
    public String getDetails() {
        return "Este modulo vai filtrar os elementos da lista de acordo com uma expressão.";
    }

    @Override
    public String getIcon() {
        return Icon.use("layer", "shape-polygon");
    }

    @Override
    public String getName() {
        return "Onde";
    }

}
