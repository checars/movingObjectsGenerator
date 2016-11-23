package cn.edu.neu.mog.apps;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import cn.edu.neu.mog.fileinput.Earth;
import cn.edu.neu.mog.fileinput.Edge;
import cn.edu.neu.mog.fileinput.HeaderBoundingBox;
import cn.edu.neu.mog.fileinput.MainFile;
import cn.edu.neu.mog.fileinput.MainFileRecord;
import cn.edu.neu.mog.fileinput.Node;
import cn.edu.neu.mog.fileinput.shapetypes.Point;
import cn.edu.neu.mog.fileinput.shapetypes.PolyLine;
import cn.edu.neu.mog.util.ExecuteParamSingleton;

/**
 * Look for shape files containing streets, parse them and create node and edge
 * file which can be used by the brinkhoff generator.
 * 
 * @author CheQingShou
 */
public class NodesAndEdgesGenarator {

	private static final Pattern streetFilenamePattern = Pattern.compile(".*_0\\d\\.shp");

	/**
	 * A map used while parsing the street files which allows to lookup a node
	 * id by a Point.
	 */
	private HashMap<Point, Integer> pointToNodeIdMap;

	/**
	 * The next node ID to assign.
	 */
	private int nextNodeId;

	/**
	 * The next edge ID to assign.
	 */
	private int nextEdgeId;

	/**
	 * Number of junctions found while parsing the street files.
	 */
	private int numJunctions;

	/**
	 * A bounding box around all coordinates found in all street files.
	 */
	private HeaderBoundingBox box;

	/**
	 * Scaling factor in x dimension.
	 */
	private double ratioX;

	/**
	 * Scaling factor in y dimension.
	 */
	private double ratioY;

	/**
	 * Output stream to write edge file.
	 */
	private DataOutputStream outEdges;

	/**
	 * Output stream to write node file.
	 */
	private DataOutputStream outNodes;

	/**
	 * @param args must have length 2. the input- and output directories.
	 */
	public static void main(String[] args) throws IOException {
		new NodesAndEdgesGenarator().run(args);
	}

	/**
	 * Instantiate.
	 */
	public NodesAndEdgesGenarator() {
		pointToNodeIdMap = new HashMap<Point, Integer>();
		nextNodeId = 0;
		nextEdgeId = 0;
		numJunctions = 0;
		box = new HeaderBoundingBox();
	}

	public void run(String[] args) throws IOException {

		// get given directories
		Properties pro = ExecuteParamSingleton.getInstance().getProperties();
		File dirInput = new File(pro.getProperty("shpFileinputPath"));
		File dirOutput = new File(pro.getProperty("nodeAndedgeFileOutputPath"));
		
		System.out.println("Input  directory: " + dirInput.getAbsolutePath());
		System.out.println("Output directory: " + dirOutput.getAbsolutePath());
		System.out.println();

		// collect street files
		List<File> streetFiles = new ArrayList<File>();
		collectStreetFilesRecursively(dirInput, streetFiles);
		StringBuffer buf = new StringBuffer();
		for (File file : streetFiles) {
			buf.append(file.getName() + " ");
		}
		System.out.println("Street files (" + streetFiles.size() + "): " + buf.toString());
		System.out.println();

		// find data space bounding box
		for (File file : streetFiles) {
			MainFile streetFile = new MainFile(file);
			box.updateMinMax(streetFile.getHeader().getBoundingBox());
		}
		double xExt = box.xMax - box.xMin + 0.0001;
		double yExt = box.yMax - box.yMin + 0.0001;
		box.zMin = 0.0;
		box.zMax = 0.0;
		box.mMin = 0.0;
		box.mMax = 0.0;
		System.out.println("Bounding box: " + box);
		System.out.println();

		// translation to m
		final double heightM = yExt / 360.0 * Earth.POLAR_CIRCUMFERENCE;
		final double middleY = box.yMin + yExt / 2.0;
		final double middleRadius = Earth.EQUATORIAL_RADIUS * Math.sin((90.0 - middleY) / 180.0 * Math.PI);
		final double circumferenceX = middleRadius * 2.0 * Math.PI;
		final double widthM = xExt / 360.0 * circumferenceX;
		ratioX = widthM / xExt;
		ratioY = heightM / yExt;
		System.out.println("Width in km: " + (widthM / 1000.0) + ", height in km: " + (heightM / 1000.0));
		System.out.println("Ratio X: " + ratioX + ", ratio Y: " + ratioY);
		System.out.println();

		// create output streams
		String filenameEdges = "file-from-shapefile.edge";
		String filenameNodes = "file-from-shapefile.node";
		File fileEdges = new File(dirOutput.getAbsolutePath() + "/" + filenameEdges);
		File fileNodes = new File(dirOutput.getAbsolutePath() + "/" + filenameNodes);
		OutputStream fosEdges = new FileOutputStream(fileEdges);
		OutputStream fosNodes = new FileOutputStream(fileNodes);
		outEdges = new DataOutputStream(new BufferedOutputStream(fosEdges));
		outNodes = new DataOutputStream(new BufferedOutputStream(fosNodes));

		// parse the street files
		// write node- and edge file while parsing
		for (File file : streetFiles) {
			parseStreetfile(file);
		}

		// show stats
		System.out.println();
		System.out.println("Total number of edges:     " + nextEdgeId);
		System.out.println("Total number of nodes:     " + nextNodeId);
		System.out.println("Total number of junctions: " + numJunctions);

		// close streams
		outEdges.close();
		outNodes.close();

		// show written file sizes
		System.out.println("Written file sizes: nodes=" + fileNodes.length() + ", edges=" + fileEdges.length());
	}

