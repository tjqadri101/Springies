package simulation;
import org.jbox2d.common.Vec2;

/*
 * Pulls objects down
 */

public class Gravity implements Force {
	
	private static final double GRAVITY_CONSTANT = 9.81;
	
	@Override
	public Vec2 calculateForce(Mass m) {
		return null;
	}
}
