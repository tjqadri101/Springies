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
	private static final int TOP_WALL = 1;
	private static final int RIGHT_WALL = 2;
	private static final int BOTTOM_WALL = 3;
	private static final int LEFT_WALL = 4;
	
	public WallForce(int id, double magnitude, double exponent, Simulation simulation){
		this.id = id;
		this.magnitude = magnitude;
		this.exponent =  exponent;
		this.simulation = simulation;
	}
	
	@Override
	public Vec2 calculateForce(Mass m) {
		// TODO refactor to get rid of if statements
		float xPos = m.getBody().getWorldCenter().x;
		float yPos = m.getBody().getWorldCenter().y;
		float posOne = 1;
		float negOne = -1;
		if(id == TOP_WALL){//top wall, y co-or is 0
			float resultLength = (float) (magnitude / Math.pow(yPos, exponent));
			Vec2 unitDirVector = new Vec2(0f,posOne);
			return unitDirVector.mul(resultLength);
		}
		if(id == RIGHT_WALL){// right wall
			float rightWallPos = simulation.getWidth();
			float distance = Math.abs(rightWallPos-xPos);
			float resultLength = (float) (magnitude / Math.pow(distance, exponent));
			Vec2 unitDirVector = new Vec2(negOne,0f);
			return unitDirVector.mul(resultLength);
		}
		if(id == BOTTOM_WALL){// bottom wall
			float botWallPos = simulation.getHeight();
			float distance = Math.abs(botWallPos-yPos);
			float resultLength = (float) (magnitude / Math.pow(distance, exponent));
			Vec2 unitDirVector = new Vec2(0f,negOne);
			return unitDirVector.mul(resultLength);
		}
		if(id == LEFT_WALL){// left wall's x co-ord is 0
			float resultLength = (float) (magnitude / Math.pow(xPos, exponent));
			Vec2 unitDirVector = new Vec2(posOne,0f);
			return unitDirVector.mul(resultLength);
		}
		return new Vec2(0, 0);
	}

	@Override
	public String getForceName(){
		return "WallForce" + id;
	}
}
