package forces;

import jgobjects.Assembly;
import jgobjects.Mass;

import org.jbox2d.common.Vec2;

/*
 * Pulls all objects towards the center of mass of the assembly
 */

public class CoMForce implements Force {

	private double magnitude;
	private double exponent;

	public CoMForce(double magnitude, double exponent){
		this.magnitude = magnitude;
		this.exponent =  exponent;
	}

	@Override
	public void applyForce(Assembly a){
		Vec2 COM = a.getCOM();

		for (Mass m : a.getMasses()){
			Vec2 displacement = COM.sub(m.getBody().getWorldCenter());
			float resultLength = (float) (magnitude / Math.pow(displacement.length(), exponent));
			displacement.normalize();
			displacement.mulLocal(resultLength);
			m.setForce(displacement.x, displacement.y);
		}
	}

	@Override
	public String getForceName(){
		return "CoMForce";
	}
}
