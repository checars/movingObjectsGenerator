package cn.edu.neu.mog.generator;

import cn.edu.neu.mog.generator.network.Edge;
import cn.edu.neu.mog.generator.network.Node;
import cn.edu.neu.mog.generator.scenarios.Scenario;

/**
 * An object moving on the network of streets.
 * 
 * @author CheQingShou
 */
public class MovingObject {

	/**
	 * The edge on which this object is moving currently.
	 */
	public Edge edge;

	/**
	 * True to indicate moving from edge.node1 to edge.node2.
	 */
	public boolean forward;

	/**
	 * The relative position on the edge. between 0.0 (including) and 1.0
	 * (excluding)
	 */
	public double position;

	/**
	 * Instantiate.
	 * 
	 * @param edge
	 * @param forward
	 * @param position
	 */
	public MovingObject(Edge edge, boolean forward, double position) {
		this.edge = edge;
		this.forward = forward;
		this.position = position;
	}

	public void move(Scenario scenario) {

		// the time we may still move
		// initialized to 1 timestamp
		// (we assume 1 timestamp equals 1 s)
		double remainingTime = 1.0;

		// move along edges until next edge is not reached anymore
		// in each iteration the edge changes.
		// therefore all edge related calculations are done inside the loop.
		while (true) {

			// speed on current edge (between 0 and scenario.speedMaxMs) [m/s]
//			System.out.println("edgeClass========>"+edge.edgeClass);
			double edgeClass = 1.0 + edge.edgeClass;
			double speedOnEdge = scenario.speedMaxMs / edgeClass;

			// edge length [m]
			double edgeLength = edge.getLength();

			// current position on edge [m]
			double currentPositionOnEdge = edgeLength * position;

			// how far we can travel [m]
			double maxDistOnEdge = remainingTime * speedOnEdge;

			// switch on wheather node is reached or not
			if ((remainingTime <= 0.0) || (currentPositionOnEdge + maxDistOnEdge < edgeLength)) {

				// next node IS NOT reached
				currentPositionOnEdge += maxDistOnEdge;
				position = currentPositionOnEdge / edgeLength;
				return;

			} else {

				// next node IS reached

				// to calculate the remaining time,
				// we take the distance which we moved [in logical space units]
				// and divide it by the logical speed on the current edge
				// e.g.
				// on a heighway (edgeClass = 1.0) we can travel with the
				// maximum logical speed
				double distMoved = edgeLength - currentPositionOnEdge;
				double usedTime = distMoved / speedOnEdge;
				if (usedTime < 0.01) {
					usedTime = 0.01;
				}
				remainingTime -= usedTime;

				// update members
				Node prevNode = forward ? edge.node1 : edge.node2;
				Node curNode = forward ? edge.node2 : edge.node1;
				edge = getNextEdge(edge, prevNode, curNode);
				position = 0.0;
				forward = curNode == edge.node1 ? true : false;
			}
		}
	}

	/**
	 * Calculate the next edge. We try to keep the direction from which we came
	 * from.
	 * 
	 * @param comingFrom
	 * @param prevNode
	 * @param curNode
	 * @return next edge.
	 */
	private Edge getNextEdge(Edge comingFrom, Node prevNode, Node curNode) {

		// previous direction
		int dx = curNode.getX() - prevNode.getX();
		int dy = curNode.getY() - prevNode.getY();

		// loop over all edges
		int bestIndex = 0;
		Edge[] edges = curNode.getEdges();
		for (int i = 0; i < edges.length; i++) {
			Edge edge = edges[i];

			// ignore edge where we are coming from
			if (edge.equals(comingFrom)) {
				continue;
			}

			// look at direction of this edge
			Node nextNode = edge.getOppositeNode(curNode);
			int curDx = nextNode.getX() - curNode.getX();
			int curDy = nextNode.getY() - curNode.getY();

			Node nextNodeBest = edges[bestIndex].getOppositeNode(curNode);
			int curBestDx = nextNodeBest.getX() - curNode.getX();
			int curBestDy = nextNodeBest.getY() - curNode.getY();

			int bestSum = (((curBestDx < 0 && dx < 0) || (curBestDx > 0 && dx > 0)) ? 1 : 0)
					+ (((curBestDy < 0 && dy < 0) || (curBestDy > 0 && dy > 0)) ? 1 : 0);
			int curSum = (((curDx < 0 && dx < 0) || (curDx > 0 && dx > 0)) ? 1 : 0)
					+ (((curDy < 0 && dy < 0) || (curDy > 0 && dy > 0)) ? 1 : 0);

			// look for a closer match
			if (bestSum < curSum) {
				bestIndex = i;
			} else {

			}

		}

		// if we have several edges,
		// make sure we choose another than the edge we came from
		if (edges.length > 1 && edges[bestIndex].equals(comingFrom)) {
			return edges[(bestIndex + 1) % edges.length];
		} else {
			return edges[bestIndex];
		}
	}
}
