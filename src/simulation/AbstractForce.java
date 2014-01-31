package simulation;
import org.jbox2d.common.Vec2;

/*
 * Represents anything that can modify a mass's velocity
 */

public abstract class AbstractForce {
	public abstract Vec2 calculateForce(Mass m);
}
