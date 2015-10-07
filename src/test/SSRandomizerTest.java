package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.Before;

import main.SSRandomizer;

public class SSRandomizerTest {
	ArrayList<String> names = new ArrayList<String>();
	
	@Before
	public void setUp(){
		//ArrayList<String> names = new ArrayList<String>();
		names.add("Smriti");
		names.add("Rachel");
		names.add("Sara");
		names.add("Joe");
	}
	
	@Test
	public void testSantaIsOther() {
		ArrayList<String> twonames = new ArrayList<String>();
		HashMap<String,String> santas = new HashMap<String,String>();
		twonames.add("Smriti");
		twonames.add("Rachel");
		SSRandomizer sr = new SSRandomizer(twonames);
		santas = sr.getSanta();
		
		//asserts
		assertEquals("Rachel", santas.get("Smriti"));
		assertEquals("Smriti", santas.get("Rachel"));
	}
	
	@Test
	public void testSingleSantaError(){
		ArrayList<String> onename = new ArrayList<String>();
		onename.add("Dylan");
		HashMap<String,String> santas = new HashMap<String,String>();
		SSRandomizer sr = new SSRandomizer(onename);
		boolean thrown = false;
		String message = "";
		
		try{
		  santas = sr.getSanta();
		}catch(IllegalArgumentException ie){
			thrown = true;
			message = ie.getMessage();
		}
		
	    assertTrue(thrown);
	    assertEquals("List must have two or more names.", message);
		
	}
	
	@Test
	public void testNotOwnSantaAndSize() {
		
		HashMap<String,String> santas = new HashMap<String,String>();
		SSRandomizer sr = new SSRandomizer(names);
		santas = sr.getSanta();
		
		//asserts
		assertNotEquals("Smriti", santas.get("Smriti"));
		assertNotEquals("Sara", santas.get("Sara"));
		assertNotEquals("Rachel", santas.get("Rachel"));
		assertNotEquals("Joe", santas.get("Joe"));
		
		assertEquals(4, santas.size());
	}
	
	@Test
	public void testNotDuplicateSanta() {
		
		HashMap<String,String> santas = new HashMap<String,String>();
		
		//set old santas
		HashMap<String,String> oldSanta =  new HashMap<String,String>();
		oldSanta.put("Smriti", "Sara");
		oldSanta.put("Rachel", "Joe");
		oldSanta.put("Joe", "Rachel");
		oldSanta.put("Sara", "Smriti");
		HashMap<String,String> oldSanta2 =  new HashMap<String,String>();
		oldSanta2.put("Smriti", "Joe");
		oldSanta2.put("Rachel", "Sara");
		oldSanta2.put("Joe", "Smriti");
		oldSanta2.put("Sara", "Rachel");
		
		SSRandomizer sr = new SSRandomizer(names);
		sr.setPreviousSantas(oldSanta, oldSanta2, false);
		santas = sr.getSanta();
		
		//asserts
		assertNotEquals("Sara", santas.get("Smriti"));
		assertNotEquals("Joe", santas.get("Smriti"));
		assertNotEquals("Rachel", santas.get("Joe"));
		assertNotEquals("Smriti", santas.get("Joe"));
		
	}

	
	@Test
	public void testNotinFamilyReadFile(){
		
		 HashMap<String,String> santas = new HashMap<String,String>();
		
		String filename = "./SantaNames";
		SSRandomizer sr = new SSRandomizer(filename);
		
		santas = sr.getSanta();
		
		assertNotNull(santas.get("Eric"));
		assertNotNull(santas.get("Joe"));
		assertNotEquals("Steve", santas.get("Maria") );
		assertNotEquals("Steve", santas.get("Christopher") );
		assertNotEquals("Steve", santas.get("Aliya") );
		
		assertNotEquals("Joe", santas.get("Erin") );
		assertNotEquals("Rhea", santas.get("Joe") );
		
		
		
	}
	
}
