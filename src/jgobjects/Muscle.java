package jgobjects;

import forces.Force;
import jgame.JGColor;

public class Muscle extends Spring implements Force{

	protected double baseRestLength;
	protected double amplitude;
	protected double time;
	protected double stretch;

	private final static JGColor MUSCLE_POSITIVE_COLOR = JGColor.red;
	private final static JGColor MUSCLE_NEGATIVE_COLOR = new JGColor(144, 0, 0);
	private final static double MUSCLE_WIDTH = 5.0;
	protected float myRotation;

	public Muscle(Mass a, Mass b, double restLength, double constant, double amplitude){
		super("muscle", a, b, restLength, constant);
		
		this.baseRestLength = restLength;
		this.amplitude = amplitude;
	}

	@Override
	public void paint(){
		if (stretch > 0)
			myEngine.drawLine(a.x, a.y, b.x, b.y, MUSCLE_WIDTH, MUSCLE_POSITIVE_COLOR);
		else
			myEngine.drawLine(a.x, a.y, b.x, b.y, MUSCLE_WIDTH, MUSCLE_NEGATIVE_COLOR);
	}
	
	@Override
	public void applyForce(Assembly assembly) {
		updateStretch();
		super.applyForce(assembly);
	}

	public void changeAmplitude(double amount){
		amplitude += amount;
	}
	
	private void updateStretch(){
		time += 0.17; //aprox 10 degrees in radians
		stretch = amplitude * Math.sin(time);  
	}

	public String getForceName(){
		return "Muscle";
	}
}
