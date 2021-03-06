package input;

import java.util.Collections;
import java.util.List;

import jgobjects.Mass;
import forces.Force;

/*
 * Generic way to specify Masses and Forces to be added to the simulation
 */

public abstract class AbstractSpringiesInput {
	protected List<Mass> massList;
	protected List<Force> forceList;
	public static final double DEFAULT_VX = 0;
	public static final double DEFAULT_VY = 0;
	public static final double DEFAULT_MASS = 1;
	public static final double DEFAULT_RESTLENGTH = 50;
	public static final double DEFAULT_SPRING_CONSTANT = 1;
	public static final double DEFAULT_MUSCLE_AMPLITUDE = 1;
	public static final double DEFAULT_GRAVITY_DIRECTION = 90;
	public static final double DEFAULT_GRAVITY_MAGNITUDE = .005;
	public static final double DEFAULT_VISCOSITY_MAGNITUDE = 1;
	
	/*
	 * Return list of Masses
	 * Should be called after readInput()
	 */
	public List<Mass> getMasses(){
		return Collections.unmodifiableList(massList);
	}
	
	/*
	 * Return list of Forces
	 * Should be called after readInput()
	 */
	public List<Force> getForces(){
		return Collections.unmodifiableList(forceList);
	}
	
	/*
	 * Populate lists of Masses and Forces
	 */
	public abstract void readInput();
}
