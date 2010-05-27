package pt.ist.utl.birdy.client.controls.svg;


import java.util.HashMap;
import com.hydro4ge.raphaelgwt.client.Raphael.Shape;

public abstract class SVGShape{
    private Shape shape;
    private HashMap<String,String> stage;
    
    protected void stageIt(Shape s){
	for (String k : stage.keySet()){
	    s.attr(k,stage.get(k));
	}
	setShape(s);
    }
    
    protected String getAttr(String name){
	if (shape == null){
	    if (stage == null){
		stage = new HashMap<String, String>();
		return "";
	    }else{
		if(stage.containsKey(name)){
		    return stage.get(name);
		}else{
		    return "";
		}
	    }
	}else{
	    return shape.attrAsString(name);
	}
    }
    
    protected void setAttr(String name,String value){
	if (shape == null){
	    if (stage == null){
		stage = new HashMap<String, String>();
		
	    }
	    stage.put(name, value);
	}else{
	    shape.attr(name, value);
	}
    }
    
    public SVGShape() {
    }

    protected void setShape(Shape shape) {
	this.shape = shape;
    }

    protected Shape getShape() {
	return shape;
    }
    
    abstract protected void accept (SVGCanvas s);
        
}
