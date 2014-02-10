package jgobjects;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jbox2d.common.Vec2;

/*
 * An independent collection of masses
 * 
 * Typically, each call to an AbstractSpringiesInput adds one assembly that consists of Masses and connections
 */

public class Assembly {
	private List<Mass> massList;
	private Mass nearestAssemblyMass;
	
	public Assembly(List<Mass> inputMasses){
		massList = new LinkedList<Mass>();
		massList.addAll(inputMasses);
	}
	
	public List<Mass> getMasses(){
		return Collections.unmodifiableList(massList);
	}
	
	public Vec2 getCOM() {
		float massSum = 0;
		Vec2 sum = new Vec2(0.0f, 0.0f);
		for (Mass m : massList){
			Vec2 weightedPosition = m.getBody().getWorldCenter().mul(m.getMass());
			sum = sum.add(weightedPosition);
			massSum += m.getMass();
		}
		return sum.mul(1.0f/massSum);
	}
	
	public void clearAssembly(){
		massList = new LinkedList<Mass>();
	}
	
	public double getNearestDist(double nearestDist, double mouseXPos, double mouseYpos){
		for (Mass m : massList){
			if(m.calcArbitraryDist(mouseXPos, mouseYpos) < nearestDist){
				nearestDist = m.calcArbitraryDist(mouseXPos, mouseYpos);
				nearestAssemblyMass = m;
			}
		}
		return nearestDist;
	}
	public Mass getNearestMass(){
		return nearestAssemblyMass;
	}
}
