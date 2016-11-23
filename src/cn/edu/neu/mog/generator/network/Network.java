package cn.edu.neu.mog.generator.network;

/**
 * A network consists of nodes and edges.
 * 
 * @author CheQingShou
 */
public interface Network {

	/**
	 * @return number of nodes in network.
	 */
	int size();

	/**
	 * @param index
	 * @return node with given index.
	 */
	Node getNode(int index);

	/**
	 * @return bounding box around all nodes of this network.in physical
	 *         coordinates [m].
	 */
	BoundingBox getBoundingBox();

}
