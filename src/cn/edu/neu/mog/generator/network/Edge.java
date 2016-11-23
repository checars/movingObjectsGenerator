package cn.edu.neu.mog.generator.network;

/**
 * An edge in the network of streets.
 * 
 * @author CheQingShou
 */
public class Edge {

	/**
	 * Pointer to first node.
	 */
	public Node node1;

	/**
	 * Pointer to second node.
	 */
	public Node node2;

	/**
	 * The class of this edge. 0 is the best, larger values are worse.
	 */
	public int edgeClass;

	/**
	 * Instantiate.
	 * 
	 * @param node1
	 * @param node2
	 * @param edgeClass
	 */
	public Edge(Node node1, Node node2, int edgeClass) {
		this.node1 = node1;
		this.node2 = node2;
		this.edgeClass = edgeClass;
	}

	@Override
	public String toString() {
		return node1 + ";<" + edgeClass + ">;" + node2;
	}

	/**
	 * Calculate length of this edge.
	 * 
	 * @return length of this edge. [m]
	 */
	public double getLength() {
		int dx = node2.getX() - node1.getX();
		int dy = node2.getY() - node1.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Get node which is on the opposite side of the given node.
	 * 
	 * @param node
	 * @return
	 */
	public Node getOppositeNode(Node node) {
		return node == node1 ? node2 : node1;
	}

}
