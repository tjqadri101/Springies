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
	private double stretch;
	
	protected JGEngineInterface myEngine;
    private final static JGColor MUSCLE_POSITIVE_COLOR = JGColor.red;
    private final static JGColor MUSCLE_NEGATIVE_COLOR = new JGColor(144, 0, 0);
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
    	if (stretch > 0)
    		myEngine.drawLine(a.x, a.y, b.x, b.y, MUSCLE_WIDTH, MUSCLE_POSITIVE_COLOR);
    	else
    		myEngine.drawLine(a.x, a.y, b.x, b.y, MUSCLE_WIDTH, MUSCLE_NEGATIVE_COLOR);
	}

	@Override
	public Vec2 calculateForce(Mass m) {
		updateStretch();  	
    	double curRestLength = restLength + stretch;
		
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
	
	private void updateStretch(){
		time += 0.17; //aprox 10 degrees in radians
    	stretch = amplitude * Math.sin(time);  
	}
}
