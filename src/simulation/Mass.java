package simulation;
import jboxGlue.PhysicalObject;
import jgame.JGColor;
import jgame.JGObject;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.FilterData;
import org.jbox2d.common.Vec2;

/*
 * Represents a generic Mass
 */

public class Mass extends PhysicalObject{
	protected static final double DEFAULT_RADIUS = 10;
	protected static JGColor[] MASS_COLORS = {JGColor.blue, JGColor.cyan, JGColor.green, JGColor.magenta, JGColor.orange, JGColor.pink, JGColor.white, JGColor.yellow};	
	private static int colorIndex = 0;
	private double radius;
	private float myMass;

	public Mass(String id, double x, double y, double xspeed, double yspeed, double mass, double radius){
		super(id, x, y, 1, MASS_COLORS[colorIndex % MASS_COLORS.length], xspeed, yspeed);
		this.myMass = (float) mass;
		init(x, y, radius, mass);
		colorIndex++;
	}

	public Mass(String id, double x, double y, double xspeed, double yspeed, double mass){
		this(id, x, y, xspeed, yspeed, mass, DEFAULT_RADIUS);
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

		FilterData noCollisionFilter = new FilterData();
		noCollisionFilter.groupIndex = -8;//Negative prevents collisions
		noCollisionFilter.categoryBits = 0;
		noCollisionFilter.maskBits = 0;
		shape.m_filter = noCollisionFilter;

		createBody(shape);
		setBBox(-intRadius, -intRadius, 2 * intRadius, 2 * intRadius);
	}

	public void hit(JGObject other){
		/*// we hit something! bounce off it!
        Vec2 velocity = myBody.getLinearVelocity();
        // is it a tall wall?
        final double DAMPING_FACTOR = 0.8;
        boolean isSide = other.getBBox().height > other.getBBox().width;
        if (isSide) {
            velocity.x *= -DAMPING_FACTOR;
        }
        else {
            velocity.y *= -DAMPING_FACTOR;
        }
        // apply the change
        myBody.setLinearVelocity(velocity);*/
	}

	@Override
	public void paintShape ()
	{
		myEngine.setColor(myColor);
		myEngine.drawOval(x, y, (float)radius * 2, (float)radius * 2, true, true);
	}

	/*
	 * Calculates distance from this to Mass m using Pythagorean theorem
	 */
	public double distance(Mass m){
		return Math.sqrt(Math.pow(this.x - m.x, 2) + Math.pow(this.y - m.y, 2));
	}

	public float getMass(){
		return myMass;
	}
}
