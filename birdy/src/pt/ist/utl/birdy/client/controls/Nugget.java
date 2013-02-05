package pt.ist.utl.birdy.client.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ist.utl.birdy.client.controls.strategy.Strategy;
import pt.ist.utl.birdy.client.controls.svg.Rectangle;
import pt.ist.utl.birdy.client.controls.svg.SVGComposite;
import pt.ist.utl.birdy.client.controls.svg.Text;
import pt.ist.utl.birdy.client.controls.wiring.CanvasWorkbench;
import pt.ist.utl.birdy.client.controls.wiring.InputTerminal;
import pt.ist.utl.birdy.client.controls.wiring.OutputTerminal;
import pt.ist.utl.birdy.client.controls.wiring.Point;
import pt.ist.utl.birdy.client.controls.wiring.Terminal;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;

public abstract class Nugget {
    private SVGComposite composite;
    boolean onDrag = false;
    Point dragInit;
    Point locInit;
    HandlerRegistration handlerRegistration1;
    Window optionsWindow;
    Rectangle rectangle;
    private Strategy strategy;
    private List<InputTerminal> inputs = new ArrayList<InputTerminal>();
    private List<OutputTerminal> outputs = new ArrayList<OutputTerminal>();
    private HashMap<String, String> properties = new HashMap<String, String>();

    public Nugget(final CanvasWorkbench cw, final String text, String color, int inputs, int outputs, Strategy s) {
        setComposite(cw.getComposite());
        setProperty("kind", this.getClass().getName());
        final Rectangle r = cw.drawRectangle(100, 100, 80, 100);
        r.setStoke(color);
        r.setStrokeWidth(2);
        r.setRoundRadius(10);
        r.setFill(color);
        r.setFillOpacity(0.1);
        rectangle = r;
        Text t = cw.drawText(105, 180, text);
        t.setBold();
        t.alignLeft();
        t.setColor("#ffffff");
        t.setFontSize(10);
        getComposite().push(r);
        getComposite().push(t);
        setStrategy(s);
        for (int i = 0; i < inputs; i++) {
            InputTerminal ci = new InputTerminal(cw, this, 110 + 15 * i, 120);
            getComposite().push(ci);
            this.inputs.add(ci);
        }
        for (int i = 0; i < outputs; i++) {
            OutputTerminal co = new OutputTerminal(cw, this, 170 + 15 * i, 120);
            getComposite().push(co);
            this.outputs.add(co);
        }

        t.toBack();
        r.addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(MouseDownEvent event) {
                if (!onDrag) {
                    onDrag = true;
                    dragInit = new Point(event.getRelativeX(cw.getSVGElement()), event.getRelativeY(cw.getSVGElement()));
                    locInit =
                            new Point(r.getX() - event.getRelativeX(cw.getSVGElement()), r.getY()
                                    - event.getRelativeY(cw.getSVGElement()));
                    handlerRegistration1 = cw.addMouseMoveHandler(new MouseMoveHandler() {
                        public void onMouseMove(MouseMoveEvent event) {
                            if (onDrag) {
                                double dx = 0, dy = 0;
                                if (event.getX() + locInit.x <= cw.getWidth() - r.getWidth() && event.getX() + locInit.x >= 0) {
                                    dx = (event.getX() + locInit.x) - r.getX();
                                }

                                if (event.getY() + locInit.y <= cw.getHeight() - r.getHeight() && event.getY() + locInit.y >= 0) {
                                    dy = (event.getY() + locInit.y) - r.getY();
                                }
                                getComposite().translate(dx, dy);
                                dragInit = new Point(event.getX(), event.getY());
                                draw();
                            }
                        }

                    });
                }
            }
        });
        r.addMouseUpHandler(new MouseUpHandler() {

            public void onMouseUp(MouseUpEvent event) {
                if (onDrag) {
                    onDrag = false;
                    handlerRegistration1.removeHandler();
                }
            }
        });
        r.addDoubleClickHandler(new DoubleClickHandler() {

            public void onDoubleClick(DoubleClickEvent event) {
                Nugget.this.doubleClick();
            }
        });

    }

    public void doubleClick() {
        optionsWindow.show();
    }

    public void createWindow(CanvasWorkbench cw) {
        optionsWindow = new Window();
        optionsWindow.setTitle(this.getStrategy().getName());
        optionsWindow.setAutoSize(true);
        VerticalPanel vp = new VerticalPanel();
        this.createOptions(vp);
        optionsWindow.addItem(vp);
        cw.addChild(optionsWindow);
        optionsWindow.hide();
    }

    public abstract void createOptions(VerticalPanel vp);

    public abstract void saveOptions();

    public void setComposite(SVGComposite composite) {
        this.composite = composite;
    }

    public SVGComposite getComposite() {
        return composite;
    }

    public double getX() {
        return rectangle.getX();
    }

    public double getY() {
        return rectangle.getY();
    }

    public void moveTo(double x, double y) {
        composite.translate(x - getX(), y - getY());
    }

    public List<InputTerminal> getInputs() {
        return inputs;
    }

    public List<OutputTerminal> getOutputs() {
        return outputs;
    }

    public void draw() {
        for (Terminal t : getInputs()) {
            t.draw();
        }
        for (Terminal t : getOutputs()) {
            t.draw();
        }
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String val) {
        properties.put(key, val);
    }

    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }

    abstract public HashMap<String, String> compiled();
}
