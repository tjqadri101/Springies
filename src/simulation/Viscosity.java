package simulation;
import org.jbox2d.common.Vec2;

/*
 * Resists object's motion
 */

public class Viscosity implements Force {
	private double magnitude;
	
	public Viscosity( double magnitude){
		this.magnitude = magnitude;
	}
	
	@Override
	public Vec2 calculateForce(Mass m) {
		Vec2 viscos = m.getVel().mul((float) magnitude);
		return viscos.negateLocal();
	}
	
	@Override
	public String getForceName(){
		return "Viscosity";
	}
}
