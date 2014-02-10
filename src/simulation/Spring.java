package simulation;

import jgame.JGColor;

public class Spring extends Muscle implements Force {
	
    private final static JGColor SPRING_COMPRESSED_COLOR = new JGColor(96, 96, 96);
    private final static JGColor SPRING_STRETCHED_COLOR = new JGColor(208, 208, 208);
    private final static double SPRING_WIDTH = 5.0;
    protected float myRotation;

	public Spring(Mass a, Mass b, double restLength, double constant){
		super("spring", a, b, restLength, constant, 0);
	}
	

	@Override
	public void paint(){
		if (a.distance(b) > restLength)
			myEngine.drawLine(a.x, a.y, b.x, b.y, SPRING_WIDTH, SPRING_STRETCHED_COLOR);
		else
			myEngine.drawLine(a.x, a.y, b.x, b.y, SPRING_WIDTH, SPRING_COMPRESSED_COLOR);
	}
	
	@Override
	public String getForceName(){
		return "Spring";
	}
}
