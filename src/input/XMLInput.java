package input;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import simulation.AbstractForce;
import simulation.Mass;

/*
 * Reads an XML file and creates Masses and Forces to populate the world
 */


public class XMLInput extends AbstractSpringiesInput{
	private String fileName;
	private List<Mass> massList;
	private List<AbstractForce> forceList;

	public XMLInput(String fileName){
		this.fileName = fileName;
	}

	public List<Mass> getMasses(){
		return null;
	}

	public List<AbstractForce> getForces(){
		return null;
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
			forceList = new LinkedList<AbstractForce>();

			//Parse masses
			NodeList massNodes = doc.getElementsByTagName("mass");
			for (int i=0; i<massNodes.getLength(); i++){
				NamedNodeMap attributes = massNodes.item(i).getAttributes();
				String id = attributes.getNamedItem("id").getNodeValue();
				double x = Double.parseDouble(attributes.getNamedItem("x").getNodeValue());
				double y = Double.parseDouble(attributes.getNamedItem("y").getNodeValue());
				double vx = attributes.getNamedItem("vx") == null ? 0 : Double.parseDouble(attributes.getNamedItem("vx").getNodeValue());
				double vy = attributes.getNamedItem("vy") == null ? 0 : Double.parseDouble(attributes.getNamedItem("vy").getNodeValue());
				double mass = attributes.getNamedItem("mass") == null ? 0 : Double.parseDouble(attributes.getNamedItem("mass").getNodeValue());

				Mass toAdd = new Mass(id, x, y, vx, vy, mass);
				toAdd.setPos(x, y);
				System.out.println(x + " " + y);
				massList.add(toAdd);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
