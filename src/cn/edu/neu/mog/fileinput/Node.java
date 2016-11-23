package cn.edu.neu.mog.fileinput;

/**
 * A class representing a node.
 * 
 * @author CheQingShou
 */
public class Node {

	public int nodeId;

	public double x;

	public double y;

	/**
	 * Instantiate.
	 * 
	 * @param nodeId
	 * @param x
	 * @param y
	 */
	public Node(int nodeId, double x, double y) {
		this.nodeId = nodeId;
		this.x = x;
		this.y = y;
	}

}
