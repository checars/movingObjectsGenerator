package cn.edu.neu.mog.generator;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.edu.neu.mog.generator.network.BoundingBox;
import cn.edu.neu.mog.generator.network.Node;
import cn.edu.neu.mog.generator.scenarios.Scenario;
import cn.edu.neu.mog.generator.writers.TextWriter;
import cn.edu.neu.mog.generator.writers.Writer;

/**
 * A reporter produces a trace of positions of objects while these objects move
 * around.
 * 
 * @author CheQingShou
 */
public class Reporter {

	private List<Writer> writers;

	private BoundingBox boundingBox;

	private Scenario scenario;

	/**
	 * Instantiate.
	 * 
	 * @param outDir
	 * @param outFilename
	 * @param boundingBox
	 * @param scenario
	 * @throws IOException
	 */
	public Reporter(File outDir, String outFilename, BoundingBox boundingBox, Scenario scenario) throws IOException {

		// create writers
		writers = new ArrayList<Writer>();
		writers.add(new TextWriter(outDir, outFilename));
		//writers.add(new BinaryWriter(outDir, outFilename));
		//writers.add(new ImageWriter(outDir, outFilename, scenario));

		// store bounding box
		this.boundingBox = boundingBox;

		// store scenario
		this.scenario = scenario;
	}

	public void print(int objectId, int timestamp, MovingObject object) throws IOException {
		DecimalFormat dFormat = new DecimalFormat("#.00"); 
		// prepare direction
		Node node1 = object.edge.node1;
		Node node2 = object.edge.node2;
		if (!object.forward) {
			node1 = object.edge.node2;
			node2 = object.edge.node1;
		}
//		byte dirBits = getDirection(node1.getX(), node1.getY(), node2.getX(), node2.getY());

		// prepare speed
		// speed is in [m/s]
		// speedBits are a proportion of the maximum speed
//		double edgeClass = 1.0 + object.edge.edgeClass;
//		System.out.println("object.position=======>" + object.position);
//		double speed = scenario.speedMaxMs / edgeClass;
//		byte speedBits = getSpeed(speed);

		// prepare logical coordinates
		// x, y are in physical coordinates [m].
		double x = node1.getX() + (node2.getX() - node1.getX()) * object.position;
		double y = node1.getY() + (node2.getY() - node1.getY()) * object.position;

		double xlog = getX(x);
		double ylog = getY(y);
		// write
		for (Writer writer : writers) {
			writer.write(objectId, dFormat.format(xlog), dFormat.format(ylog));
		}
	}


	/**
	 * Get logical x-coordinate.
	 * 
	 * @param x physical x-coordinate [m].
	 * @return translated and scaled logical x-coordinate.
	 */
	private double getX(double x) {

		// make sure xpos is smaller than width
		double xpos = x - boundingBox.xMin - 0.1;

		// shift scenario.borderWidth to the right and
		// scale to fit into logical space
		double xlog = scenario.borderWidthLog + xpos / boundingBox.maxDim * scenario.widthLogNoBorder;
		return xlog;
	}

	/**
	 * Get logical y-coordinate.
	 * 
	 * @param y physical y-coordinate.
	 * @return translated and scaled logical y-coordinate.
	 */
	private double getY(double y) {

		// make sure ypos is smaller than height
		double ypos = y - boundingBox.yMin - 0.1;

		// shift scenario.borderWidth down and
		// scale to fit into logical space
		// (use the same scaling as for the x-coordinate)
		double ylog = scenario.borderWidthLog + ypos / boundingBox.maxDim * scenario.widthLogNoBorder;
		return ylog;
	}
	
	/**
	 * Get speed bits (2).
	 * 
	 * @param speed speed between 0.0 and scenario.speedMaxMs (both including)
	 * @return speed bits between 0 and 3 (both including)
	 */
	/*private byte getSpeed(double speed) {
		int tmp = (int) Math.round(speed / scenario.speedMaxMs * 3.0);
		return (byte) tmp;
	}*/


	/**
	 * Get direction bits (3).
	 * 
	 * @param x
	 * @param y
	 * @param nextX
	 * @param nextY
	 * @return a direction value between including 0 and 7 (3 bits).
	 */
	/*private byte getDirection(double x, double y, double nextX, double nextY) {

		// find normalized direction vector
		double dx = nextX - x;
		double dy = nextY - y;
		double length = Math.sqrt(dx * dx + dy * dy);
		double ndx = dx / length;
		double ndy = dy / length;

		// calculate dot product
		double dot = 0.0 * ndx + 1.0 * ndy;

		// calculate the angle
		double angle = Math.acos(dot);

		// differentiate between positive and negative x
		if (dx < 0) {

			// tmp can be between and including 4 and 8
			int tmp = (int) (angle / Math.PI * 4) + 4;
			byte result = tmp == 8 ? 0 : (byte) tmp;
			return result;
		} else {

			// tmp can be between and including 0 and 4
			int tmp = (int) (angle / Math.PI * 4);
			return (byte) tmp;
		}
	}*/

	
	public void close() throws IOException {
		for (Writer writer : writers) {
			writer.close();
		}
	}


}
