package net.tigerstudios.RPGCraft.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CTextFileWriter{
	private File fName = null;
	private FileWriter fWriter = null;
	
	
	public void writeLine(String line)
	{
		try {
			fWriter.append(line);
		} catch (IOException e) { e.printStackTrace(); }		
	}
	
	public CTextFileWriter(String filename)
	{	fName = new File(filename);
		
		try {
			if(!fName.exists())
				fName.createNewFile();			
		
			fWriter = new FileWriter(fName);
			
		} catch (IOException e) { e.printStackTrace(); }
		
	} // public CTextFileWriter(String filename)
} // public class CTextFileWriter
