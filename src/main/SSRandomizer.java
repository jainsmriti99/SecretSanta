package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SSRandomizer {

	HashMap<String,String> secretSantasPrev = new HashMap<String,String>();
	HashMap<String,String> secretSantasPrevPrev = new HashMap<String,String>();

	private  ArrayList<String> listToPick = new ArrayList<String>();
	private  List<String[]> famLists = new ArrayList<String[]>();

	//default
	public SSRandomizer(ArrayList<String> gifters){
		this.listToPick = gifters; 
	}

	/*
	 * @param file that has list of names
	 * File example-
	 * Families:2
	 * Steve,Maria,Christopher,Aliya
     * Joe,Erin,Rhea
     * Jack
     * Sara
     * Paul
     * Eric
	 */
	public SSRandomizer(String filename){

		File file = new File(filename);

		try {

			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.indexOf(':') >= 0){

					int numFamilies = Integer.valueOf(line.substring(line.indexOf(':')+1));
					for(int k = 1; k <= numFamilies; k++){
						String famNames = sc.nextLine();
						String[] famList = famNames.split(",");
						famLists.add(famList);
						listToPick.addAll(Arrays.asList(famList));
					}
				}else
					listToPick.add(line);
			}
			sc.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Intiate in calling class. Exists separately to assist testing.
	 */
	public void init(){
		//Load previous santas
		setPreviousSantas(null, null, true);
	}

	/*
	 * Random Secret Santa picker
	 * @param - List of names participating in Secret Santa
	 * @returns - Hashmap of Secret santa and the recipient names  
	 */
	public HashMap<String,String> getSanta() throws IllegalArgumentException{
		if(listToPick.size() < 2)
			throw new IllegalArgumentException("List must have two or more names.");

		HashMap<String,String> secretSantas = new HashMap<String,String>();

		//Create a fixed array of names
		String[] namesList = new String[listToPick.size()];
		namesList = listToPick.toArray(namesList);

		//Initiate list of names already picked
		ArrayList<String> namesPicked = new ArrayList<String>();

		for (int i =0; i<namesList.length; i++){
			String picker = namesList[i];
			System.out.println("Picker is: " + picker);

			//remove from list previously existing santas (last 2yrs.) for this person
			listToPick = removePrevSanta(listToPick, picker);

			//remove family names from list
			removeFamily(listToPick, picker);

			//Cannot be their own santa
			if(listToPick.contains(picker))
				listToPick.remove(picker);

			//Now pick name from trimmed list
			int max = listToPick.size() -1;
			int pickInt = (int) ((Math.random() * max) + 1);
			String name = listToPick.get(pickInt-1);
			System.out.println("You are secret Santa to: " + name );
			listToPick.remove(name);

			//Add name to picked list
			namesPicked.add(name);

			/*Now build back the pickList*/

			//If picker had not already been picked, add back
			if(!namesPicked.contains(picker))
				listToPick.add(picker);

			//Add back previous santas
			listToPick = addPrevSanta(listToPick, picker);

			//Add back family names
			listToPick = addFamily(listToPick, picker,namesPicked);

			//Add santa/name to map
			secretSantas.put(picker, name);

		}

		PropFileWriter pf = new PropFileWriter();
		pf.storeToFile(secretSantas);

		return secretSantas;
	}

	/*
	 * @param list to be updated, secret santa name
	 * @returns updated list
	 */
	private ArrayList<String> removePrevSanta(ArrayList<String> listToPick, String name){
		String prevSanta = "";
		//remove last year santa for this picker
		if(!secretSantasPrev.isEmpty()){
			prevSanta = secretSantasPrev.get(name);
			listToPick.remove(prevSanta);
		}
		//remove santa from 2 yrs back
		if(!secretSantasPrevPrev.isEmpty()){
			prevSanta = secretSantasPrevPrev.get(name);
			listToPick.remove(prevSanta);	

		}
		return listToPick;

	}

	/*
	 * @param list to be updated, secret santa name
	 * @returns updated list
	 */
	private ArrayList<String> addPrevSanta(ArrayList<String> listToPick, String name){
		String prevSanta;
		//add last year santa back to list
		if(!secretSantasPrev.isEmpty()){
			prevSanta = secretSantasPrev.get(name);
			listToPick.add(prevSanta);
		}
		//add 2 ys back santa to the list
		if(!secretSantasPrevPrev.isEmpty()){
			prevSanta = secretSantasPrevPrev.get(name);
			listToPick.add(prevSanta);

		}
		return listToPick;
	}

	/*
	 * Setter method for both in-class or calling class
	 * @param previous map of secret santas
	 * @param boolean true, if reading from external source (props file)
	 */
	public void setPreviousSantas(HashMap<String,String> santaPrev, HashMap<String,String> santaPrevPrev, boolean inFile ){
		if (inFile){
			//Call file reader
			PropFileReader pr = new PropFileReader();
			secretSantasPrev = pr.readPrevSanta();
			secretSantasPrevPrev = pr.readPrevPrevSanta();
		}
		else{
			secretSantasPrev = santaPrev;
			secretSantasPrevPrev = santaPrevPrev;

		}		
	}

	/*
	 * @param list to update, name of secret santa
	 * @returns updated pickList
	 */
	private ArrayList<String> removeFamily(ArrayList<String> listToPick, String name){
		//Go through all family lists
		for(int i = 0; i<famLists.size(); i++){
			String[] arr = famLists.get(i);
			//If this person belongs to a family, remove all family members from list
			if(Arrays.asList(arr).contains(name)){
				for(int j =0; j<arr.length; j++){
					listToPick.remove(arr[j]);
				}
			}		
		}

		return listToPick;

	}

	/*
	 * @param list to update, name of secret santa, List of names already picked
	 * @returns updated pickList
	 */
	private ArrayList<String> addFamily(ArrayList<String> listToPick, String name, ArrayList<String> namesPicked){


		//Go through all family lists
		for(int i = 0; i<famLists.size(); i++){
			String[] arr = famLists.get(i);
			//If this person belonged to a family, add all family members back to list
			if(Arrays.asList(arr).contains(name)){
				for(int j =0; j<arr.length; j++){
					if(!listToPick.contains(arr[j]) && !namesPicked.contains(arr[j]))
						listToPick.add(arr[j]);
				}
			}		
		}


		return listToPick;

	}




}
