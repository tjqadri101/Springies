package jboxGlue;

import org.jbox2d.collision.Shape;
import org.jbox2d.dynamics.ContactFilter;

public class NoContactFilter implements ContactFilter{
	public boolean shouldCollide(Shape shape1, Shape shape2){
		return false;
	}
}