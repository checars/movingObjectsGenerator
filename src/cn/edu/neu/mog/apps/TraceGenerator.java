package cn.edu.neu.mog.apps;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import cn.edu.neu.mog.generator.MovingObject;
import cn.edu.neu.mog.generator.Reporter;
import cn.edu.neu.mog.generator.network.Edge;
import cn.edu.neu.mog.generator.network.Network;
import cn.edu.neu.mog.generator.network.NetworkFromNodesAndEdges;
import cn.edu.neu.mog.generator.network.Node;
import cn.edu.neu.mog.generator.scenarios.Scenario;
import cn.edu.neu.mog.util.ExecuteParamSingleton;

/**
 * A moving objects generator based on shapefiles.
 * 
 * @author CheQingShou
 */
public class TraceGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		new TraceGenerator().run(args);
	}

	private void run(String[] args) throws IOException {

		// input parameters
		Properties pro = ExecuteParamSingleton.getInstance().getProperties();
		final int numObjects = Integer.parseInt(pro.getProperty("numMovingObj"));
		final int numTimestamps = Integer.parseInt(pro.getProperty("numTimeStamps"));
		String basePath = pro.getProperty("traceFileInputPath");
		String outPath = pro.getProperty("traceFileOutputPath");
		String outPrefix = pro.getProperty("traceFilePrefix");
		
		File outDir = new File(outPath);
		if (!outDir.exists() || !outDir.isDirectory()) {
			System.err.println("Output directory does not exist or is not a directory.");
		}

		// load network
		Network network = new NetworkFromNodesAndEdges(basePath);
		System.out.println("Number of nodes in network: " + network.size());
		System.out.println("Bounding box of network: " + network.getBoundingBox());

		// generate trace
		generateTrace(numObjects, numTimestamps, network, outDir, outPrefix);
	}

	protected void generateTrace(final int numObjects, final int numTimestamps, Network network,
			File outDir, String outPrefix) throws IOException {

		// create moving objects
		Random random = new Random();
		MovingObject[] objects = new MovingObject[numObjects];
		for (int i = 0; i < objects.length; i++) {

			// select a random node,
			// put object on random edge and
			// at a random position on the edge
			int nodeId = random.nextInt(network.size());
			Node node = network.getNode(nodeId);
			int edgeIndex = random.nextInt(node.getEdges().length);
			Edge edge = node.getEdges()[edgeIndex];
			double position = random.nextDouble();
			objects[i] = new MovingObject(edge, true, position);
		}
		System.out.println("Number of moving objects: " + objects.length);

		// create scenario
		int widthKm = (int) (network.getBoundingBox().width / 1000.0);
		int heightKm = (int) (network.getBoundingBox().height / 1000.0);
		Scenario scenario = new Scenario("Shapefile", widthKm, heightKm, 60.0*32,1 << 5);
		System.out.println("Scenario: " + scenario);

		// create reporter
		String prefix = outPrefix + "_numobjs-" + numObjects + "_maxt-" + (numTimestamps);
		Reporter reporter = new Reporter(outDir, prefix, network.getBoundingBox(), scenario);

		// loop over timestamps
		long begin = System.nanoTime();
		for (int timestamp = 0; timestamp < numTimestamps; timestamp++) {

			// loop over objects
			for (int i = 0; i < objects.length; i++) {
				MovingObject object = objects[i];

				// report
				reporter.print(i, timestamp, object);

				// update position
				object.move(scenario);
			}
		}

		// close reporter
		reporter.close();

		// report time
		long end = System.nanoTime();
		System.out.println("Time to generate trace: " + ((end - begin) / 1e9) + "s.\n");
	}

}

