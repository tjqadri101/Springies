package forces;

import jgobjects.Assembly;

public interface Force {
	/*
	 * Applies the force to the assembly
	 */
	public void applyForce(Assembly assembly);
	
	/*
	 * Returns a descriptive name of the force for identification purposes
	 */
	public String getForceName();
}
