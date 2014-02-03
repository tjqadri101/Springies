package main;

import input.AbstractSpringiesInput;
import input.XMLInput;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;

import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectRect;
import jboxGlue.WorldManager;
import jgame.JGColor;
import jgame.platform.JGEngine;

import org.jbox2d.common.Vec2;

import simulation.Force;
import simulation.Mass;

@SuppressWarnings("serial")
public class Simulation extends JGEngine
{
	private static final int HEIGHT = 600;
	private static final double ASPECT = 4.0 / 3.0;
	private static final double FORCE_FUDGE = 1000;//Make the simulation run faster by making all forces stronger

	private List<AbstractSpringiesInput> inputs;

	public Simulation ()
	{
		// set the window size
		int height = HEIGHT;
		double aspect = ASPECT;
		initEngineComponent((int) (height * aspect), height);
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
		//WorldManager.getWorld().setGravity(new Vec2(0.0f, .05f));
		//input = new XMLInput("assets/daintywalker.xml");

		inputs = new LinkedList<AbstractSpringiesInput>();
		JFileChooser chooser = new JFileChooser("assets/");
		int response = JFileChooser.CANCEL_OPTION;
		while (response != JFileChooser.APPROVE_OPTION){
			response = chooser.showDialog(this, "Choose first XML file");
		}

		AbstractSpringiesInput newInput = new XMLInput(chooser.getSelectedFile().getPath());
		newInput.readInput();
		inputs.add(newInput);
		
		response = JFileChooser.CANCEL_OPTION;
		while (response != JFileChooser.APPROVE_OPTION){
			response = chooser.showDialog(this, "Choose second XML file");
		}
		
		newInput = new XMLInput(chooser.getSelectedFile().getPath());
		newInput.readInput();
		inputs.add(newInput);

		/*Mass test = new Mass("aoeu", 500, 500, 2, 4, 1);
		test.setPos(displayWidth() / 2, displayHeight() / 2);
		test.setForce(8000, -10000);*/
		addWalls();
		//JGObject test = new VirtualObjectRect("testrect", 4, JGColor.gray, 10, 10, this);
		//test.setPos(200, 200);
	}

	public void inputXML(){
		JFileChooser chooser = new JFileChooser("assets/");
		chooser.showOpenDialog(this);

		//System.out.println(chooser.getSelectedFile().getPath());
		AbstractSpringiesInput newInput = new XMLInput(chooser.getSelectedFile().getPath());
		newInput.readInput();
		inputs.add(newInput);
	}

	private void addWalls ()
	{
		// add walls to bounce off of
		// NOTE: immovable objects must have no mass
		final double WALL_MARGIN = 10;
		final double WALL_THICKNESS = 10;
		final double WALL_WIDTH = displayWidth() - WALL_MARGIN * 2 + WALL_THICKNESS;
		final double WALL_HEIGHT = displayHeight() - WALL_MARGIN * 2 + WALL_THICKNESS;
		PhysicalObject wall = new PhysicalObjectRect("topwall", 2, JGColor.green,
				WALL_WIDTH, WALL_THICKNESS);
		wall.setPos(displayWidth() / 2, WALL_MARGIN);
		wall = new PhysicalObjectRect("bottomwall", 2, JGColor.green,
				WALL_WIDTH, WALL_THICKNESS);
		wall.setPos(displayWidth() / 2, displayHeight() - WALL_MARGIN);
		wall = new PhysicalObjectRect("leftwall", 2, JGColor.green,
				WALL_THICKNESS, WALL_HEIGHT);
		wall.setPos(WALL_MARGIN, displayHeight() / 2);
		wall = new PhysicalObjectRect("rightwall", 2, JGColor.green,
				WALL_THICKNESS, WALL_HEIGHT);
		wall.setPos(displayWidth() - WALL_MARGIN, displayHeight() / 2);
	}

	@Override
	public void doFrame ()
	{
		// update game objects
		WorldManager.getWorld().step(1f, 1);
		calculateForces();
		moveObjects();
		checkCollision(2, 1);
	}

	private void calculateForces(){
		for (AbstractSpringiesInput input : inputs){
			for (Force f : input.getForces()){
				for (Mass m : input.getMasses()){
					Vec2 force = f.calculateForce(m);
					m.setForce(force.x * FORCE_FUDGE, force.y * FORCE_FUDGE);
				}
			}
		}
	}

	@Override
	public void paintFrame ()
	{
		// nothing to do
		// the objects paint themselves
	}

}
