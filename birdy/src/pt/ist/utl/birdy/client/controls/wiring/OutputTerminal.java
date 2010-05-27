package pt.ist.utl.birdy.client.controls.wiring;

import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.svg.SVGCanvas;

public class OutputTerminal extends Terminal{

    public OutputTerminal(SVGCanvas sc, Nugget father, int x, int y) {
	super(sc, father, x, y);
    }

    @Override
    void removeTerminal(Wire w) {
	w.setEnd(null);
    }

    @Override
    void setTerminal(Wire w) {
	w.setEnd(this);
    }

    @Override
    boolean isPlaceable(Wire w) {
	return w.getEnd() == null;
    }
    
}
