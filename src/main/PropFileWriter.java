package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

public class PropFileWriter {
	
	private String fileName = "";
	//String filepath = "/Users/jain/Documents/Smriti/SantaProperties";
	String filepath = ".";
	

	public void storeToFile(HashMap<String,String> santas){
		
		getFilename();
		//checkIfexists();
		
		try {
			Properties properties = new Properties();
			
			for(HashMap.Entry<String, String> entry : santas.entrySet()){
			
				properties.setProperty(entry.getKey(), entry.getValue());
			}

			//File file = new File(fileName);
			FileOutputStream fileOut = new FileOutputStream(fileName);
			properties.store(fileOut, "Santa List");
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getFilename(){
		Calendar now = Calendar.getInstance();
		int year = now.getWeekYear();
		fileName = filepath + "/santas" + String.valueOf(year);
			
	}
			
}
