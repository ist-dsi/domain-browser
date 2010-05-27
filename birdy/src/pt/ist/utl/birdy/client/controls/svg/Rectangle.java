package pt.ist.utl.birdy.client.controls.svg;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.hydro4ge.raphaelgwt.client.Raphael;
import com.hydro4ge.raphaelgwt.client.Raphael.Rect;

public class Rectangle extends Rect implements HasDoubleClickHandlers{
    protected Rectangle(SVGCanvas sc, double x, double y, double w, double h) {
	sc.raphael.super(x, y, w, h);
    }

    public double getX() {
	return attrAsDouble("x");
    }

    public double getY() {
	return attrAsDouble("y");
    }

    public double getHeight() {
	return attrAsDouble("height");
    }

    public double getWidth() {
	return attrAsDouble("height");
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

    public double getRoundRadius() {
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

    public void setHeight(int x) {
	attr("height", "" + x);
    }

    public void setWidth(int x) {
	attr("width", "" + x);
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

    public void setRoundRadius(int r) {
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

    public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
	return this.addDomHandler(handler, DoubleClickEvent.getType());
    }
}
