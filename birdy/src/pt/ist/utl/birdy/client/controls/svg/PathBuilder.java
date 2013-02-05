package pt.ist.utl.birdy.client.controls.svg;

import java.util.ArrayList;
import java.util.List;

public abstract class PathBuilder {
    private List<String> s = new ArrayList<String>();

    protected void moveTo(double x, double y) {
        s.add("M" + x + "," + y);
    }

    protected void moveTo(double dx, double dy, boolean relative) {
        s.add("m" + dx + "," + dy);
    }

    protected void lineTo(double x, double y) {
        s.add("L" + x + "," + y);
    }

    protected void lineTo(double dx, double dy, boolean relative) {
        s.add("l" + dx + "," + dy);
    }

    protected void horizontalLineTo(double x) {
        s.add("H" + x);
    }

    protected void horizontalLineTo(double dx, boolean relative) {
        s.add("h" + dx);
    }

    protected void verticalLineTo(double x) {
        s.add("H" + x);
    }

    protected void verticalLineTo(double dx, boolean relative) {
        s.add("h" + dx);
    }

    protected void curveTo(double x, double y, double xControl1, double yControl1, double xControl2, double yControl2) {
        s.add("C" + xControl1 + "," + yControl1 + " " + xControl2 + "," + yControl2 + " " + x + "," + y);
    }

    protected void curveTo(double x, double y, double xControl1, double yControl1, double xControl2, double yControl2,
            boolean relative) {
        s.add("c" + xControl1 + "," + yControl1 + " " + xControl2 + "," + yControl2 + " " + x + "," + y);
    }

    protected void curveTo(double x, double y, double xControl, double yControl) {
        s.add("S" + xControl + "," + yControl + " " + x + "," + y);
    }

    protected void curveTo(double x, double y, double xControl, double yControl, boolean relative) {
        s.add("s" + xControl + "," + yControl + " " + x + "," + y);
    }

    protected void quadraticCurveTo(double x, double y, double xControl, double yControl) {
        s.add("Q" + xControl + "," + yControl + " " + x + "," + y);
    }

    protected void quadraticCurveTo(double x, double y, double xControl, double yControl, boolean relative) {
        s.add("q" + xControl + "," + yControl + " " + x + "," + y);
    }

    protected void quadraticCurveTo(double x, double y) {
        s.add("T" + x + "," + y);
    }

    protected void quadraticCurveTo(double x, double y, boolean relative) {
        s.add("t" + x + "," + y);
    }

    abstract protected void draw();

    protected String getProgram() {
        StringBuffer foo = new StringBuffer();
        for (String command : s) {
            foo.append(command);
            foo.append(" ");
        }
        return foo.toString();
    }
}
