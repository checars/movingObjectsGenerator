package cn.edu.neu.mog.fileinput.shapetypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A class representing a PolyLine of a shape file.
 * 
 * @author CheQingShou
 */
public class PolyLine implements RecordContent {

	private Box box;

	private int numParts;

	private int numPoints;

	private int[] parts;

	private Point[] points;

	/**
	 * Instantiate and read record content from given buffer.
	 * 
	 * @param buffer
	 */
	public PolyLine(ByteBuffer buffer) {

		// assert byte order
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		// assert shape type
		int shapeType = buffer.getInt();
		if (shapeType != 3) {
			throw new RuntimeException("Wrong shape type encountered (3): " + shapeType);
		}

		// read data
		box = new Box(buffer);
		numParts = buffer.getInt();
		numPoints = buffer.getInt();
		parts = new int[numParts];
		for (int i = 0; i < numParts; i++) {
			parts[i] = buffer.getInt();
		}
		points = new Point[numPoints];
		for (int i = 0; i < numPoints; i++) {
			points[i] = new Point(buffer, false);
		}
	}

	public String getShapeName() {
		return "PolyLine";
	}

	public int[] getParts() {
		return parts;
	}

	public Point[] getPoints() {
		return points;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(box + ", numParts (lines)=" + numParts + ", numPoints=" + numPoints);
		return buf.toString();
	}

}
