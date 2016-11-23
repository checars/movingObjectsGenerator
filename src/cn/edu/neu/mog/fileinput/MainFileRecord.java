package cn.edu.neu.mog.fileinput;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import cn.edu.neu.mog.fileinput.shapetypes.PolyLine;
import cn.edu.neu.mog.fileinput.shapetypes.RecordContent;

/**
 * A class representing a shape file main file record.
 * 
 * @author CheQingShou
 */
public class MainFileRecord {

	private final int bufferPosition;

	private final int recordNumber;

	/* content length in number of 16-bit words */
	private final int contentLength;

	private RecordContent content;

	/**
	 * Instantiate and read data.
	 * 
	 * @param buffer
	 * @param shapeType
	 */
	public MainFileRecord(ByteBuffer buffer, final int shapeType) {
		bufferPosition = buffer.position();

		// assert byte order
		buffer.order(ByteOrder.BIG_ENDIAN);

		// read record header
		recordNumber = buffer.getInt();
		contentLength = buffer.getInt();

		// read record content
		int limit = buffer.position() + contentLength * 2;
		while (buffer.position() < limit) {
			switch (shapeType) {
			
			case ShapeType.POLYLINE:
				content = new PolyLine(buffer);
				break;
			
			default:
				throw new UnsupportedOperationException("Unsupported shape type: " + shapeType);
			}
		}
	}

	public RecordContent getContent() {
		return content;
	}

	@Override
	public String toString() {
		return content.getShapeName() + ": #" + recordNumber + ": length=" + contentLength * 2 + "\t(@"
				+ bufferPosition + ")\t" + content;
	}

}
