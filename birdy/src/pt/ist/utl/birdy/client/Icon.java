package pt.ist.utl.birdy.client;

public class Icon {
    public static String use(String name) {
        return "/birdy/resources/icons/" + name + ".png";
    }

    public static String use(String name, String xtra) {
        return "/birdy/resources/icons/" + name + "-" + xtra + ".png";
    }
}
