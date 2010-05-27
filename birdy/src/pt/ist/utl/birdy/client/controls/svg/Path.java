package pt.ist.utl.birdy.client.controls.svg;



public class Path extends com.hydro4ge.raphaelgwt.client.Raphael.Path{
    
    private SVGCanvas canvas;
    
    protected Path(SVGCanvas sc,String path) {
	sc.raphael.super(path);
	canvas = sc;
    }
    
    public void changePath(String path){
	attr("path",path);
    }
    
    public void changePath(PathBuilder pb){
	pb.draw();
	attr("path",pb.getProgram());
    }
    
    public String getStoke() {
	return attrAsString("stroke");
    }

    public double getStrokeWidth() {
	return attrAsDouble("stroke-weight");
    }
    
    public void setStoke(String s) {
	attr("stroke", s);
    }

    public void setStrokeWidth(int s) {
	attr("stroke-width", "" + s);
    }

    public SVGCanvas getCanvas() {
	return canvas;
    }
}
