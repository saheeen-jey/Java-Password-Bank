

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Util {
	public static void createTextFile(String fileName) {
		try {
		      File file = new File(fileName);
		      if (file.createNewFile()) {
		        System.out.println("File created: " + file.getName());
		      } else {
		        System.out.println("File already exists. (" + file.getName() + ")");
		      }
		      
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	
	public static void writeToFile(String fileName, String msg) {
		try {
			//Set append the text file to true.
		    	FileWriter myWriter = new FileWriter(fileName,true);
		     //Write the msg into the text file and write true.
		    	myWriter.write(msg +"\n");
		    //Close the writer.
		    	myWriter.close();
		    	System.out.println();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		  }
	
	public static String[] readATextFile(String fileName) {
		        File file = new File(fileName);
		        int counter = 0;
		        
		        BufferedReader br = null;
		        
				try {
					br = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
		        
		        try {
		            
		            while (br.readLine() != null) {
		            	
		                counter++;
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        String[] lines = new String[counter];
		        
		        
		        try {
					br = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		        
		        counter = 0;
		        try {
		            for(int i =0; i < lines.length; i++) {
		            	lines[i] = br.readLine();
		            }
		            //Read to move cursor down one more line.
		            br.readLine();
		        }
		        
		        catch (IOException e) {
		            e.printStackTrace();
		        }
		        return lines;
	}
	
	public static void insertLine(String fileName, int startingLine, String msg) {
		try {
	    	String inFile = fileName;
	    	int counter = 0;
	    	StringBuffer newContent = new StringBuffer();
	    	String line;
	    	BufferedReader br = new BufferedReader(new FileReader(inFile));
	    	while ((line = br.readLine()) != null) {
	    		counter++;
	    		if (counter == startingLine) {
	    			newContent.append(msg);
	    			newContent.append("\n"); // new line
	    		}
	    		newContent.append(line);
	    		newContent.append("\n"); // new line
	    	}
	    	

	    	FileWriter removeLine = new FileWriter(inFile);
	    	BufferedWriter change = new BufferedWriter(removeLine);
	    	PrintWriter replace = new PrintWriter(change);
	    	replace.write(newContent.toString());
	    	replace.close();
	    	br.close();
	    }
	     catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void deleteLines(String fileName, int startingLine, int numOfLines) {
		 try {
		    	String inFile = fileName;
		    	int counter = 0;
		    	StringBuffer newContent = new StringBuffer();
		    	String line;
		    	BufferedReader br = new BufferedReader(new FileReader(inFile));
		    	while ((line = br.readLine()) != null) {
		    		counter++;
		    		if (counter == startingLine) {
		    			for(int i =0; i < numOfLines - 1; i++) {
		    				newContent.append("");
		    				br.readLine();
		    				counter++;
		    			}
		    			continue;
		    		}
		    		
		    		newContent.append(line);
		    		newContent.append("\n"); // new line
		    	}
		    	br.close();

		    	FileWriter removeLine = new FileWriter(inFile);
		    	BufferedWriter change = new BufferedWriter(removeLine);
		    	PrintWriter replace = new PrintWriter(change);
		    	replace.write(newContent.toString());
		    	replace.close();
		    }
		     catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	
	//https://www.tutorialspoint.com/how-to-overwrite-a-line-in-a-txt-file-using-java#:~:text=Invoke%20the%20replaceAll()%20method,(new%20line)%20as%20parameters.&text=Instantiate%20the%20FileWriter%20class.,using%20the%20append()%20method.
	public static void replaceLine(String fileName, String newMsg, int lineNumber) {
	    try {
	    	String inFile = fileName;
	    	int counter = 0;
	    	StringBuffer newContent = new StringBuffer();
	    	String line;
	    	BufferedReader br = new BufferedReader(new FileReader(inFile));
	    	while ((line = br.readLine()) != null) {
	    		counter++;
	    		if (counter == lineNumber) {
	    			newContent.append(newMsg);
	    			newContent.append("\n"); // new line
	    		}
	    		else {
	    			newContent.append(line);
	    			newContent.append("\n"); // new line
	    		}
	    	}
	    	br.close();

	    	FileWriter removeLine = new FileWriter(inFile);
	    	BufferedWriter change = new BufferedWriter(removeLine);
	    	PrintWriter replace = new PrintWriter(change);
	    	replace.write(newContent.toString());
	    	
	    	replace.close();
	    }
	     catch (Exception e) {
	        e.printStackTrace();
	    }

	}
	
	public static String readOneLine(String fileName, int lineNumber) {
	    try {
	    	String inFile = fileName;
	    	int counter = 0;
	    	String line;
			BufferedReader br = new BufferedReader(new FileReader(inFile));
	    	while ((line = br.readLine()) != null) {
	    		counter++;
	    		if (counter == lineNumber) {
	    			br.close();
	    			return line;
	    		}
	    	}
	    	br.close();
	    
	    }
	     catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	    
	}
	
	public static boolean fileExists(String filePathString) {
		File f = new File(filePathString);
		if(f.exists() && !f.isDirectory()) { 
			return true;
		}
		else
			return false;
	}
	
	
	//https://stackoverflow.com/questions/3987921/not-able-to-delete-the-directory-through-java
	public static boolean deleteFile(String pathName) {
		File path = new File(pathName);
		try {
			return path.delete();
	    }
	    catch(Exception e) {
		
		
		if (path.exists()) {
	        File[] files = path.listFiles();
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isDirectory()) {
	            	deleteFile(files[i].getName());
	            } else {
	                files[i].delete();
	            }
	        }
	    }
	    return (path.delete());
	    }
	}
	
	public static int linearSearch(String arr[], String x) 
	{
		for(int i =0; i < arr.length; i++) {
			if(arr[i].equals(x)) { 
				return i+1;
			}
		}
		return -1;
	}
	
	public static boolean arePasswordsTheSame(char[] pass1, char[] pass2) {
		
		if(pass1.length != pass2.length)
			return false;
		
		for(int i =0; i < pass1.length; i++ ) {
				if(pass1[i] != pass2[i])
					return false;
		}
		return true;
	}
	
}
