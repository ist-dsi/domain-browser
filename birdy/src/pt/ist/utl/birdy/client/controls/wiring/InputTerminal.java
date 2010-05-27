package pt.ist.utl.birdy.client.controls.wiring;

import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.svg.SVGCanvas;

public class InputTerminal extends Terminal{

    public InputTerminal(SVGCanvas sc, Nugget father, int x, int y) {
	super(sc, father, x, y);
    }

    @Override
    void removeTerminal(Wire w) {
	w.setStart(null);
    }

    @Override
    void setTerminal(Wire w) {
	w.setStart(this);
    }

    @Override
    boolean isPlaceable(Wire w) {
	return w.getStart() == null;
    }
    
}
