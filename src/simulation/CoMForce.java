package simulation;
import java.util.List;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;

/*
 * Pulls all objects towards the center of mass
 */

public class CoMForce implements Force {
	
	private double magnitude;
	private double exponent;
	private List<Mass> massList;
	
	public CoMForce(double magnitude, double exponent, List<Mass> list){
		this.magnitude = magnitude;
		this.exponent =  exponent;
		massList = list;
	}
	@Override
	public Vec2 calculateForce(Mass m) {
		// TODO Auto-generated method stub
		float massSum = 0;
		double init = 0;
		Vec2 sum = new Vec2((float) init, (float) init);
		for (Mass m1 : massList){
			float curMass = m1.getMass();
			Vec2 position = m1.getBody().getWorldCenter();
			Vec2 product = position.mul(curMass);
			sum = sum.add(product);
			massSum += curMass;
		}
		
		float numerator = 1;
		float inverse = numerator/massSum;
		Vec2 CoM = sum.mul(inverse);
		Vec2 distance = m.getBody().getWorldCenter().sub(CoM);
		float dist = distance.length();
		double distan = (double) dist;
		float vecMag = (float)	(magnitude/Math.pow(distan, exponent));	
		distance.normalize();
		Vec2 force = distance.mul(vecMag);
		
		return force;
	}

}
