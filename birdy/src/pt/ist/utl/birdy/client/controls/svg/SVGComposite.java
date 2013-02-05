package pt.ist.utl.birdy.client.controls.svg;

import com.hydro4ge.raphaelgwt.client.Raphael.Set;

final public class SVGComposite extends Set {
    private SVGCanvas canvas;

    protected SVGComposite(SVGCanvas sc) {
        sc.raphael.super();
        setCanvas(sc);
    }

    void setCanvas(SVGCanvas canvas) {
        this.canvas = canvas;
    }

    public SVGCanvas getCanvas() {
        return canvas;
    }
}
