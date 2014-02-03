package simulation;
import main.Simulation;

import org.jbox2d.common.Vec2;

/*
 * Repels objects from walls, separate from bouncing off walls
 */

public class WallForce implements Force {

	private int id;
	private double magnitude;
	private double exponent;
	private Simulation simulation;
	
	public WallForce(double id, double magnitude, double exponent, Simulation simulation){
		this.id = (int) id;
		this.magnitude = magnitude;
		this.exponent =  exponent;
		this.simulation = simulation;
	}
	@Override
	public Vec2 calculateForce(Mass m) {
		// TODO refactor to get rid of if statements
		float xPos = m.getBody().getWorldCenter().x;
		float yPos = m.getBody().getWorldCenter().y;
		
		if(id == 1){//top wall, y co-or is 0
			float resultLength = (float) (magnitude / Math.pow(yPos, exponent));
			Vec2 unitDirVector = new Vec2(0f,1f);
			return unitDirVector.mul(resultLength);
		}
		if(id == 2){// right wall
			float leftWallPos = simulation.getWidth();
			float distance = Math.abs(leftWallPos-xPos);
			float resultLength = (float) (magnitude / Math.pow(distance, exponent));
			Vec2 unitDirVector = new Vec2(-1f,0f);
			return unitDirVector.mul(resultLength);
		}
		if(id == 3){// bottom wall
			float botWallPos = simulation.getHeight();
			float distance = Math.abs(botWallPos-yPos);
			float resultLength = (float) (magnitude / Math.pow(distance, exponent));
			Vec2 unitDirVector = new Vec2(0f,-1f);
			return unitDirVector.mul(resultLength);
		}
		if(id == 4){// left wall's x co-ord is 0
			float resultLength = (float) (magnitude / Math.pow(xPos, exponent));
			Vec2 unitDirVector = new Vec2(1f,0f);
			return unitDirVector.mul(resultLength);
		}
		return new Vec2(0, 0);
	}

}
