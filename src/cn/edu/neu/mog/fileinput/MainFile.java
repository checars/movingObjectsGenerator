package cn.edu.neu.mog.fileinput;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Iterator;

/**
 * A class representing a shape file main file.
 * 
 * @author CheQingShou
 */
public class MainFile implements Iterable<MainFileRecord> {

	private File file;

	private FileChannel channel;

	private ByteBuffer buffer;

	private MainFileHeader header;

	/**
	 * Instantiate and read header.
	 * 
	 * @param file
	 * @throws IOException
	 */
	public MainFile(File file) throws IOException {

		// store and test given file
		this.file = file;
		if (!file.canRead()) {
			throw new IOException("Cannot read from file " + file.getAbsolutePath());
		}

		// map file to memory
		channel = new RandomAccessFile(file, "r").getChannel();
		buffer = channel.map(MapMode.READ_ONLY, 0L, file.length());

		// read header
		this.header = new MainFileHeader(buffer);
	}

	/**
	 * @return the main file header of this shape file.
	 */
	public MainFileHeader getHeader() {
		return header;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Name: " + file.getName() + ", ");
		buf.append("Length: " + file.length() + ", ");
		buf.append("Header: " + header);
		return buf.toString();
	}

	public Iterator<MainFileRecord> iterator() {
		buffer.position(100);
		MainFileRecordIterator iter = new MainFileRecordIterator(buffer, header.getShapeType());
		return iter;
	}

}
