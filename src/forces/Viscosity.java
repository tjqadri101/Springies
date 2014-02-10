package forces;
import jgobjects.Assembly;
import jgobjects.Mass;

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
	public void applyForce(Assembly assembly) {
		for (Mass m : assembly.getMasses()){
			Vec2 viscos = m.getVel().mul((float) magnitude);
			viscos.negateLocal();
			m.setForce(viscos.x, viscos.y);
		}
	}
	
	@Override
	public String getForceName(){
		return "Viscosity";
	}
}
