package cn.edu.neu.mog.fileinput;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The main file header of a shape file.
 * 
 * @author CheQingShou
 */
public class MainFileHeader {

	private int fileCode;

	/* file length in number of 16-bit words */
	private int fileLength;

	private int version;

	private int shapeType;

	private HeaderBoundingBox boundingBox;

	/**
	 * Instantiate.
	 * 
	 * @param bytes 100 bytes containing the main file header.
	 */
	public MainFileHeader(ByteBuffer buffer) {

		// misc info
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.position(0);
		fileCode = buffer.getInt();
		buffer.position(24);
		fileLength = buffer.getInt();
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		version = buffer.getInt();
		shapeType = buffer.getInt();

		// assert file code and version
		if (fileCode != 9994 || version != 1000) {
			throw new UnsupportedOperationException("Unsupported file code or version!");
		}

		// bounding box
		boundingBox = new HeaderBoundingBox(buffer);
	}

	public int getShapeType() {
		return shapeType;
	}

	/**
	 * @return bounding box around all coordinates found in the main file.
	 */
	public HeaderBoundingBox getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Shape type: " + shapeType + ", File length: " + fileLength * 2 + ", Bounding box: " + boundingBox);
		return buf.toString();
	}

}
