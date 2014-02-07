package simulation;
import org.jbox2d.common.Vec2;

public interface Force {
	/*
	 * Returns a Vec2 that represents the force this Force applies on Mass m
	 */
	public Vec2 calculateForce(Mass m);
	public String getForceName();
}
