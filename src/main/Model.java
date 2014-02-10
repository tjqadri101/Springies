package main;

import forces.Force;
import input.AbstractSpringiesInput;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import jboxGlue.WorldManager;
import jgame.platform.JGEngine;
import jgobjects.Assembly;
import jgobjects.FixedMass;
import jgobjects.Muscle;
import jgobjects.Spring;

@SuppressWarnings("serial")

/*
 * Model actually runs the simulation
 * Contains main method
 */

public class Model extends JGEngine
{
	public static final String TITLE = "springies_team04";
	
	public static final int HEIGHT = 600;
	public static final double ASPECT = 4.0 / 3.0;
	public static final double FONT_HEIGHT = 20;

	public final boolean DEFAULT_FORCE_STATUS = false;
	private boolean justClicked = true;

	public static final String[] togglableForces = {"Gravity", "Viscosity", "CoMForce", "WallForce1", "WallForce2", "WallForce3", "WallForce4"};
	public static final int[] toggleKeys = {KeyEvent.VK_G, KeyEvent.VK_V, KeyEvent.VK_M, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4};
	private boolean[] keyStatuses = new boolean[toggleKeys.length];

	public static final int NEW_ASSEMBLY_KEY = KeyEvent.VK_N;
	public static final int CLEAR_KEY = KeyEvent.VK_C;
	public static final int MUSCLE_PLUS_KEY = KeyEvent.VK_EQUALS;//Need Shift to get +
	public static final int MUSCLE_MINUS_KEY = KeyEvent.VK_MINUS;
	public static final int WALL_OUT_KEY = KeyEvent.VK_UP;
	public static final int WALL_IN_KEY = KeyEvent.VK_DOWN;

	public static final double MUSCLE_AMPLITUDE_CHANGE = 5.0;
	private double wallShift = 0.0;
	public static final double WALL_SHIFT_DELTA = 50.0;//Amount to shift walls in/out per up/down keypress
	
	private Factory factory;

	private List<Force> forceList;
	private List<Assembly> assemblyList;
	private FixedMass mouseMass;
	private Spring mouseSpring;

	public Model (){
		int height = HEIGHT;
		double aspect = ASPECT;
		initEngineComponent((int) (height * aspect), height);

		forceList = new LinkedList<Force>();
		assemblyList = new LinkedList<Assembly>();
		
		factory = new Factory(this);

		for (int i=0; i<keyStatuses.length; i++)
			keyStatuses[i] = DEFAULT_FORCE_STATUS;
	}

	@Override
	public void initCanvas (){
		// I have no idea what tiles do...
		setCanvasSettings(1, // width of the canvas in tiles
				1, // height of the canvas in tiles
				displayWidth(), // width of one tile
				displayHeight(), // height of one tile
				null,// foreground colour -> use default colour white
				null,// background colour -> use default colour black
				null); // standard font -> use default font
	}

	@Override
	public void initGame (){
		setFrameRate(60, 2);
		// NOTE:
		// world coordinates have y pointing down
		// game coordinates have y pointing up
		// so gravity is up in world coords and down in game coords
		// so set all directions (e.g., forces, velocities) in world coords
		WorldManager.initWorld(this);
		//WorldManager.getWorld().setContactFilter(new NoContactFilter());//Turns off ALL Jbox collisions

		//input = new XMLInput("assets/daintywalker.xml");
		//WorldManager.getWorld().setGravity(new Vec2(0.0f, .1f));
		loadFile();
		
		factory.addWalls();
	}

	private void loadFile(){
		AbstractSpringiesInput newInput = factory.buildListsFromInput();
		forceList.addAll(newInput.getForces());
		assemblyList.add(new Assembly(newInput.getMasses()));
	}
	
	public int getHeight(){
		return HEIGHT;
	}

	public int getWidth(){
		return (int) (HEIGHT*ASPECT);
	}

	@Override
	public void doFrame (){
		WorldManager.getWorld().step(1f, 1);
		calculateForces();
		moveObjects();
		checkCollision(Factory.WALL_COLID, 1);//Check for mass-wall collisions
		checkKeys();
		checkMouse();
	}

