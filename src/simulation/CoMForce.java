package simulation;

import main.Simulation;

import org.jbox2d.common.Vec2;

/*
 * Pulls all objects towards the center of mass
 */

public class CoMForce implements Force {

	private double magnitude;
	private double exponent;
	private Simulation simulation;

	public CoMForce(double magnitude, double exponent, Simulation simulation){
		this.magnitude = magnitude;
		this.exponent =  exponent;
		this.simulation = simulation;
	}
	
	@Override
	public Vec2 calculateForce(Mass m) {
		Vec2 COM = simulation.getCOM();
		Vec2 displacement = m.getBody().getWorldCenter().sub(COM);
		float resultLength = (float) (magnitude / Math.pow(displacement.length(), exponent));
		displacement.normalize();
		
		return displacement.mul(resultLength);
	}
}
