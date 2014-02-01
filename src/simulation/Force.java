package simulation;
import org.jbox2d.common.Vec2;

public interface Force {
	public abstract Vec2 calculateForce(Mass m);
}
