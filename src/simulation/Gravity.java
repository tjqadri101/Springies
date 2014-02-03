package simulation;
import org.jbox2d.common.Vec2;

/*
 * Pulls objects down
 */

public class Gravity implements Force {
	
	private double direction;
	private double magnitude;
	
	public Gravity(double direction, double magnitude){
		this.direction = direction;
		this.magnitude = magnitude;
	}
	
	@Override
	public Vec2 calculateForce(Mass m) {
		float x = (float) (magnitude * Math.cos(Math.toRadians(direction)) * m.getBody().getMass());
		float y = (float) (magnitude * Math.sin(Math.toRadians(direction)) * m.getBody().getMass());
		System.out.println(x + " " + y);
		return new Vec2(x, y);
	}
}
