package cn.edu.neu.mog.generator.network;

/**
 * A bounding box with physical x- and y-coordinates in meters.
 * 
 * @author CheQingShou
 */
public class BoundingBox {

	/**
	 * Minimum x-coordinate in m.
	 */
	public int xMin;

	/**
	 * Minimum y-coordinate in m.
	 */
	public int yMin;

	/**
	 * Maximum x-coordinate in m.
	 */
	public int xMax;

	/**
	 * Maximum y-coordinate in m.
	 */
	public int yMax;

	/**
	 * Width in m.
	 */
	public int width;

	/**
	 * Height in m.
	 */
	public int height;

	/**
	 * Maximum extent in m.
	 */
	public int maxDim;

	/**
	 * Instantiate with uninitialized values.
	 */
	public BoundingBox() {
		xMin = Integer.MAX_VALUE;
		yMin = Integer.MAX_VALUE;
		xMax = Integer.MIN_VALUE;
		yMax = Integer.MIN_VALUE;
		width = 0;
		height = 0;
		maxDim = 0;
	}

	/**
	 * Update stored min and max values with min and max values from given box.
	 * 
	 * @param box
	 */
	public void updateMinMax(BoundingBox box) {
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
		updateDerivedValues();
	}

	/**
	 * Update stored min and max values in such a way to include the position of
	 * the given node.
	 * <p>
	 * Hint: Remember to call updateDerivedValues after a call to any of the
	 * enclose methods.
	 * 
	 * @param node
	 */
	public void encloseNode(Node node) {
		encloseX(node.getX());
		encloseY(node.getY());
	}

	/**
	 * Update stored x-min and x-max values in such a way to include the
	 * position of the given x value.
	 * <p>
	 * Hint: Remember to call updateDerivedValues after a call to any of the
	 * enclose methods.
	 * 
	 * @param x
	 */
	public void encloseX(int x) {
		if (x < xMin) {
			xMin = x;
		}
		if (x > xMax) {
			xMax = x;
		}
	}

	/**
	 * Update stored y-min and y-max values in such a way to include the
	 * position of the given y value.
	 * <p>
	 * Hint: Remember to call updateDerivedValues after a call to any of the
	 * enclose methods.
	 * 
	 * @param y
	 */
	public void encloseY(int y) {
		if (y < yMin) {
			yMin = y;
		}
		if (y > yMax) {
			yMax = y;
		}
	}

	public void updateDerivedValues() {
		width = xMax - xMin;
		height = yMax - yMin;
		maxDim = width < height ? height : width;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("(" + xMin + " m," + yMin + " m) x ");
		buf.append("(" + xMax + " m," + yMax + " m)");
		return buf.toString();
	}

}
