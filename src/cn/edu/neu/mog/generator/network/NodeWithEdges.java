package cn.edu.neu.mog.generator.network;

/**
 * A node in the network of streets.
 * 
 * @author CheQingShou
 */
public class NodeWithEdges implements Node {

	/**
	 * The physical x-coordinate of the node. [m]
	 */
	private int x;

	/**
	 * The physical y-coordinate of the node. [m]
	 */
	private int y;

	/**
	 * The list of edges of this node.
	 */
	private Edge[] edges;

	/**
	 * Instantiate.
	 * 
	 * @param x physical x-coordinate of the node. [m]
	 * @param y physical y-coordinate of the node. [m]
	 */
	public NodeWithEdges(int x, int y) {
		this.x = x;
		this.y = y;
		this.edges = null;
	}

	public void addEdge(Edge edge) {
		final int oldEdgeCount = (edges == null ? 0 : edges.length);
		Edge[] newEdges = new Edge[oldEdgeCount + 1];
		for (int i = 0; i < oldEdgeCount; i++) {
			newEdges[i] = edges[i];
		}
		newEdges[oldEdgeCount] = edge;
		edges = newEdges;
	}

	/**
	 * @return physical x-coordinate of the node. [m]
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return physical y-coordinate of the node. [m]
	 */
	public int getY() {
		return y;
	}

	public Edge[] getEdges() {
		return edges;
	}

	@Override
	public String toString() {
		return x + "," + y + " #edges=" + (edges == null ? 0 : edges.length);
	}

}
