package forces;

import jgobjects.Assembly;
import jgobjects.Mass;

/*
 * Force that pulls all Masses in the assembly down
 */

public class Gravity implements Force {

	private double direction;
	private double magnitude;

	public Gravity(double direction, double magnitude){
		this.direction = direction;
		this.magnitude = magnitude;
	}

	@Override
	public void applyForce(Assembly a) {
		for (Mass m : a.getMasses()){
			float x = (float) (magnitude * Math.cos(Math.toRadians(direction)) * m.getBody().getMass());
			float y = (float) (magnitude * Math.sin(Math.toRadians(direction)) * m.getBody().getMass());
			m.setForce(x, y);
		}
	}

	@Override
	public String getForceName(){
		return "Gravity";
	}
}
