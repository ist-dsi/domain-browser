package pt.ist.utl.birdy.client.controls.wiring;

public class Point {
    public double x;
    public double y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = (double) x;
        this.y = (double) y;
    }

    public Point(double x2, double y2) {
        x = x2;
        y = y2;
    }
}
