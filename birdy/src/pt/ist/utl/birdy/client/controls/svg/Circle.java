package pt.ist.utl.birdy.client.controls.svg;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
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
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.MouseListener;

public class Circle extends com.hydro4ge.raphaelgwt.client.Raphael.Circle implements HasMouseOutHandlers, HasMouseOverHandlers {

    protected Circle(SVGCanvas sc, double x, double y, double r) {
        sc.raphael.super(x, y, r);
    }

    public double getX() {
        return attrAsDouble("cx");
    }

    public double getY() {
        return attrAsDouble("cy");
    }

    public String getStoke() {
        return attrAsString("stroke");
    }

    public double getStrokeWidth() {
        return attrAsDouble("stroke-weight");
    }

    public String getFill() {
        return attrAsString("fill");
    }

    public double getRadius() {
        return attrAsDouble("r");
    }

    public double getFillOpacity() {
        return attrAsDouble("fill-opaticty");
    }

    public void setX(double x) {
        attr("x", x);
    }

    public void setY(double y) {
        attr("y", y);
    }

    public void setStoke(String s) {
        attr("stroke", s);
    }

    public void setStrokeWidth(int s) {
        attr("stroke-width", "" + s);
    }

    public void setFill(String s) {
        attr("fill", "" + s);
    }

    public void setRadius(int r) {
        attr("r", "" + r);
    }

    public void setFillOpacity(double s) {
        attr("fill-opacity", "" + s);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return this.addDomHandler(handler, ClickEvent.getType());
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return this.addDomHandler(handler, MouseWheelEvent.getType());
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return this.addDomHandler(handler, MouseDownEvent.getType());
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return this.addDomHandler(handler, MouseUpEvent.getType());
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return this.addDomHandler(handler, MouseMoveEvent.getType());
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return this.addDomHandler(handler, MouseOutEvent.getType());
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return this.addDomHandler(handler, MouseOverEvent.getType());
    }
}