	private void checkKeys(){
		for (int i=0; i<toggleKeys.length; i++)
			if (getKey(toggleKeys[i])){
				keyStatuses[i] = !keyStatuses[i];
				clearKey(toggleKeys[i]);
			}

		if (getKey(NEW_ASSEMBLY_KEY)){
			loadFile();
			clearKey(NEW_ASSEMBLY_KEY);
		}

		if (getKey(CLEAR_KEY)){
			clearAssemblies();
			clearKey(CLEAR_KEY);
		}

		if (getKey(MUSCLE_PLUS_KEY) && getKey(KeyEvent.VK_SHIFT)){
			changeMuscleAmplitudes(+1 * MUSCLE_AMPLITUDE_CHANGE);
			clearKey(MUSCLE_PLUS_KEY);
			clearKey(KeyEvent.VK_SHIFT);
		}

		if (getKey(MUSCLE_MINUS_KEY)){
			changeMuscleAmplitudes(-1 * MUSCLE_AMPLITUDE_CHANGE);
			clearKey(MUSCLE_MINUS_KEY);
		}

		if (getKey(WALL_IN_KEY)){
			shiftWalls(+1 * WALL_SHIFT_DELTA);
			clearKey(WALL_IN_KEY);
		}

		if (getKey(WALL_OUT_KEY)){
			shiftWalls(-1 * WALL_SHIFT_DELTA);
			clearKey(WALL_OUT_KEY);
		}
	}

	private void checkMouse(){
		if(getMouseButton(1)){
			if(justClicked){
				justClicked = false;

				double mouseX = (double) getMouseX();
				double mouseY = (double) getMouseY();
				double nearestDist = Double.MAX_VALUE;

				Assembly nearestAssembly = assemblyList.get(0);
				for (Assembly a : assemblyList){
					if(a.getNearestDist(nearestDist,mouseX,mouseY)< nearestDist){
						nearestDist = a.getNearestDist(nearestDist, mouseX, mouseY);
						nearestAssembly = a;
					}
				}

				mouseMass = new FixedMass("mClick", mouseX, mouseY);
				mouseSpring = new Spring(nearestAssembly.getNearestMass(), mouseMass,nearestDist, 1);
				forceList.add(mouseSpring);
				return;
			}
			
			mouseMass.setPos((double) getMouseX(), (double) getMouseY());
			return;
		}

		justClicked = true;
		if(mouseMass != null)
			mouseMass.remove();
		if(mouseSpring != null){
			forceList.remove(mouseSpring);
			mouseSpring.remove();
		}
	}
	
	public double getWallShift(){
		return wallShift;
	}
	
	private void shiftWalls(double amount){
		clearWalls();
		wallShift += amount;
		factory.addWalls();
	}

	private void clearWalls(){
		removeObjects("wall", 0);
	}

	private void changeMuscleAmplitudes(double amount){
		for (Force f : forceList){
			if (f.getForceName().equals("muscle")){
				Muscle m = (Muscle) f;
				m.changeAmplitude(amount);
			}
		}
	}

	private void clearAssemblies(){
		for (Assembly a : assemblyList)
			a.clearAssembly();
		assemblyList = new LinkedList<Assembly>();
		removeObjects("", 0);
		factory.addWalls();
	}

	private void calculateForces(){

		for (Force f : forceList)
			if (getForceStatus(f.getForceName()))
				for (Assembly a : assemblyList)
					f.applyForce(a);
	}

	private boolean getForceStatus(String forceName){
		for (int i=0; i<togglableForces.length; i++)
			if (forceName.equals(togglableForces[i]))
				return keyStatuses[i];
		return true;
	}

	@Override
	public void paintFrame ()
	{
		int y = (int) Factory.WALL_THICKNESS * 2;
		for (int i=0; i<keyStatuses.length; i++){
			drawString((char)toggleKeys[i] + ": " + Boolean.toString(keyStatuses[i]), 2 * Factory.WALL_THICKNESS, y, -1);
			y += FONT_HEIGHT;
		}
	}

	public static void main(String[] args) {
		Model sim = new Model();		
		
		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(sim, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
