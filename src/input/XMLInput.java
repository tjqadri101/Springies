package input;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import main.Simulation;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import simulation.CoMForce;
import simulation.FixedMass;
import simulation.Force;
import simulation.Gravity;
import simulation.Mass;
import simulation.Muscle;
import simulation.Spring;
import simulation.Viscosity;

/*
 * Reads an XML file and creates Masses and Forces to populate the world
 */

public class XMLInput extends AbstractSpringiesInput{
	private String fileName;
	private List<Mass> massList;
	private List<Force> forceList;
	private Simulation simulation;

	public XMLInput(String fileName, Simulation simulation){
		this.fileName = fileName;
		this.simulation = simulation;
	}

	public List<Mass> getMasses(){
		return massList;
	}

	public List<Force> getForces(){
		return forceList;
	}

	public void readInput(){
		try {
			File file = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			//Prep lists
			massList = new LinkedList<Mass>();
			forceList = new LinkedList<Force>();

			//Parse masses
			NodeList massNodes = doc.getElementsByTagName("mass");
			for (int i=0; i<massNodes.getLength(); i++){
				NamedNodeMap attributes = massNodes.item(i).getAttributes();
				String id = attributes.getNamedItem("id").getNodeValue();
				double x = Double.parseDouble(attributes.getNamedItem("x").getNodeValue());
				double y = Double.parseDouble(attributes.getNamedItem("y").getNodeValue());
				double vx = attributes.getNamedItem("vx") == null ? 0 : Double.parseDouble(attributes.getNamedItem("vx").getNodeValue());
				double vy = attributes.getNamedItem("vy") == null ? 0 : Double.parseDouble(attributes.getNamedItem("vy").getNodeValue());
				double mass = attributes.getNamedItem("mass") == null ? 1 : Double.parseDouble(attributes.getNamedItem("mass").getNodeValue());

				Mass toAdd = new Mass(id, x, y, vx, vy, mass);
				toAdd.setPos(x, y);
				massList.add(toAdd);
			}
			
			//Parse fixed masses
			NodeList fixedMassNodes = doc.getElementsByTagName("fixed");
			for (int i=0; i<fixedMassNodes.getLength(); i++){
				NamedNodeMap attributes = fixedMassNodes.item(i).getAttributes();
				String id = attributes.getNamedItem("id").getNodeValue();
				double x = Double.parseDouble(attributes.getNamedItem("x").getNodeValue());
				double y = Double.parseDouble(attributes.getNamedItem("y").getNodeValue());

				FixedMass toAdd = new FixedMass(id, x, y);
				toAdd.setPos(x, y);
				massList.add(toAdd);
			}
			
			//Parse springs
			NodeList forceNodes = doc.getElementsByTagName("spring");
			for (int i=0; i<forceNodes.getLength(); i++){
				NamedNodeMap attributes = forceNodes.item(i).getAttributes();
				String aId = attributes.getNamedItem("a").getNodeValue();
				String bId = attributes.getNamedItem("b").getNodeValue();
				double restlength = attributes.getNamedItem("restlength") == null ? 50 : Double.parseDouble(attributes.getNamedItem("restlength").getNodeValue());
				double constant = attributes.getNamedItem("constant") == null ? 1 : Double.parseDouble(attributes.getNamedItem("constant").getNodeValue());
				
				//Find endpoint masses
				Mass a = null, b = null;
				for (int j=0; j<massNodes.getLength(); j++){
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
			
			//Parse muscles
			NodeList muscleNodes = doc.getElementsByTagName("muscle");
			for (int i=0; i<muscleNodes.getLength(); i++){
				NamedNodeMap attributes = muscleNodes.item(i).getAttributes();
				String aId = attributes.getNamedItem("a").getNodeValue();
				String bId = attributes.getNamedItem("b").getNodeValue();
				double restlength = attributes.getNamedItem("restlength") == null ? 50 : Double.parseDouble(attributes.getNamedItem("restlength").getNodeValue());
				double constant = attributes.getNamedItem("constant") == null ? 1 : Double.parseDouble(attributes.getNamedItem("constant").getNodeValue());
				double amplitude = attributes.getNamedItem("amplitude") == null ? 1 : Double.parseDouble(attributes.getNamedItem("amplitude").getNodeValue());
				
				//Find endpoint masses
				Mass a = null, b = null;
				for (int j=0; j<massNodes.getLength(); j++){
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
			
			
			//Build Gravity
			NodeList gravityNodes = doc.getElementsByTagName("gravity");
			for (int i=0; i<gravityNodes.getLength(); i++){
				NamedNodeMap attributes = gravityNodes.item(i).getAttributes();
				double direction = attributes.getNamedItem("direction") == null ? 90 : Double.parseDouble(attributes.getNamedItem("direction").getNodeValue());
				double magnitude = attributes.getNamedItem("magnitude") == null ? .005 : Double.parseDouble(attributes.getNamedItem("magnitude").getNodeValue());
				
				Force toAdd = new Gravity(direction, magnitude);
				forceList.add(toAdd);
			}
			
			//Build Viscosity
			NodeList viscosityNodes = doc.getElementsByTagName("viscosity");
			for (int i=0; i<viscosityNodes.getLength(); i++){
				NamedNodeMap attributes = viscosityNodes.item(i).getAttributes();
				double magnitude = attributes.getNamedItem("magnitude") == null ? 1 : Double.parseDouble(attributes.getNamedItem("magnitude").getNodeValue());
				
				Force toAdd = new Viscosity(magnitude);
				forceList.add(toAdd);
			}
			
			//Build CoM force
			NodeList comNodes = doc.getElementsByTagName("centermass");
			for (int i=0; i<comNodes.getLength(); i++){
				NamedNodeMap attributes = comNodes.item(i).getAttributes();
				double magnitude = Double.parseDouble(attributes.getNamedItem("magnitude").getNodeValue());
				double exponent = Double.parseDouble(attributes.getNamedItem("exponent").getNodeValue());
				
				Force toAdd = new CoMForce(magnitude, exponent, simulation);
				forceList.add(toAdd);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
