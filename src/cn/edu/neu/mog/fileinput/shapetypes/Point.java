package cn.edu.neu.mog.fileinput.shapetypes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A class representing a Point of a shape file.
 * 
 * @author CheQingShou
 */
public class Point implements RecordContent {

	private double x;

	private double y;

	/**
	 * Instantiate and read record content from given buffer.
	 * 
	 * @param buffer
	 */
	public Point(ByteBuffer buffer, boolean readShapeType) {

		// assert byte order
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		// assert shape type
		if (readShapeType) {
			int shapeType = buffer.getInt();
			if (shapeType != 1) {
				throw new RuntimeException("Wrong shape type encountered (1): " + shapeType);
			}
		}

		// read data
		x = buffer.getDouble();
		y = buffer.getDouble();
	}

	public String getShapeName() {
		return "Point";
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	// ---------------------------- Overrides to use Point as keys in a HashMap

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point p = (Point) obj;
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		long bits = Double.doubleToLongBits(x + y);
		return (int) (bits ^ (bits >>> 32));
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
