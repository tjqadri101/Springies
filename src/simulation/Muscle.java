package simulation;
import jgame.JGColor;
import jgame.JGObject;
import jgame.impl.JGEngineInterface;

import org.jbox2d.common.Vec2;

public class Muscle extends JGObject implements Force{
	
	private Mass a;
	private Mass b;
	private double restLength;
	private double constant;
	private double amplitude;
	private double time;
	private double curRestLength;
	
	protected JGEngineInterface myEngine;
    private final static JGColor MUSCLE_COLOR = JGColor.red;
    private final static double MUSCLE_WIDTH = 5.0;
    protected float myRotation;
    
    public Muscle(Mass a, Mass b, double restLength, double constant, double amplitude){
		super("muscle", true, 300, 300, 8, null);

		this.a = a;
		this.b = b;
		this.restLength = restLength;
		this.constant = constant;
		this.amplitude = amplitude;
		
		myEngine = eng;
	}
    
    @Override
	public void paint(){
		myEngine.drawLine(a.x, a.y, b.x, b.y, MUSCLE_WIDTH, MUSCLE_COLOR);
	}

	@Override
	public Vec2 calculateForce(Mass m) {
		time += 0.17; //aprox 10 degrees in radians
    	curRestLength = restLength + amplitude*Math.sin(time);
    	/*float zer = 0; float rev = -1;
    	Vec2 zero = new Vec2 (zer, zer);
    	if(a.getVel().equals(zero)){
    		double thre = 3;
    		a.setVel(thre, thre);
    	}
    	if(b.getVel().equals(zero)){
    		Vec2 cur = a.getVel().mul(rev);
    		b.setVecVel(cur);
    	}
    	
    	if(a.distance(b) > curRestLength+30 || a.distance(b) < curRestLength-30){
    		a.revVel(); b.revVel();
    	}*/
    	
    	
		Vec2 displacement = new Vec2((float) (b.x - a.x), (float) (b.y - a.y));
		displacement.normalize();
		float magnitude = (float) (constant * Math.pow(a.distance(b) - curRestLength, 1));
		Vec2 force = displacement.mul(magnitude);
		
		if (m == a)
			return force;
		
		if (m == b)
			return force.negateLocal();

		//Object isn't one of the endpoints, so no force exerted
		return new Vec2(0, 0);
	}
}
