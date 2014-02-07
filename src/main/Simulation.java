package main;

import input.AbstractSpringiesInput;
import input.XMLInput;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;

import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.WorldManager;
import jgame.JGColor;
import jgame.platform.JGEngine;
import simulation.Assembly;
import simulation.Force;
import simulation.Muscle;

@SuppressWarnings("serial")
public class Simulation extends JGEngine
{
	private static final int HEIGHT = 600;
	private static final double ASPECT = 4.0 / 3.0;
	private static final double WALL_MARGIN = 10;
	private static final double WALL_THICKNESS = 10;
	
	private final boolean DEFAULT_FORCE_STATUS = false;

	private static int[] toggleKeys = {KeyEvent.VK_G, KeyEvent.VK_V, KeyEvent.VK_M, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4};
	private static String[] togglableForces = {"Gravity", "Viscosity", "CoMForce", "WallForce1", "WallForce2", "WallForce3", "WallForce4"};
	private boolean[] keyStatuses = new boolean[toggleKeys.length];

	private static int NEW_ASSEMBLY_KEY = KeyEvent.VK_N;
	private static int CLEAR_KEY = KeyEvent.VK_C;
	private static int MUSCLE_PLUS_KEY = KeyEvent.VK_EQUALS;//Need Shift to get +
	private static int MUSCLE_MINUS_KEY = KeyEvent.VK_MINUS;
	private static int WALL_OUT_KEY = KeyEvent.VK_UP;
	private static int WALL_IN_KEY = KeyEvent.VK_DOWN;
	
	private static double MUSCLE_AMPLITUDE_CHANGE = 5.0;
	private double wallShift = 0.0;
	private static final double WALL_SHIFT_DELTA = 50.0;//Amount to shift walls in/nout per up/down keypress

	private List<Force> forceList;
	private List<Assembly> assemblyList;

	public Simulation ()
	{
		int height = HEIGHT;
		double aspect = ASPECT;
		initEngineComponent((int) (height * aspect), height);

		forceList = new LinkedList<Force>();
		assemblyList = new LinkedList<Assembly>();

		for (int i=0; i<keyStatuses.length; i++)
			keyStatuses[i] = DEFAULT_FORCE_STATUS;
	}

	@Override
	public void initCanvas ()
	{
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
	public void initGame ()
	{
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
		buildListsFromInput();

		addWalls();
	}

	public int getHeight(){
		return HEIGHT;
	}

	public int getWidth(){
		return (int) (HEIGHT*ASPECT);
	}

	/*
	 * Return displacement vector representing center of mass of all objects
	 */


	private void buildListsFromInput(){
		JFileChooser chooser = new JFileChooser("assets/");
		int response = chooser.showDialog(this, "Choose XML file to load");

		if (response == JFileChooser.APPROVE_OPTION){
			AbstractSpringiesInput newInput = new XMLInput(chooser.getSelectedFile().getPath(), this);
			newInput = new XMLInput(chooser.getSelectedFile().getPath(), this);
			newInput.readInput();
			forceList.addAll(newInput.getForces());
			assemblyList.add(new Assembly(newInput.getMasses()));
		}
	}

	private void addWalls ()
	{
		// add walls to bounce off of
		// NOTE: immovable objects must have no mass
		final double WALL_WIDTH = displayWidth() - WALL_MARGIN * 2 + WALL_THICKNESS;
		final double WALL_HEIGHT = displayHeight() - WALL_MARGIN * 2 + WALL_THICKNESS;
		
		PhysicalObject wall = new PhysicalObjectRect("wallTop", 2, JGColor.green, WALL_WIDTH, WALL_THICKNESS);
		wall.setPos(displayWidth() / 2, WALL_MARGIN + wallShift);
		
		wall = new PhysicalObjectRect("wallBottom", 2, JGColor.green, WALL_WIDTH, WALL_THICKNESS);
		wall.setPos(displayWidth() / 2, displayHeight() - WALL_MARGIN - wallShift);
		
		wall = new PhysicalObjectRect("wallLeft", 2, JGColor.green, WALL_THICKNESS, WALL_HEIGHT);
		wall.setPos(WALL_MARGIN + wallShift, displayHeight() / 2);
		
		wall = new PhysicalObjectRect("wallRight", 2, JGColor.green, WALL_THICKNESS, WALL_HEIGHT);
		wall.setPos(displayWidth() - WALL_MARGIN - wallShift, displayHeight() / 2);
	}

	@Override
	public void doFrame ()
	{
		// update game objects
		WorldManager.getWorld().step(1f, 1);
		calculateForces();
		moveObjects();
		checkCollision(2, 1);
		checkKeys();
	}

	private void checkKeys(){
		for (int i=0; i<toggleKeys.length; i++)
			if (getKey(toggleKeys[i])){
				keyStatuses[i] = !keyStatuses[i];
				clearKey(toggleKeys[i]);
			}

		if (getKey(NEW_ASSEMBLY_KEY)){
			buildListsFromInput();
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
	
	private void shiftWalls(double amount){
		clearWalls();
		wallShift += amount;
		addWalls();
	}
	
	private void clearWalls(){
		removeObjects("wall", 0);
	}
	
	private void changeMuscleAmplitudes(double amount){
		//Paint short message about muscle amplitudes changing
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
		addWalls();

	}

	private void calculateForces(){
		for (Force f : forceList){
			if (getForceStatus(f.getForceName())){
				//System.out.println(f.getForceName());
				for (Assembly a : assemblyList){
					f.applyForce(a);
				}
			}
		}
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
		int y = (int) WALL_THICKNESS * 2;
		for (int i=0; i<keyStatuses.length; i++){
			drawString((char)toggleKeys[i] + ": " + Boolean.toString(keyStatuses[i]), 2 * WALL_THICKNESS, y, -1);
			y += 20;//Extract constant at some point
		}
	}

}
