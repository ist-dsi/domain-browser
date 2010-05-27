package pt.ist.utl.birdy.client.controls.wiring;

import pt.ist.utl.birdy.client.controls.Nugget;
import pt.ist.utl.birdy.client.controls.svg.Circle;
import pt.ist.utl.birdy.client.controls.svg.SVGCanvas;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

public abstract class Terminal extends Circle {
    SVGCanvas canvas;
    private Nugget father;
    private Wire link;

    public Wire getLink() {
        return link;
    }

    public Terminal(final SVGCanvas sc, Nugget father, int x, int y) {
	super(sc, x, y, 5);
	this.father = father;
	canvas = sc;
	setStoke("#ffffff");
	setFill("#ffffff");
	setFillOpacity(0);
	setStrokeWidth(1);
	addMouseUpHandler(new MouseUpHandler() {
	    public void onMouseUp(MouseUpEvent event) {
		setStoke("#ffffff");
		setStrokeWidth(1);

		if (Mouse.getWire() != null) {
		    Wire w = Mouse.getWire();
		    Mouse.setWire(null);
		    if (isPlaceable(w)) {
			link = w;
			setTerminal(w);
			w.draw();
		    } else {
			w.remove();
		    }
		    w.hr1.removeHandler();
		    w.hr2.removeHandler();
		}
	    }
	});

	addMouseOverHandler(new MouseOverHandler() {
	    public void onMouseOver(MouseOverEvent event) {
		if (Mouse.getWire() != null) {
		    if (isPlaceable(Mouse.getWire())) {
			setStoke("yellow");
			setStrokeWidth(2);
		    } else {
			setStoke("red");
			setStrokeWidth(2);
		    }
		}
	    }
	});

	addMouseOutHandler(new MouseOutHandler() {
	    public void onMouseOut(MouseOutEvent event) {
		if (Mouse.getWire() != null) {
		    setStoke("#ffffff");
		    setStrokeWidth(1);
		}
	    }
	});

	addMouseDownHandler(new MouseDownHandler() {
	    Wire w;

	    public void onMouseDown(final MouseDownEvent eventStart) {
		if (link == null) {
		    w = new Wire(sc);
		    setTerminal(w);
		    link = w;
		    Mouse.setWire(w);
		} else {
		    removeTerminal(link);
		    Mouse.setWire(link);
		    w = link;
		    link = null;
		}

		w.hr1 = canvas.addMouseMoveHandler(new MouseMoveHandler() {
		    public void onMouseMove(final MouseMoveEvent event) {
			w.draw(event.getX() - 5, event.getY() - 3);
		    }
		});
		w.hr2 = canvas.addMouseUpHandler(new MouseUpHandler() {
		    public void onMouseUp(MouseUpEvent event) {
			Mouse.setWire(null);
			w.hr1.removeHandler();
			w.hr2.removeHandler();
			w.remove();
		    }
		});
	    }
	});

    }

    abstract boolean isPlaceable(Wire w);

    abstract void setTerminal(Wire w);

    abstract void removeTerminal(Wire w);

    public void setFather(Nugget father) {
	this.father = father;
    }

    public Nugget getFather() {
	return father;
    }

    public void draw() {
	if (link != null) {
	    link.draw();
	}
    }

}
