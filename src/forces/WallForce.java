package forces;
import jgobjects.Assembly;
import jgobjects.Mass;
import main.Model;

import org.jbox2d.common.Vec2;

/*
 * Repels objects from walls, separate from bouncing off walls
 */

public class WallForce implements Force {

	private int id;
	private double magnitude;
	private double exponent;
	private Model model;
	private static final int TOP_WALL = 1;
	private static final int RIGHT_WALL = 2;
	private static final int BOTTOM_WALL = 3;
	private static final int LEFT_WALL = 4;

	public WallForce(int id, double magnitude, double exponent, Model model){
		this.id = id;
		this.magnitude = magnitude;
		this.exponent =  exponent;
		this.model = model;
	}

	@Override
	public void applyForce(Assembly assembly) {
		for (Mass m : assembly.getMasses()){
			// TODO refactor to get rid of if statements
			float xPos = m.getBody().getWorldCenter().x;
			float yPos = m.getBody().getWorldCenter().y;
			
			float resultLength = 0.0f;
			Vec2 unitDirVector = null;
			
			if(id == TOP_WALL){//top wall, y co-or is 0
				resultLength = (float) (magnitude / Math.pow(yPos, exponent));
				unitDirVector = new Vec2(0.0f,1.0f);
			}
			if(id == RIGHT_WALL){// right wall
				float rightWallPos = model.getWidth();
				float distance = Math.abs(rightWallPos-xPos);
				resultLength = (float) (magnitude / Math.pow(distance, exponent));
				unitDirVector = new Vec2(-1.0f,0.0f);
			}
			if(id == BOTTOM_WALL){// bottom wall
				float botWallPos = model.getHeight();
				float distance = Math.abs(botWallPos-yPos);
				resultLength = (float) (magnitude / Math.pow(distance, exponent));
				unitDirVector = new Vec2(0f,-1.0f);
			}
			if(id == LEFT_WALL){// left wall's x co-ord is 0
				resultLength = (float) (magnitude / Math.pow(xPos, exponent));
				unitDirVector = new Vec2(1.0f,0f);
			}
			
			if (unitDirVector != null){
				Vec2 force = unitDirVector.mul(resultLength);
				System.out.println(force);
				m.setForce(force.x, force.y);
			}
		}
	}

	@Override
	public String getForceName(){
		return "WallForce" + id;
	}
}
