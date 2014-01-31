package simulation;
import jboxGlue.PhysicalObject;
import jgame.JGColor;

import org.jbox2d.collision.CircleDef;

/*
 * Represents a generic Mass
 */

public class Mass extends PhysicalObject{
	protected static final double DEFAULT_RADIUS = 10;
	protected static JGColor DEFAULT_COLOR = JGColor.yellow;	
	
	private double radius;
	
	public Mass(String id, double x, double y, double xspeed, double yspeed, double mass, double radius){
		super(id, x, y, 1, DEFAULT_COLOR, xspeed, yspeed);
		init(x, y, radius, mass);
	}
	
	public Mass(String id, double x, double y, double xspeed, double yspeed, double mass){
		super(id, x, y, 1, DEFAULT_COLOR, xspeed, yspeed);
		init(x, y, DEFAULT_RADIUS, mass);
	}
	
	protected void init (double x, double y, double radius, double mass)
    {
		setPos(x, y);
        // save arguments
        this.radius = radius;
        int intRadius = (int)radius;
        // make it a circle
        CircleDef shape = new CircleDef();
        shape.radius = (float)radius;
        shape.density = (float)mass;
        createBody(shape);
        setBBox(-intRadius, -intRadius, 2 * intRadius, 2 * intRadius);
    }

    @Override
    public void paintShape ()
    {
        myEngine.setColor(myColor);
        myEngine.drawOval(x, y, (float)radius * 2, (float)radius * 2, true, true);
    }
    
    public double distance(Mass m){
    	return Math.sqrt(Math.pow(this.x - m.x, 2) + Math.pow(this.y - m.y, 2));
    }
}
