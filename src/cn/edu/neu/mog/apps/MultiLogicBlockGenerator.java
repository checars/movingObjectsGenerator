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
 * Generating multiple different size logic blocks, then map to a whole map.
 * 
 * @author CheQingShou
 *
 */
public class MultiLogicBlockGenerator {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		new MultiLogicBlockGenerator().run();
	}
	
	public void run() throws IOException{
		
		//input parameters 
		Properties pro = ExecuteParamSingleton.getInstance().getProperties();
		final int numObjects = Integer.parseInt(pro.getProperty("numMovingObj"));
		final int numTimestamps = Integer.parseInt(pro.getProperty("numTimeStamps"));
		String traceFileInputPath = pro.getProperty("traceFileInputPath");
		String multiTraceFileOutpath = pro.getProperty("multiTraceFilepath");
		String outPrefix = pro.getProperty("traceFilePrefix");
		
		File outDir = new File(multiTraceFileOutpath);
		if(!outDir.exists() || !outDir.isDirectory()){
			System.out.println("Output directory does not exist or is not a directory.");
		}
		
		//delete all files in the directory 
		File[] deleteFiles = outDir.listFiles();
		for(int i=0; i<deleteFiles.length; i++){
            File tmp = deleteFiles[i];
            if(tmp.toString().endsWith(".txt")){
                tmp.delete();
            }
        }
		
		//Load network by the files from traceFileInputPath
		Network network = new NetworkFromNodesAndEdges(traceFileInputPath);
		System.out.println("Bounding box of network: " + network.getBoundingBox());
		
		//Generate multiple trace, array logicBlockSize in order to save block size
		int[] logicBlockSize = {8000,8000,10000,15000,15000,20000,20000,50000,50000,50000};
		
		for(int i = 0; i < 10; i++){
			generateMultiTrace(numObjects, numTimestamps, network, outDir, outPrefix, logicBlockSize[i], i);
		}
	}

	private void generateMultiTrace(int numObjects, int numTimestamps, Network network, File outDir, String outPrefix,
			int logicBlockSize, int blockIndex) throws IOException {
		
		System.out.println("Logic size of block " + blockIndex + " is : "+ logicBlockSize);
		System.out.println("Number of moving objects for the block " + blockIndex + " : "+ numObjects);
		
		//create moving objects
		Random rand = new Random();
		MovingObject[] objects = new MovingObject[numObjects];
		for (int i = 0; i < objects.length; i++) {
			//select a random node 
			//put object on random edge 
			//put a random position on the edge
			int nodeId = rand.nextInt(network.size());
			Node node = network.getNode(nodeId);
			int edgeIndex = rand.nextInt(node.getEdges().length);
			Edge edge = node.getEdges()[edgeIndex];
			double position = rand.nextDouble();
			objects[i] = new MovingObject(edge, true, position);
		}
		
		//create scenario
		int withKm = (int) (network.getBoundingBox().width / 1000);
		int heightKm = (int) (network.getBoundingBox().height / 1000);
		Scenario scenario = new Scenario("LogicBlock", withKm, heightKm, 60*logicBlockSize, logicBlockSize);
		System.out.println("Scenario: " + scenario);

		//create reporter
		String prefix = outPrefix + "_numobjs-" + numObjects + "_block-" + blockIndex + "_logicsize-" + logicBlockSize ;
		Reporter reporter = new Reporter(outDir, prefix, network.getBoundingBox(), scenario);
		
		//loop over time stamps
		long begintime = System.nanoTime();
		for(int timestamp = 0; timestamp < numTimestamps; timestamp ++){
			
			//loop over objects
			for(int i = 0; i < objects.length; i ++){
				MovingObject object = objects[i];
				//reporter
				reporter.print(i, timestamp, object);
				//update position
				object.move(scenario);
			}
		}
		
		//close reporter
		reporter.close();
		
		//print time
		long endtime = System.nanoTime();
		System.out.println("Time to generate trace for block " + blockIndex + ": " + ((endtime-begintime) / 1e9) + "s.\n");
	}
	
	
}
