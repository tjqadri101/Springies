package simulation;

/*
 * A mass that can't be moved
 */

public class FixedMass extends Mass{

	//Replace this
	public FixedMass(String id){
		super(id, 0, 0, 0, 0, 999999);
	}
}
