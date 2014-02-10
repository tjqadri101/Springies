package simulation;

import jgame.JGObject;

import org.jbox2d.common.Vec2;

public class MouseMass extends Mass{

	public MouseMass(double x, double y, double xspeed, double yspeed) {
		super("", x, y, xspeed, yspeed, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paintShape ()
	{
		//paint nothing
	}

	@Override
	public void setForce (double x, double y) {
	        //apply no force
	}

	@Override
	public void hit(JGObject other){
		// we hit something but do nothing
       
	}
}
