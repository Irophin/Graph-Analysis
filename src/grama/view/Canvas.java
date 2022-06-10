package grama.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import grama.model.Link;
import grama.model.Node;
import java.awt.BasicStroke;

/**
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public class Canvas extends JPanel {
	
	private Graphics2D graphic;
	
	private List<Node> nodesList = new ArrayList<>();
	private List<Node> nodesDisplay = new ArrayList<>();
	private List<Link> linksDisplay = new ArrayList<>();
	private Map<String, Point> positions = new HashMap<>();
	
	private Object hover = null;
	private Node[] selected = new Node[2];
	
	public Canvas() {
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Object PreviousHover = hover;
				
				hover = getNode(e.getPoint());
				if (hover == null)
					hover = getLink(e.getPoint());
				
				if (PreviousHover != hover){
					repaint();
				}
			}
		});
	}
			
	@Override
	public void paint(Graphics g){
		graphic = (Graphics2D)g;
		super.paint(graphic);
		
		setNodesLocation();
		
		for(Link link: linksDisplay) {
			drawLink(link);
		}
		
		for(Node node: nodesDisplay) {
			drawNode(node);
		}
	}
	
	public void drawLink(Link link){
		Point coords = positions.get(link.getDeparture().getName());

		Point destination = positions.get(link.getDestination().getName());
		graphic.setColor(link.getType().getColor());

		graphic.setStroke(new BasicStroke(2));
		graphic.drawLine(coords.x, coords.y, destination.x, destination.y);
		graphic.setStroke(new BasicStroke(1));

		Point center = new Point((coords.x + destination.x)/2 , (coords.y + destination.y)/2);
		if (link == hover)
			graphic.setFont(new Font("sans serif", Font.BOLD, 12));
		else
			graphic.setFont(new Font("sans serif", Font.PLAIN, 12));

		graphic.setColor(Color.BLACK);
		String info = Integer.toString(link.getDistance());
		graphic.drawString(info, center.x - graphic.getFontMetrics().getDescent()*info.length()/2, center.y-10);

		if (link == hover)
			graphic.setFont(new Font("sans serif", Font.PLAIN, 12));
		
	}
	
	public void drawNode(Node node){
		Point coords = positions.get(node.getName());
		graphic.setColor(node.getType().getColor());
		
		if (node == hover)
			graphic.drawImage(node.getType().getImage(), coords.x - 20, coords.y - 20, 40, 40, null);
		else
			graphic.drawImage(node.getType().getImage(), coords.x - 15, coords.y - 15, 30, 30, null);
		
		graphic.setColor(Color.BLACK);
		if (selected[0] == node || selected[1] == node) {
			if (node == hover)
				graphic.drawOval(coords.x - 20,coords.y - 20, 40, 40);
			else
				graphic.drawOval(coords.x - 15,coords.y - 15, 30, 30);
		}
		
		graphic.setFont(new Font("sans serif", Font.PLAIN, 12));
		graphic.drawString(node.getName(), coords.x - graphic.getFontMetrics().getDescent()*node.getName().length(), coords.y-15);
	}
	
	public Node getNode(Point pos){
		for(Node node : nodesDisplay){
			Point coords = positions.get(node.getName());
			if (coords.distance(pos.x, pos.y) < 20){
				return node;
			}
		}
		return null;
	}
	
	public Link getLink(Point pos){
		for(Link link : linksDisplay){
			
			Point departure = positions.get(link.getDeparture().getName());
			Point destination = positions.get(link.getDestination().getName());
			
			Point center = new Point((departure.x + destination.x)/2 , (departure.y + destination.y)/2);
			if (center.distance(pos.x, pos.y) < 30){
				return link;
			}
		}
		return null;
	}	
	
	public void initNodes(List<Node> nodes){
		nodesList = nodes;
		setNodesLocation();
	}
	
	public void setNodesLocation(){
		for (Node node : nodesList){
			Point center = new Point( (int)(getWidth() * node.getRatioX()) , (int)(getHeight() * node.getRatioY()));
			positions.put(node.getName(), center);
		}
	}
	
	public void setDisplay(List<Node> nodes, List<Link> links) {
		nodesDisplay = nodes;
		linksDisplay = links;
		repaint();
	}
	
	public void setDisplayNodes(List<Node> nodes){
		nodesDisplay = nodes;
		repaint();
	}
	
	public void setDisplayLinks(List<Link> links){
		linksDisplay = links;
		repaint();
	}

	public Object getHover() {
		return hover;
	}
	
	public void reset() {
		nodesList.clear();
		nodesDisplay.clear();
		linksDisplay.clear();
		positions.clear();
		resetSelected();
		repaint();
	}
	
	public void resetSelected() {
		selected[0] = null;
		selected[1] = null;
	}
	
	public void addSelected(int i, Node node) {
		selected[i] = node;
		repaint();
	}
	
}