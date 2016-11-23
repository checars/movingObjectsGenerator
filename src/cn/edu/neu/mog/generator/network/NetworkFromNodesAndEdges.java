package cn.edu.neu.mog.generator.network;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * A network is represented by some nodes and edges. It is instantiated (or
 * loaded) from a node- and an edge file.
 * 
 * @author CheQingShou
 */
public class NetworkFromNodesAndEdges implements Network {

	private Node[] nodes;

	/**
	 * Bounding box in physical coordinates [m].
	 */
	private BoundingBox boundingBox;

	/**
	 * Instantiate network from given base path.
	 * 
	 * @param basePath
	 * @throws IOException
	 */
	public NetworkFromNodesAndEdges(String basePath) throws IOException {
		this.boundingBox = null;

		// input files
		File nodeFile = new File(basePath + ".node");
		File edgeFile = new File(basePath + ".edge");
		System.out.println("Node file: " + nodeFile.getName() + ", edge file: " + edgeFile.getName());
		System.out.println();

		// number of nodes and edges
		// (assuming there are no names stored)
		final int numNodes = (int) (nodeFile.length() / 17L);
		final int numEdges = (int) (edgeFile.length() / 29L);
		System.out.print("Number of nodes: " + numNodes + ", number of edges: " + numEdges);
		System.out.println(" (assuming there are no names stored)");
		System.out.println();

		// read node file and
		// create list of nodes
		nodes = new NodeWithEdges[numNodes];
		long begin1 = System.nanoTime();
		int count = 0;
		FileChannel channel = new RandomAccessFile(nodeFile, "r").getChannel();
		ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0L, nodeFile.length());
		while (buffer.remaining() > 0) {
			byte nameLength = buffer.get();
			if (nameLength > 0) {
				throw new RuntimeException("No names allowed in nodes!");
			}
			int nodeId = (int) buffer.getLong();
			int x = buffer.getInt();
			int y = buffer.getInt();
			Node node = new NodeWithEdges(x, y);
			if (nodeId != count) {
				throw new RuntimeException("No dense packed node file!");
			}
			nodes[count] = node;
			count++;
		}
		channel.close();
		long end1 = System.nanoTime();
		System.out.println("Time to create nodes (" + count + "): " + (end1 - begin1) / 1e9 + "s.");

		// read edge file
		long begin2 = System.nanoTime();
		count = 0;
		channel = new RandomAccessFile(edgeFile, "r").getChannel();
		buffer = channel.map(MapMode.READ_ONLY, 0L, edgeFile.length());
		while (buffer.remaining() > 0) {
			int nodeId1 = (int) buffer.getLong();
			int nodeId2 = (int) buffer.getLong();
			byte nameLength = buffer.get();
			if (nameLength > 0) {
				throw new RuntimeException("No names allowed in edges!");
			}
			buffer.getLong(); // id
			int edgeClass = buffer.getInt();
			Node node1 = nodes[nodeId1];
			Node node2 = nodes[nodeId2];
			if (node1 == null || node2 == null) {
				throw new RuntimeException("Inconsistent node- and edge files!");
			}
			Edge edge = new Edge(node1, node2, edgeClass);

			// store edge in node
			node1.addEdge(edge);
			node2.addEdge(edge);
			count++;
		}
		channel.close();
		buffer = null;
		channel = null;
		long end2 = System.nanoTime();
		System.out.println("Time to create edges (" + count + "): " + (end2 - begin2) / 1e9 + "s.");

		System.out.println("Total time to create network: " + (end2 - begin1) / 1e9 + "s.");
		System.out.println();
	}

	/**
	 * @return number of nodes in network.
	 */
	public int size() {
		return nodes.length;
	}

	/**
	 * @param index
	 * @return node with given index.
	 */
	public Node getNode(int index) {
		return nodes[index];
	}

	/**
	 * @return bounding box around all nodes of this network. in physical
	 *         coordinates [m].
	 */
	public BoundingBox getBoundingBox() {
		if (boundingBox == null) {
			boundingBox = new BoundingBox();
			for (Node node : nodes) {
				boundingBox.encloseNode(node);
			}
			boundingBox.updateDerivedValues();
		}
		return boundingBox;
	}

}
