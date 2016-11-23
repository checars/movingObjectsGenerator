package cn.edu.neu.mog.fileinput;

/**
 * A class representing an edge.
 * 
 * @author CheQingShou
 */
public class Edge {

	public final int nodeId1;

	public final int nodeId2;

	public final int edgeId;

	public final int edgeClass;

	/**
	 * Instantiate
	 * 
	 * @param nodeId1
	 * @param nodeId2
	 * @param edgeId
	 * @param edgeClass
	 */
	public Edge(int nodeId1, int nodeId2, int edgeId, int edgeClass) {
		this.nodeId1 = nodeId1;
		this.nodeId2 = nodeId2;
		this.edgeId = edgeId;
		this.edgeClass = edgeClass;
	}

}
