package pt.ist.utl.birdy.client.controls.svg;

public class Text extends com.hydro4ge.raphaelgwt.client.Raphael.Text {
    protected Text(SVGCanvas sc, double x, double y, String text) {
        sc.raphael.super(x, y, text);
    }

    public void setFontSize(int px) {
        attr("font-size", "" + px);
    }

    public void setBold() {
        attr("font-weight", "bold");
    }

    public void setColor(String color) {
        attr("fill", color);
    }

    public void alignLeft() {
        attr("text-anchor", "start");
    }
}
