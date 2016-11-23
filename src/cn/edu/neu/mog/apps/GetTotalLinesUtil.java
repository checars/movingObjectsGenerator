package cn.edu.neu.mog.apps;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class GetTotalLinesUtil {
	
	public static void main(String[] args) throws IOException{
		int i = getTotalLine(new File("C:\\Users\\Cars\\Desktop\\movingObjectsGenerator\\finalResult\\experimentData_numObject=1000_numStamps=1000.txt"));
		System.out.println(i);
	}
	
	/**
	 * 
	 * get the total number of file
	 * */
	public static int getTotalLine(File file) throws IOException{
		FileReader inputFile = new FileReader(file);
		LineNumberReader reader = new LineNumberReader(inputFile);
		
		int lines = 0;
		long begin = System.nanoTime();
		while(reader.readLine() != null){
			lines ++;
		}
		long end = System.nanoTime();
		System.out.println((end - begin) / 1e9);
		inputFile.close();
		return lines;
	}
	
}
