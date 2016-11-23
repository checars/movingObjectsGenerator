package cn.edu.neu.mog.generator.writers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A textual trace writer.
 * 
 * @author CheQingShou
 */
public class TextWriter extends Writer {

	private PrintWriter printer;

	/**
	 * Instantiate.
	 * 
	 * @param outDir
	 * @param outFilename
	 * @throws IOException
	 */
	public TextWriter(File outDir, String outFilename) throws IOException {

		// create print writer
		String filename = outFilename + ".txt";
		File file = new File(outDir.getAbsolutePath() + "/" + filename);
		printer = new PrintWriter(file);
	}

	public void write(int objectId, short xlog, short ylog, byte dirBits, byte speedBits) {
		printer.print(objectId);
		printer.print('\t');
		printer.print(xlog);
		printer.print('\t');
		printer.print(ylog);
		printer.print('\t');
		printer.print(dirBits);
		printer.print('\t');
		printer.print(speedBits);
		printer.println();
	}

	public void writeQuery(int queryId, short xMinLog, short xMaxLog, short yMinLog, short yMaxLog) throws IOException {
		printer.print(queryId);
		printer.print('\t');
		printer.print(xMinLog);
		printer.print('\t');
		printer.print(yMinLog);
		printer.print('\t');
		printer.print(xMaxLog);
		printer.print('\t');
		printer.print(yMaxLog);
		printer.println();
	}

	public void close() throws IOException {
		printer.close();
	}

	@Override
	public void write(int objectId, String x, String y) {
		// TODO Auto-generated method stub
		printer.print(objectId);
		printer.print('\t');
		printer.print(x);
		printer.print('\t');
		if(x.length()<8)printer.print('\t');
		printer.print(y);
		printer.println();
	}

}
