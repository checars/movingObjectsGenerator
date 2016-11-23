package cn.edu.neu.mog.fileinput;

import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * An iterator to walk over the records of a shape file main file.
 * 
 * @author CheQingShou
 */
public class MainFileRecordIterator implements Iterator<MainFileRecord> {

	private ByteBuffer buffer;

	private int shapeType;

	/**
	 * Instantiate.
	 * 
	 * @param buffer
	 * @param shapeType
	 */
	public MainFileRecordIterator(ByteBuffer buffer, int shapeType) {
		this.buffer = buffer.duplicate();
		this.shapeType = shapeType;
	}

	public boolean hasNext() {
		return buffer.remaining() > 0;
	}

	public MainFileRecord next() {
		MainFileRecord record = new MainFileRecord(buffer, shapeType);
		return record;
	}

	public void remove() {
		throw new UnsupportedOperationException("Remove is not allowed.");
	}

}
