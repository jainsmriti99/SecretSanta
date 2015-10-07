package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class PropFileReader {
	Calendar now = Calendar.getInstance();
	int year = now.getWeekYear();
	String fileName = "";
	//String filepath = "/Users/jain/Documents/Smriti/SantaProperties";
	String filepath = ".";
	
	
	public HashMap<String,String> readPrevSanta(){
		HashMap<String,String> santaPrev = new HashMap<String,String>();
		int prevYear = year -1;
		fileName = filepath + "/santas" + String.valueOf(prevYear);
		
		Properties defaultProps = new Properties();
		
		try {
			FileInputStream in = new FileInputStream(fileName);
			defaultProps.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 Enumeration e = defaultProps.propertyNames();
		 while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      santaPrev.put(key, defaultProps.getProperty(key));
		      
		    }
		 
		 return santaPrev;
	}
	
	
	public HashMap<String,String> readPrevPrevSanta(){
		HashMap<String,String> santaPrevPrev = new HashMap<String,String>();
		int prevYear = year-2;
		fileName = filepath + "/santas" + String.valueOf(prevYear);
		
		Properties defaultProps = new Properties();
		
		try {
			FileInputStream in = new FileInputStream(fileName);
			defaultProps.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 Enumeration e = defaultProps.propertyNames();
		 while (e.hasMoreElements()) {
		      String key = (String) e.nextElement();
		      santaPrevPrev.put(key, defaultProps.getProperty(key));
		      
		    }
		 
		 return santaPrevPrev;
		
	}

}
