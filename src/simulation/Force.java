package simulation;

public interface Force {
	/*
	 * Returns a Vec2 that represents the force this Force applies on Mass m
	 */
	public void applyForce(Assembly assembly);
	public String getForceName();
}
