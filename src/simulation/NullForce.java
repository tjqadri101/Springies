package simulation;

import org.jbox2d.common.Vec2;

public class NullForce implements Force {

	@Override
	public Vec2 calculateForce(Mass m) {
		return new Vec2(0.0f, 0.0f);
	}
	
	@Override
	public String getForceName(){
		return "NullForce";
	}
}
