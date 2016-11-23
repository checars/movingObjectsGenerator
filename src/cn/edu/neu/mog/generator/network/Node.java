package cn.edu.neu.mog.generator.network;

/**
 * A node of a network.
 * 
 * @author CheQingShou
 */
public interface Node {

	/**
	 * @return physical x-coordinate of the node. [m]
	 */
	int getX();

	/**
	 * @return physical y-coordinate of the node. [m]
	 */
	int getY();

	/**
	 * Attach an edge to this node.
	 * 
	 * @param edge
	 */
	void addEdge(Edge edge);

	/**
	 * @return edges attached to this node.
	 */
	Edge[] getEdges();

}
