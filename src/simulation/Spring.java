package simulation;

import jgame.JGColor;
import jgame.JGObject;
import jgame.impl.JGEngineInterface;

import org.jbox2d.common.Vec2;

public class Spring extends JGObject implements Force {

	private Mass a;
	private Mass b;
	private double restLength;
	private double constant;
	
	protected JGEngineInterface myEngine;
    private final static JGColor SPRING_COMPRESSED_COLOR = new JGColor(96, 96, 96);
    private final static JGColor SPRING_STRETCHED_COLOR = new JGColor(208, 208, 208);
    private final static double SPRING_WIDTH = 5.0;
    protected float myRotation;

	public Spring(Mass a, Mass b, double restLength, double constant){
		super("spring", true, 300, 300, 8, null);

		this.a = a;
		this.b = b;
		this.restLength = restLength;
		this.constant = constant;
		
		myEngine = eng;
	}

	@Override
	public void paint(){
		if (a.distance(b) > restLength)
			myEngine.drawLine(a.x, a.y, b.x, b.y, SPRING_WIDTH, SPRING_STRETCHED_COLOR);
		else
			myEngine.drawLine(a.x, a.y, b.x, b.y, SPRING_WIDTH, SPRING_COMPRESSED_COLOR);
	}

	@Override
	public Vec2 calculateForce(Mass m) {
		Vec2 displacement = new Vec2((float) (b.x - a.x), (float) (b.y - a.y));
		displacement.normalize();
		float magnitude = (float) (constant * Math.pow(a.distance(b) - restLength, 1));
		Vec2 force = displacement.mul(magnitude);
		
		if (m == a)
			return force;
		
		if (m == b)
			return force.negateLocal();

		//Object isn't one of the endpoints, so no force exerted
		return new Vec2(0, 0);
	}
	
	@Override
	public String getForceName(){
		return "Spring";
	}
}
