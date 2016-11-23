package cn.edu.neu.mog.apps;

import java.io.IOException;

/**
 * One key generate trace file.
 * 
 * @author CheQingShou 
 */

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		NodesAndEdgesGenarator.main(args);
		TraceGenerator.main(args);
	}

}
