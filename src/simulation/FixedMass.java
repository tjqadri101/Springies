package simulation;

/*
 * A mass that can't be moved
 */

public class FixedMass extends Mass{

	public FixedMass(String id, double x, double y){
		super(id, x, y, 0, 0, 0);
	}
}
