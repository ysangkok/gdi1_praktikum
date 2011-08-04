package gruppe16;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class HighscoreManager {
    // An arraylist of the type "score" we will use to work with the scores inside the class
    private List<Score> scores;

    // The name of the file where the highscores will be saved
    private static final String HIGHSCORE_FILE = "scores.dat";

    //Initialising an in and outputStream for working with the file
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public HighscoreManager() {
        //initialising the scores-arraylist
        scores = new LinkedList<Score>();
    }
    
   static final int max = 10;
    
public List<Score> getScores() {
        //loadScoreFile();
        //sort();
        return scores;
}


    
    

private void sort(String criteria, boolean reverse) {
     Comparator<Score> comparator = new ScoreComparator(criteria);
     
     if (reverse) comparator = Collections.reverseOrder(comparator);
     
     Collections.sort(scores, comparator);
}






public void addScore(String name, int score, int neededTime, String datestring) {
	//loadScoreFile();
	//Score(String naam, int score, int neededTime, String date) 
	Score s;
	try {
		s = new Score(name, score, neededTime, new SimpleDateFormat(DateFormat).parse(datestring));
	} catch (ParseException e) {
		throw new RuntimeException("Invalid date format: " + e.getMessage());
	}
	scores.add(s);

	sort("score", true);

	if (scores.size() > max) {
		((LinkedList<Score>) scores).removeLast();
	}
       //updateScoreFile();
}

public void loadScoreFile() {
    try {
        inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
        scores = (List<Score>) inputStream.readObject();
    } catch (FileNotFoundException e) {
        System.out.println("[Laad] FNF Error: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("[Laad] IO Error: " + e.getMessage());
    } catch (ClassNotFoundException e) {
        System.out.println("[Laad] CNF Error: " + e.getMessage());
    } finally {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            System.out.println("[Laad] IO Error: " + e.getMessage());
        }
    }
}


public void updateScoreFile() {
       try {
           outputStream = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE));
           outputStream.writeObject(scores);
       } catch (FileNotFoundException e) {
           System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
       } catch (IOException e) {
           System.out.println("[Update] IO Error: " + e.getMessage());
       } finally {
           try {
               if (outputStream != null) {
                   outputStream.flush();
                   outputStream.close();
               }
           } catch (IOException e) {
               System.out.println("[Update] Error: " + e.getMessage());
           }
       }
}


public String getHighscoreString() {
    String highscoreString = "";


    //ArrayList<Score> scores;
    //scores = getScores();

    //int i = 0;
    //int x = scores.size();
    //if (x > max) {
    //    x = max;
    //}
   // while (i < x) {
     
    int i = 0;
    for(Score s : scores){
      highscoreString += (i + 1) + ".\t" + scores.get(i).toString() + "\n";
      i++;
    }
    return highscoreString;
}

public void setDateFormat(String string) {
	this.DateFormat = string;
	
}

String DateFormat;


    public static void main(String[] args) {
    	HighscoreManager hm;
    	
    	hm = new HighscoreManager();
    	hm.setScores(new LinkedList<Score>());
    	hm.updateScoreFile();
    	
        hm = new HighscoreManager();
        hm.loadScoreFile();
        hm.setDateFormat("dd.MM.yyyy   HH:mm");
        hm.addScore("Bart",240, 30,   "01.08.2011   08:01"  );
        hm.addScore("Marge",300, 22,  "11.08.2011   09:01" );
        hm.addScore("Maggie",220, 45, "01.07.2011   10:01" );
        hm.addScore("Homer",100, 33,  "21.08.2011   11:01" );
        hm.addScore("Lisa",270,  22,  "01.11.2011   08:01" );
        hm.updateScoreFile();
        
        System.out.print(hm.getHighscoreString());
        System.out.println("===");
        
    	hm = new HighscoreManager();
        hm.loadScoreFile();
        hm.setDateFormat("dd.MM.yyyy   HH:mm");
        
        hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
        hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
        hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
        hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
        hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
        hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
        hm.addScore("Lukas",1000, 30,   "05.10.2012   13:37"  );
        System.out.print(hm.getHighscoreString());

        hm.sort("neededTime", false);
        System.out.println("===");
        System.out.print(hm.getHighscoreString());

        
        //for ( Score s: hm.getScores()) {
        //	System.out.println(s.toString());
        //}
    }





	private void setScores(List<Score> object) {
		this.scores = object;
	}





}