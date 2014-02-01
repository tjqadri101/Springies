package input;
import java.util.List;
import simulation.*;

/*
 * Generic way to specify Masses and Forces to be added to the simulation
 */

public abstract class AbstractSpringiesInput {
	public abstract List<Mass> getMasses();
	public abstract List<Force> getForces();
	public abstract void readInput();
}
