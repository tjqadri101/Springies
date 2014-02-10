package main;

import input.AbstractSpringiesInput;
import input.XMLInput;

import javax.swing.JFileChooser;

import jboxGlue.PhysicalObject;
import jboxGlue.PhysicalObjectRect;
import jgame.JGColor;

/*
 * Factory creates objects to be simulated in the Model
 */

public class Factory {
	public static final double WALL_MARGIN = 10;
	public static final double WALL_THICKNESS = 10;
	public static final int WALL_COLID = 2;
	
	private Model model;
	
	public Factory(Model model){
		this.model = model;
	}
	
	public void addWalls (){
		final double WALL_WIDTH = model.displayWidth() - WALL_MARGIN * 2 + WALL_THICKNESS;
		final double WALL_HEIGHT = model.displayHeight() - WALL_MARGIN * 2 + WALL_THICKNESS;

		PhysicalObject wall = new PhysicalObjectRect("wallTop", WALL_COLID, JGColor.green, WALL_WIDTH, WALL_THICKNESS);
		wall.setPos(model.displayWidth() / 2, WALL_MARGIN + model.getWallShift());

		wall = new PhysicalObjectRect("wallBottom", WALL_COLID, JGColor.green, WALL_WIDTH, WALL_THICKNESS);
		wall.setPos(model.displayWidth() / 2, model.displayHeight() - WALL_MARGIN - model.getWallShift());

		wall = new PhysicalObjectRect("wallLeft", WALL_COLID, JGColor.green, WALL_THICKNESS, WALL_HEIGHT);
		wall.setPos(WALL_MARGIN + model.getWallShift(), model.displayHeight() / 2);

		wall = new PhysicalObjectRect("wallRight", WALL_COLID, JGColor.green, WALL_THICKNESS, WALL_HEIGHT);
		wall.setPos(model.displayWidth() - WALL_MARGIN - model.getWallShift(), model.displayHeight() / 2);
	}
	
	public AbstractSpringiesInput buildListsFromInput(){
		JFileChooser chooser = new JFileChooser("assets/");
		int response = chooser.showDialog(model, "Choose XML file to load");

		if (response == JFileChooser.APPROVE_OPTION){
			AbstractSpringiesInput newInput = new XMLInput(chooser.getSelectedFile().getPath(), model);
			newInput = new XMLInput(chooser.getSelectedFile().getPath(), model);
			newInput.readInput();
			return newInput;
		}
		else
			return null;
	}
	
	
}
