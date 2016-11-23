package cn.edu.neu.mog.generator.writers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The abstract base class for each writer.
 * 
 * @author CheQingShou
 */
public abstract class Writer {

	public abstract void write(int objectId, String x, String y );
	protected static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	/**
	 * Write an update to the trace.
	 * 
	 * @param objectId ID of the moving object.
	 * @param xlog logical x-coordinate.
	 * @param ylog logical y-coordinate.
	 * @param dirBits 3 bits for the direction.
	 * @param speedBits 2 bits for the speed.
	 * @throws IOException
	 */
	public abstract void write(int objectId, short xlog, short ylog, byte dirBits, byte speedBits) throws IOException;

	/**
	 * Write a query to the trace.
	 * 
	 * @param queryId
	 * @param xMinLog
	 * @param xMaxLog
	 * @param yMinLog
	 * @param yMaxLog
	 * @throws IOException
	 */
	public abstract void writeQuery(int queryId, short xMinLog, short xMaxLog, short yMinLog, short yMaxLog)
			throws IOException;

	/**
	 * Close this writer.
	 * 
	 * @throws IOException
	 */
	public abstract void close() throws IOException;

}
