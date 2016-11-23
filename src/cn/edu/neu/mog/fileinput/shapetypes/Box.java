package cn.edu.neu.mog.fileinput.shapetypes;

import java.nio.ByteBuffer;

/**
 * A class to read and store a Box of a shape file.
 * 
 * @author CheQingShou
 */
public class Box {

	private double xMin;

	private double yMin;

	private double xMax;

	private double yMax;

	/**
	 * Instantiate and read data from given buffer.
	 * 
	 * @param buffer
	 */
	public Box(ByteBuffer buffer) {
		xMin = buffer.getDouble();
		yMin = buffer.getDouble();
		xMax = buffer.getDouble();
		yMax = buffer.getDouble();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("(" + xMin + "," + yMin + ") x ");
		buf.append("(" + xMax + "," + yMax + ")");
		return buf.toString();
	}

}
