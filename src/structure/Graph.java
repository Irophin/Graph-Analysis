package structure;

import java.util.HashMap;
import java.util.List;

/**
 * The class representing the Graph
 * @author VAILLON Albert
 */
public class Graph {
	
	private HashMap<String, Node> nodeList = new HashMap<>();
	
	/**
	 * @return Returns the list of all the Nodes from the Graph
	 */
	public HashMap<String, Node> getNodeList() {
		return nodeList;
	}
	
	/**
	 * Displays the Graph in a format close to the source file's
	 */
	public void display() {
		if (nodeList.isEmpty()) {
			System.out.println("The graph is empty");
		} else {
			for (Node node: nodeList.values()) {
				System.out.printf("%-22s=>%-4s", node, "");
				
				List<Link> nodeNeighbors = node.getNodeLinks();
				for (Link link: nodeNeighbors) {
					System.out.printf("%-40s\t", link);
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * @param type The type of Nodes we will count
	 * @return Returns the number of Nodes with the right type
	 */
	public int getNumberNodeType(NodeType type) {
		int cpt = 0;
		for (Node node: nodeList.values()) {
			if (node.getType() == type) {
				cpt++;
			}
		}
		return cpt;
	}
	
}