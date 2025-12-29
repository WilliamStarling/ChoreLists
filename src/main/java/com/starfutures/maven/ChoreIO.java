package com.starfutures.maven;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class ChoreIO {
	FileReader fileReader; //create a reference to a file reader
	CSVReader csvReader; //create a reference to a csv reader.
	private List<String[]> itemList = new ArrayList<>();
	private String chorePath = "Inputs/ChorePool.csv"; // the path/name of the chore list file.
	private String peoplePath = "Inputs/PeoplePool.csv"; //the path for the list of people.
	private final String outputPath = "Output/ChoreList.csv";
	private List<String[]> peoplePool;
	private List<String[]> chorePool;
	private boolean includesWorkload;
	private File outputFile;
	private FileWriter fileWriter;
	private CSVWriter csvWriter;
	
	//default constructor with predefined paths.
	ChoreIO()
	{	
		//uses the default file paths.
		this.peoplePool = readCSV(peoplePath);
		this.chorePool = readCSV(chorePath);
		
		sanitizeChoreInput();
		sanitizePeopleInput();
	} 
	//overloaded constructor where file paths are specified.
	ChoreIO(String chorePath, String peoplePath)
	{
		this.chorePath = chorePath;
		this.peoplePath = peoplePath;
		
		this.peoplePool = readCSV(peoplePath);
		this.chorePool = readCSV(chorePath);
		
		sanitizeChoreInput();
		sanitizePeopleInput();
	}
	
	//Reads the specified CSV file.
	private List<String[]> readCSV(String fileToRead)
	{
		try
		{
			fileReader = new FileReader(fileToRead, StandardCharsets.UTF_8); //create a file reader for the file to read.
			
			//allow the csvfilereader to read the csv file.
			csvReader = new CSVReader(fileReader);
			
			itemList = csvReader.readAll();
			
			//printList(itemList);
			
			return itemList;
		}
				
		catch(Exception e)
		{
			e.printStackTrace();
			return itemList;
		}
	}
	
	//Prints the specified list to console, for debug purposes.
	//made it static public so it can be accessed by other classes.
	public static void printList(List<String[]> printThis)
	{
		for (String[] row : printThis)
		{
			for (String cell : row)
			{
				System.out.print(cell + "\t");
			}
			System.out.println();
		}
	}
	
	private void sanitizeChoreInput()
	{
		String[] choresHeader = this.chorePool.get(0);
		this.includesWorkload = true;
		try
		{
			//First, try to make sure the headers match expectations.
			if(choresHeader.length != 2 && choresHeader.length != 3)
			{
				throw new IOException("ERROR: improper ChorePool.csv formatting. Expected 2 or 3 columns.");
			}
			
			if(!(choresHeader[0].toLowerCase().equals("chore to do")) || !(choresHeader[1].toLowerCase().equals("extra notes")))
			{
				throw new IOException("ERROR: 'Chores to Do' and/or 'Extra Description' columns improperly named.");
			}
			
			if(choresHeader.length == 2)
			{
				this.includesWorkload = false;
			}
			else if(choresHeader[2].toLowerCase().equals("Workload"))
			{
				throw new IOException("ERROR: 'Workload' column improperly named.");
			}
			
			//then remove the headers from input.
			this.chorePool.remove(0);
			//I'm going to add an extra blank header to between the header and actual values, to improve readability.
			//This will need to be removed too for it to function, but I want it to work without it so check if it's even there.
			if(this.chorePool.get(0)[0].equals("") || this.chorePool.get(0)[0].isBlank())
			{
				this.chorePool.remove(0);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void sanitizePeopleInput()
	{
		String[] peopleHeader = peoplePool.get(0);
		
		try
		{
			if(peopleHeader.length < 1)
			{
				throw new IOException ("ERROR: Need at least 1 column for PeoplePool.csv input.");
			}
			
			if(!(peopleHeader[0].toLowerCase().equals("housekeepers")))
			{
				throw new IOException ("ERROR: Expected first column to be named 'Housekeepers'.");
			}
			
			peoplePool.remove(0); //remove the header row.
			//I'm going to add an extra blank header to between the header and actual values, to improve readability.
			//This will need to be removed too for it to function, but I want it to work without it so check if it's even there.
			if(this.peoplePool.get(0)[0].equals("") || this.peoplePool.get(0)[0].isBlank())
			{
				this.peoplePool.remove(0);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void outputList(List<String[]> choreList)
	{
		String[] listHeader = {"Chores To Do", "Extra Description", "Housekeeper"};
		String[] blankHeader = {"", "", "", ""};
		
		choreList.add(0, blankHeader);
		choreList.add(0, listHeader);
		
		try
		{
			//create file writing code.
			outputFile = new File(outputPath);
			fileWriter = new FileWriter(outputFile, StandardCharsets.UTF_8);
			csvWriter = new CSVWriter(fileWriter);
			
			csvWriter.writeAll(choreList); //write to the csv file.
			csvWriter.close();  //saves and closes the csv file.
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//GETTER method: returns a list of each person.
	public List<String[]> getPeoplePool()
	{
		return this.peoplePool;
	}
	
	//GETTER method: returns the list of chores.
	public List<String[]> getChorePool()
	{
		return this.chorePool;
	}
	
	//GETTER method: returns whether the  csv file includes a workload.
	public boolean getIncludesWorkload()
	{
		return this.includesWorkload;
	}
}
