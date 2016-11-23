package cn.edu.neu.mog.fileinput;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A bounding box of a shape file header.
 * 
 * @author CheQingShou
 */
public class HeaderBoundingBox {

	public double xMin;

	public double yMin;

	public double xMax;

	public double yMax;

	public double zMin;

	public double zMax;

	public double mMin;

	public double mMax;

	/**
	 * Instantiate with uninitialized values.
	 */
	public HeaderBoundingBox() {
		xMin = Double.MAX_VALUE;
		yMin = Double.MAX_VALUE;
		xMax = Double.MIN_VALUE;
		yMax = Double.MIN_VALUE;
		zMin = Double.MAX_VALUE;
		zMax = Double.MIN_VALUE;
		mMin = Double.MAX_VALUE;
		mMax = Double.MIN_VALUE;
	}

	/**
	 * Instantiate by reading value from given buffer.
	 * 
	 * @param buffer
	 */
	public HeaderBoundingBox(ByteBuffer buffer) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		xMin = buffer.getDouble();
		yMin = buffer.getDouble();
		xMax = buffer.getDouble();
		yMax = buffer.getDouble();
		zMin = buffer.getDouble();
		zMax = buffer.getDouble();
		mMin = buffer.getDouble();
		mMax = buffer.getDouble();
	}

	/**
	 * Update stored min and max values with min and max values from given box.
	 * 
	 * @param box
	 */
	public void updateMinMax(HeaderBoundingBox box) {
		if (box.xMin < xMin) {
			xMin = box.xMin;
		}
		if (box.yMin < yMin) {
			yMin = box.yMin;
		}
		if (box.xMax > xMax) {
			xMax = box.xMax;
		}
		if (box.yMax > yMax) {
			yMax = box.yMax;
		}
		if (box.zMin < zMin) {
			zMin = box.zMin;
		}
		if (box.zMax > zMax) {
			zMax = box.zMax;
		}
		if (box.mMin < mMin) {
			mMin = box.mMin;
		}
		if (box.mMax > mMax) {
			mMax = box.mMax;
		}
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("(" + xMin + "," + yMin + "," + zMin + "," + mMin + ") x ");
		buf.append("(" + xMax + "," + yMax + "," + zMax + "," + mMax + ")");
		return buf.toString();
	}

}
