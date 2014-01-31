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
import simulation.FixedMass;
import simulation.Mass;
import simulation.Spring;

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
		return massList;
	}

	public List<AbstractForce> getForces(){
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
			
			//Parse forces
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
				
				if (a == null || b == null)
					System.err.println("Mass endpoints of spring not defined");

				AbstractForce toAdd = new Spring(a, b, restlength, constant);
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
				double amplitude = attributes.getNamedItem("amplitude") == null ? 1 : Double.parseDouble(attributes.getNamedItem("constant").getNodeValue());
				
				//Find endpoint masses
				Mass a = null, b = null;
				for (int j=0; j<massNodes.getLength(); j++){
					if (massList.get(j).getName().equals(aId))
						a = massList.get(j);
					if (massList.get(j).getName().equals(bId))
						b = massList.get(j);
				}
				
				if (a == null || b == null)
					System.err.println("Mass endpoints of spring not defined");

				AbstractForce toAdd = new Spring(a, b, restlength, constant);
				forceList.add(toAdd);
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
