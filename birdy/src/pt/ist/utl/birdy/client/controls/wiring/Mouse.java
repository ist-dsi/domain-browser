package pt.ist.utl.birdy.client.controls.wiring;

import pt.ist.utl.birdy.client.controls.DetailedMenuItem;

public class Mouse {
    private static Wire wire;
    private static DetailedMenuItem menuItem;
    public static void setWire(Wire wire) {
	Mouse.wire = wire;
    }

    public static Wire getWire() {
	return wire;
    }

    public static void setStrategy(DetailedMenuItem menuItem) {
	Mouse.menuItem = menuItem;
    }

    public static DetailedMenuItem getStrategy() {
	return menuItem;
    }
}
