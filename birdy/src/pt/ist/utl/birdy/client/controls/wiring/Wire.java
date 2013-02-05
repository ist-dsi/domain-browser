package pt.ist.utl.birdy.client.controls.wiring;

import pt.ist.utl.birdy.client.controls.svg.Path;
import pt.ist.utl.birdy.client.controls.svg.PathBuilder;
import pt.ist.utl.birdy.client.controls.svg.SVGCanvas;

import com.google.gwt.event.shared.HandlerRegistration;

public class Wire extends Path {
    private Terminal start;
    private Terminal end;

    HandlerRegistration hr1;
    HandlerRegistration hr2;

    protected Wire(SVGCanvas sc) {
        super(sc, "");

        setStoke("#ffffff");
        setStrokeWidth(3);

    }

    public void setStart(Terminal start) {
        this.start = start;
    }

    public Terminal getStart() {
        return start;
    }

    public void setEnd(Terminal end) {
        this.end = end;
    }

    public Terminal getEnd() {
        return end;
    }

    public void draw(final double ex, final double ey) {
        changePath(new PathBuilder() {
            @Override
            protected void draw() {
                Terminal startPoint = (getStart() != null) ? getStart() : getEnd();
                double x = startPoint.getX();
                double y = startPoint.getY();

                double inv = (ex - x) > 0 ? 1 : -1;

                double coeffMulDirection = 100;
                double distance = Math.sqrt(Math.pow(x - ex, 2) + Math.pow(y - ey, 2));

                if (distance > coeffMulDirection) {
                    coeffMulDirection = distance / 2;
                }

                moveTo(x, y);
                curveTo(ex, ey, x + (inv * coeffMulDirection), y, ex - (inv * coeffMulDirection), ey);

            }
        });
    }

    public void draw() {
        Terminal startPoint = (getEnd() != null) ? getEnd() : getStart();
        draw(startPoint.getX(), startPoint.getY());
    }

}