	/**
	 * Collect all street files inside the given directory.
	 * 
	 * @param dir
	 * @param streetFiles
	 * @throws IOException
	 */
	private void collectStreetFilesRecursively(File dir, List<File> streetFiles) throws IOException {

		// loop over all files in name order
		List<String> filenames = new ArrayList<String>(Arrays.asList(dir.list()));
		Collections.sort(filenames);
		for (String filename : filenames) {
			File file = new File(dir.getAbsolutePath() + "/" + filename);

			// ignore hidden files
			if (file.isHidden() || filename.startsWith(".")) {
				continue;
			}

			// recurse into directories
			if (file.isDirectory()) {
				collectStreetFilesRecursively(file, streetFiles);
			} else {

				// work on street files only
				if (streetFilenamePattern.matcher(filename).matches()) {
					streetFiles.add(file);
				}
			}
		}
	}

	private void parseStreetfile(File file) throws IOException {

		// find edge class (maximum edge class is 7)
		String filename = file.getName();
		int pos = filename.lastIndexOf(".");
		String edgeClassStr = filename.substring(pos - 1, pos);
//		System.out.println("edgeClassStr====>"+edgeClassStr);
		int edgeClass = Integer.parseInt(edgeClassStr);
		if (edgeClass > 7) {
			edgeClass = 7;
		}

		// create main file
		MainFile streetFile = new MainFile(file);
		System.out.print("File: " + file.getName() + ", length: " + file.length() + ", ");

		// loop over records
		int numRecords = 0;
		int numNewNodes = 0;
		int numNewEdges = 0;
		int numJunctionsCur = 0;
		for (MainFileRecord record : streetFile) {
			numRecords++;

			// get polyline
			PolyLine line = (PolyLine) record.getContent();
			int[] parts = line.getParts();
			Point[] points = line.getPoints();

			// loop over all parts of a polyline
			for (int i = 0; i < parts.length; i++) {

				// loop over one connected polyline
				int fromPointIndex = parts[i];
				int toExclusivePointIndex = (i == parts.length - 1) ? points.length : parts[i + 1];
				Point from = points[fromPointIndex];
				Point to = null;
				for (int pointIndex = fromPointIndex + 1; pointIndex < toExclusivePointIndex; pointIndex++) {
					to = points[pointIndex];

					// get node id from
					Integer fromNodeId = pointToNodeIdMap.get(from);
					if (fromNodeId == null) {
						fromNodeId = nextNodeId;
						Integer prev = pointToNodeIdMap.put(from, fromNodeId);
						Node node = new Node(fromNodeId, from.getX(), from.getY());
						writeNode(node);
						if (prev != null) {
							throw new RuntimeException("Should not happen!");
						}
						nextNodeId++;
						numNewNodes++;
					} else {
						if (pointIndex == fromPointIndex + 1) {
							numJunctionsCur++;
						}
					}

					// get node id to
					Integer toNodeId = pointToNodeIdMap.get(to);
					if (toNodeId == null) {
						toNodeId = nextNodeId;
						Integer prev = pointToNodeIdMap.put(to, toNodeId);
						Node node = new Node(toNodeId, to.getX(), to.getY());
						writeNode(node);
						if (prev != null) {
							throw new RuntimeException("Should not happen!");
						}
						nextNodeId++;
						numNewNodes++;
					} else {
						numJunctionsCur++;
					}

					// store edge
					Edge edge = new Edge(fromNodeId, toNodeId, nextEdgeId, edgeClass);
					writeEdge(edge);
					nextEdgeId++;
					numNewEdges++;

					// go to next point
					from = to;
				}
			}
		}
		numJunctions += numJunctionsCur;
		System.out.print("number of records: " + numRecords + ", number of junctions: " + numJunctionsCur + ", ");
		System.out.println("number of nodes: " + numNewNodes + ", number of edges: " + numNewEdges);
	}

	private void writeNode(Node node) throws IOException {

		// translate to upper left corner
		// translate y-coordinate to top-down
		// scale double to int
		int x = (int) ((node.x - box.xMin) * ratioX);
		int y = (int) ((box.yMax - node.y) * ratioY);
		if (y < 0) {
			System.err.println("Hello");
		}

		// no name
		byte l = (byte) 0;
		outNodes.writeByte(l);

		// node id
		outNodes.writeLong(node.nodeId);

		// x and y
		outNodes.writeInt(x);
		outNodes.writeInt(y);
	}

	private void writeEdge(Edge edge) throws IOException {

		// node ids
		outEdges.writeLong(edge.nodeId1);
		outEdges.writeLong(edge.nodeId2);

		// no name
		byte l = (byte) 0;
		outEdges.writeByte(l);

		// edge id and class
		outEdges.writeLong(edge.edgeId);
		outEdges.writeInt(edge.edgeClass);
	}
}
