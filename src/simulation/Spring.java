package simulation;

import jgame.JGObject;

import org.jbox2d.common.Vec2;

public class Spring implements Force {
	
	private Mass a;
	private Mass b;
	private double restLength;
	private double constant;
	
	public Spring(Mass a, Mass b, double restLength, double constant){
		this.a = a;
		this.b = b;
		this.restLength = restLength;
		this.constant = constant;
	}
	
	@Override
	public Vec2 calculateForce(Mass m) {
		boolean push = a.distance(b) < restLength;
		Vec2 displacement = new Vec2((float) (b.x - a.x), (float) (b.y - a.y));
		displacement.normalize();
		float magnitude = (float) (constant * Math.pow(a.distance(b) - restLength, 1));
		Vec2 force = displacement.mul(magnitude);
		//Correct force direction by negation if necessary
		if ((m == a && push) || (m == b && !push))
				force.negateLocal();
		
		if (m==a || m==b)
			return force;
		
		//Object isn't one of the endpoints, so no force exerted
		return new Vec2(0, 0);
	}
}
