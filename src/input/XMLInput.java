package input;

import java.io.File;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jgobjects.FixedMass;
import jgobjects.Mass;
import jgobjects.Muscle;
import jgobjects.Spring;
import main.Model;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import forces.CoMForce;
import forces.Force;
import forces.Gravity;
import forces.Viscosity;
import forces.WallForce;

/*
 * Reads an XML file and creates Masses and Forces to populate the world
 */

public class XMLInput extends AbstractSpringiesInput{
	private String fileName;
	protected Model model;

	public XMLInput(String fileName, Model model){
		this.fileName = fileName;
		this.model = model;
	}

	public void readInput(){
		try {
			File file = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			//Initialize empty lists
			massList = new LinkedList<Mass>();
			forceList = new LinkedList<Force>();

			parseMasses(doc);
			parseFixedMasses(doc);
			parseSprings(doc);
			parseMuscles(doc);
			parseGravity(doc);
			parseViscosity(doc);
			parseCoMForce(doc);
			parseWallForces(doc);
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private void parseMasses(Document doc){
		NodeList massNodes = doc.getElementsByTagName("mass");
		for (int i=0; i<massNodes.getLength(); i++){
			NamedNodeMap attributes = massNodes.item(i).getAttributes();
			String id = attributes.getNamedItem("id").getNodeValue();
			double x = Double.parseDouble(attributes.getNamedItem("x").getNodeValue());
			double y = Double.parseDouble(attributes.getNamedItem("y").getNodeValue());
			double vx = attributes.getNamedItem("vx") == null ? DEFAULT_VX : Double.parseDouble(attributes.getNamedItem("vx").getNodeValue());
			double vy = attributes.getNamedItem("vy") == null ? DEFAULT_VY : Double.parseDouble(attributes.getNamedItem("vy").getNodeValue());
			double mass = attributes.getNamedItem("mass") == null ? DEFAULT_MASS : Double.parseDouble(attributes.getNamedItem("mass").getNodeValue());

			Mass toAdd = new Mass(id, x, y, vx, vy, mass);
			toAdd.setPos(x, y);
			massList.add(toAdd);
		}
	}
	
	private void parseFixedMasses(Document doc){
		NodeList fixedMassNodes = doc.getElementsByTagName("fixed");
		for (int i=0; i<fixedMassNodes.getLength(); i++){
			NamedNodeMap attributes = fixedMassNodes.item(i).getAttributes();
			String id = attributes.getNamedItem("id").getNodeValue();
			double x = Double.parseDouble(attributes.getNamedItem("x").getNodeValue());
			double y = Double.parseDouble(attributes.getNamedItem("y").getNodeValue());

			Mass toAdd = new FixedMass(id, x, y);
			toAdd.setPos(x, y);
			massList.add(toAdd);
		}
	}
	
	private void parseSprings(Document doc){
		NodeList forceNodes = doc.getElementsByTagName("spring");
		for (int i=0; i<forceNodes.getLength(); i++){
			NamedNodeMap attributes = forceNodes.item(i).getAttributes();
			String aId = attributes.getNamedItem("a").getNodeValue();
			String bId = attributes.getNamedItem("b").getNodeValue();
			double restlength = attributes.getNamedItem("restlength") == null ? DEFAULT_RESTLENGTH : Double.parseDouble(attributes.getNamedItem("restlength").getNodeValue());
			double constant = attributes.getNamedItem("constant") == null ? DEFAULT_SPRING_CONSTANT : Double.parseDouble(attributes.getNamedItem("constant").getNodeValue());

			//Find endpoint masses
			Mass a = null, b = null;
			for (int j=0; j<massList.size(); j++){
				if (massList.get(j).getName().equals(aId))
					a = massList.get(j);
				if (massList.get(j).getName().equals(bId))
					b = massList.get(j);
			}

			if (a == null || b == null){
				System.err.println("Mass endpoints of spring not defined");
				System.err.println("Unable to find " + aId + ", " + bId);
			}

			Force toAdd = new Spring(a, b, restlength, constant);
			forceList.add(toAdd);
		}
	}
	
	private void parseMuscles(Document doc){
		NodeList muscleNodes = doc.getElementsByTagName("muscle");
		for (int i=0; i<muscleNodes.getLength(); i++){
			NamedNodeMap attributes = muscleNodes.item(i).getAttributes();
			String aId = attributes.getNamedItem("a").getNodeValue();
			String bId = attributes.getNamedItem("b").getNodeValue();
			double restlength = attributes.getNamedItem("restlength") == null ? DEFAULT_RESTLENGTH : Double.parseDouble(attributes.getNamedItem("restlength").getNodeValue());
			double constant = attributes.getNamedItem("constant") == null ? DEFAULT_SPRING_CONSTANT : Double.parseDouble(attributes.getNamedItem("constant").getNodeValue());
			double amplitude = attributes.getNamedItem("amplitude") == null ? DEFAULT_MUSCLE_AMPLITUDE : Double.parseDouble(attributes.getNamedItem("amplitude").getNodeValue());

			//Find endpoint masses
			Mass a = null, b = null;
			for (int j=0; j<massList.size(); j++){
				if (massList.get(j).getName().equals(aId))
					a = massList.get(j);
				if (massList.get(j).getName().equals(bId))
					b = massList.get(j);
			}

			if (a == null || b == null)
				System.err.println("Mass endpoints of muscle not defined");

			Force toAdd = new Muscle(a, b, restlength, constant, amplitude);
			forceList.add(toAdd);
		}
	}
	
	private void parseGravity(Document doc){
		NodeList gravityNodes = doc.getElementsByTagName("gravity");
		for (int i=0; i<gravityNodes.getLength(); i++){
			NamedNodeMap attributes = gravityNodes.item(i).getAttributes();
			double direction = attributes.getNamedItem("direction") == null ? DEFAULT_GRAVITY_DIRECTION : Double.parseDouble(attributes.getNamedItem("direction").getNodeValue());
			double magnitude = attributes.getNamedItem("magnitude") == null ? DEFAULT_GRAVITY_MAGNITUDE : Double.parseDouble(attributes.getNamedItem("magnitude").getNodeValue());

			Force toAdd = new Gravity(direction, magnitude);
			forceList.add(toAdd);
		}
	}
	
	private void parseViscosity(Document doc){
		NodeList viscosityNodes = doc.getElementsByTagName("viscosity");
		for (int i=0; i<viscosityNodes.getLength(); i++){
			NamedNodeMap attributes = viscosityNodes.item(i).getAttributes();
			double magnitude = attributes.getNamedItem("magnitude") == null ? DEFAULT_VISCOSITY_MAGNITUDE : Double.parseDouble(attributes.getNamedItem("magnitude").getNodeValue());

			Force toAdd = new Viscosity(magnitude);
			forceList.add(toAdd);
		}
	}
	
	private void parseCoMForce(Document doc){
		NodeList comNodes = doc.getElementsByTagName("centermass");
		for (int i=0; i<comNodes.getLength(); i++){
			NamedNodeMap attributes = comNodes.item(i).getAttributes();
			double magnitude = Double.parseDouble(attributes.getNamedItem("magnitude").getNodeValue());
			double exponent = Double.parseDouble(attributes.getNamedItem("exponent").getNodeValue());

			Force toAdd = new CoMForce(magnitude, exponent);
			forceList.add(toAdd);
		}
	}
	
	private void parseWallForces(Document doc){
		NodeList wallNodes = doc.getElementsByTagName("wall");
		for (int i=0; i<wallNodes.getLength(); i++){
			NamedNodeMap attributes = wallNodes.item(i).getAttributes();
			int id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
			double magnitude = Double.parseDouble(attributes.getNamedItem("magnitude").getNodeValue());
			double exponent = Double.parseDouble(attributes.getNamedItem("exponent").getNodeValue());

			Force toAdd = new WallForce(id, magnitude, exponent, model);
			forceList.add(toAdd);
		}
	}
}
