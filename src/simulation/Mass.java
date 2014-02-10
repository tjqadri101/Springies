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
		noCollisionFilter.groupIndex = -2;//Negative prevents mass-mass collisions
		noCollisionFilter.categoryBits = 31;
		noCollisionFilter.maskBits = 31;
		shape.filter = noCollisionFilter;

		createBody(shape);
		setBBox(-intRadius, -intRadius, 2 * intRadius, 2 * intRadius);
	}

	public void hit(JGObject other){
		// we hit something! bounce off it!
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
        myBody.setLinearVelocity(velocity);
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
		Vec2 thisPos = this.getBody().getPosition();
		Vec2 mPos = m.getBody().getPosition();
		return Math.sqrt(Math.pow(thisPos.x - mPos.x, 2) + Math.pow(thisPos.y - mPos.y, 2));
	}
	
	public double calcArbitraryDist(double x, double y){
		return Math.sqrt(Math.pow(this.getBody().getPosition().x - x, 2) + Math.pow(this.getBody().getPosition().y - y, 2));
	}

	public float getMass(){
		return myMass;
	}
}
