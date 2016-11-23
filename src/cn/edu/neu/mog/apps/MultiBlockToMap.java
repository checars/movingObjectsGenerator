package cn.edu.neu.mog.apps;

import cn.edu.neu.mog.util.ExecuteParamSingleton;

import java.util.Properties;
import java.util.regex.Pattern;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.io.BufferedWriter;

/**
 * @author CheQingShou
 *
 */
public class MultiBlockToMap {
	
	private int objectId = 0;
	
	private static final Pattern fileNamePattern = Pattern.compile("(\\w+)_(\\w+)\\-(\\d+)_(\\w+)\\-(\\d+)_(\\w+)\\-(\\d+)\\.txt");
	// get input and output path, the number of object
	private Properties pro = ExecuteParamSingleton.getInstance().getProperties();
	private File dirInput = new File(pro.getProperty("multiTraceFilepath"));
	private File dirOutput = new File(pro.getProperty("finalTraceFilePath"));
	private final int numObjects = Integer.parseInt(pro.getProperty("numMovingObj"));
	private final int numStamps = Integer.parseInt(pro.getProperty("numTimeStamps"));
	private String fileName = pro.getProperty("finalFileName");
	private final int numBlocks = Integer.parseInt(pro.getProperty("numBlocks"));
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		new MultiBlockToMap().run();
	}

	private void run() throws IOException{
//		int[] logicBlockSize = {8000,8000,10000,15000,15000,20000,20000,50000,50000,50000};
		double[][] position = {{100,149999},{80000,80000},{45869,56247},{35647,56214},
				{65413,79852},{75482,65478},{55234,45678},{65784,74258},{43568,74589},{99999,89740}};
		//delete all files in the directory 
		/*File[] deleteFiles = dirOutput.listFiles();
		for(int i=0; i<deleteFiles.length; i++){
		      File tmp = deleteFiles[i];
		      if(tmp.toString().endsWith(".txt")){
		           tmp.delete();
		      }
		 }*/
		
		//collect multiple block files
		List<File> blockFiles = new ArrayList<File>();
		collectBlockFilesRecusive(dirInput, blockFiles);
		
		//extract coordinate data from all block files
		int p = 0; 
		long startTime = System.nanoTime();
		for (int i = 0; i < numStamps; i++) {
			
			for(File blockFile : blockFiles){
				readBlockFileByLine(blockFile.toString(), i*numObjects + 1, (i+1)*numObjects, dirOutput, fileName, position, p);
				p++;
			}
		}
		long endTime = System.nanoTime();
		System.out.println("Total time generator a map data : " + ((endTime - startTime) / 1e9) / 60 + ".m\n");
		
	}
	
	public void collectBlockFilesRecusive(File dirInput, List<File> blockFiles){
		
		// loop all files in name order
		List<String> filenames = new ArrayList<String>(Arrays.asList(dirInput.list()));
		for(String filename : filenames){
			File file = new File(dirInput.getAbsolutePath() + "/" + filename);
			
			// ignore hidden files
			if(file.isHidden() || filename.startsWith(".")){
				continue;
			}
			
			// recurse into directories
			if(file.isDirectory()){
				collectBlockFilesRecusive(file, blockFiles);
			} else {
				// add specify files into block files
				if(fileNamePattern.matcher(filename).matches()){
					blockFiles.add(file);
				}
			}
		}
	}
	
	public void readBlockFileByLine(String blockFilePath, int BeginLine, int endLine, File dirOutput, String fileName, 
			double[][] position, int p) throws IOException{
		File file = new File(blockFilePath);
		if(file.exists() && file.isFile()){
			InputStreamReader read = new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader = new BufferedReader(read);
			String fileLine = "";
//			System.out.println(blockFilePath);
			// read line by line number
			int lines = 0;
			while(fileLine != null && lines <= endLine){
				lines ++;
				fileLine = bufferedReader.readLine();
				if(lines>=BeginLine && lines <= endLine){
					finalCoordinateGenerator(fileLine, dirOutput, fileName, position, p);
				}
			}
			bufferedReader.close();
		}
	}
	
	/**
	 * generate final txt file to experiment
	 * */
	public void finalCoordinateGenerator(String fileLine, File dirOutput, String fileName,
			double[][] position, int p) throws IOException{
		DecimalFormat dFormat = new DecimalFormat("#.00"); 
		String filename = fileName + "_numObject=" + numObjects + "_numStamps=" + numStamps + ".txt";
		File file = new File(dirOutput.getAbsolutePath() + "/" + filename);
		FileOutputStream fos = new FileOutputStream(file, true);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bufferedWriter = new BufferedWriter(osw);
		
		objectId = objectId % (numObjects * numBlocks);
		
		String[] lineArr = fileLine.split("[\\p{Space}]+");
		
		String xp = dFormat.format(Double.parseDouble(lineArr[1]) + position[p%10][0]);
		String yp = dFormat.format(Double.parseDouble(lineArr[2]) + position[p%10][1]);
		
		bufferedWriter.write(String.valueOf(objectId));
		bufferedWriter.write('\t');
		bufferedWriter.write(xp);
		bufferedWriter.write('\t');
		if(xp.length()<8){ bufferedWriter.write('\t'); }
		bufferedWriter.write(yp);
		
		System.out.println(objectId + "\t" + xp + "\t" + yp);
		objectId ++;
		bufferedWriter.newLine();
		bufferedWriter.flush();
		
		bufferedWriter.close();
		osw.close();
		fos.close();

	}

	
	
}
