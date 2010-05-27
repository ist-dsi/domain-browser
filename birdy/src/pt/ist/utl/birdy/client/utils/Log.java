package pt.ist.utl.birdy.client.utils;

public class Log {
    public static native void log(Object s)/*-{
    	if ($wnd.console){
    		$wnd.console.log(s.toString());
    	}
    }-*/;
    
    public static native void warn(Object s)/*-{
        if ($wnd.console){
    		$wnd.console.warn(s.toString());
    	}
    }-*/;
    public static native void error(Object s)/*-{
	if ($wnd.console){
    		$wnd.console.error(s.toString());
    	}
    }-*/;
    
    public static native void log()/*-{
	if ($wnd.console){
		$wnd.console.log("log ping!");
	}
}-*/;

    public static native void warn()/*-{
        if ($wnd.console){
            	$wnd.console.warn("warn ping!");
            }
    }-*/;

    public static native void error()/*-{
        if ($wnd.console){
        	$wnd.console.error("error ping!");
        }
    }-*/;
}
