package pt.ist.utl.birdy.client.controls.svg;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
import com.google.gwt.user.client.Element;
import com.hydro4ge.raphaelgwt.client.Raphael;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

public class SVGCanvas extends Canvas {

    protected FingRaphael raphael;

    private class FingRaphael extends Raphael implements HasClickHandlers, HasMouseWheelHandlers, HasMouseDownHandlers,
            HasMouseUpHandlers, HasMouseMoveHandlers {
        public FingRaphael(int width, int height) {
            super(width, height);
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

    }

    public SVGCanvas() {
        super();
        setHeight100();
        setWidth100();
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        raphael = new FingRaphael(this.getWidth(), this.getHeight());
        final WidgetCanvas inter = new WidgetCanvas(raphael);
        inter.setWidth(this.getWidth());
        inter.setHeight(this.getHeight());
        addChild(inter);
        this.addResizedHandler(new ResizedHandler() {

            public void onResized(ResizedEvent event) {
                inter.setHeight(getHeight());
                inter.setWidth(getWidth());
                raphael.setHeight("" + getHeight());
                raphael.setWidth("" + getWidth());
            }
        });
    }

    public Element getSVGElement() {
        return raphael.getElement();
    }

    public Rectangle drawRectangle(double x, double y, double w, double h) {
        return new Rectangle(this, x, y, w, h);
    }

    public Text drawText(double x, double y, String text) {
        return new Text(this, x, y, text);
    }

    public Circle drawCircle(double x, double y, double r) {
        return new Circle(this, x, y, r);
    }

    public Path drawPath(String string) {
        return new Path(this, string);
    }

    public Path drawPath(PathBuilder pb) {
        pb.draw();
        return new Path(this, pb.getProgram());
    }

    public SVGComposite getComposite() {
        return new SVGComposite(this);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return raphael.addClickHandler(handler);
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return raphael.addMouseWheelHandler(handler);
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return raphael.addMouseDownHandler(handler);
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return raphael.addMouseUpHandler(handler);
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return raphael.addMouseMoveHandler(handler);
    }
}
