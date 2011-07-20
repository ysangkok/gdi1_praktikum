package gruppe16;


import java.io.*;




public class DataOpp extends HighScore {
	 public static void main (String[] args) throws IOException{
	 	    
		
		
				
		 
		 
		 String text = "Dieser Text wird in einer Datei gespeichert!";
		    String dateiName = "Score.txt";
		    FileOutputStream writeStrom = 
		                     new FileOutputStream(fileName);
		    for (int i=0; i < text.length(); i++){
		      writeStrom.write((byte)text.charAt(i));
		    }
		    schreibeStrom.close();
	
		  }
		}



public class LeseAusDatei {

	  public static void main (String[] args) throws IOException{
	     byte zeichen;
	     char buchstabe;
	     String text = "";
	     String dateiName = "Test.txt";
	     FileInputStream leseStrom = new FileInputStream(dateiName);
	     do{
	       zeichen = (byte)leseStrom.read();
	       System.out.print(zeichen+" ");
	       text += (char)zeichen;
	     } while (zeichen !=-1);
	     leseStrom.close();
	     System.out.println();
	     System.out.println(text);
	  }
	}
	
	
	
	
}
