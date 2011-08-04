package gruppe16;
import java.util.*;
import java.io.*;

public class HighscoreManager {
    // An arraylist of the type "score" we will use to work with the scores inside the class
    private ArrayList<Score> scores;

    // The name of the file where the highscores will be saved
    private static final String HIGHSCORE_FILE = "scores.dat";

    //Initialising an in and outputStream for working with the file
    ObjectOutputStream outputStream = null;
    ObjectInputStream inputStream = null;

    public HighscoreManager() {
        //initialising the scores-arraylist
        scores = new ArrayList<Score>();
    }
    
   static int max = 10;
    
public ArrayList<Score> getScores() {
        loadScoreFile();
        sort();
        return scores;
}
    
    

private void sort() {
     ScoreComparator comparator = new ScoreComparator();
     Collections.sort(scores, comparator);
}


public void addScore(String name, int score, String neededTime, String date) {
       loadScoreFile();
       //Score(String naam, int score, String neededTime, String date) 
       Score s = new Score(name, score, neededTime, date);
       scores.add(s);
       updateScoreFile();
}

public void loadScoreFile() {
    try {
        inputStream = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE));
        scores = (ArrayList<Score>) inputStream.readObject();
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


    ArrayList<Score> scores;
    scores = getScores();

    //int i = 0;
    int x = scores.size();
    if (x > max) {
        x = max;
    }
   // while (i < x) {
     
    for(int i=0; i<x; i++){
    highscoreString += (i + 1) + ".\t" + scores.get(i).getNaam() + "\t\t" + scores.get(i).getScore() +
        		"\t" + scores.get(i).getNeededTime() + "\t" + scores.get(i).getDate() + "\n";
       // i++;
    }
    return highscoreString;
}




public static class Main {
    public static void main(String[] args) {
        HighscoreManager hm = new HighscoreManager();
        hm.addScore("Bart",240, "30",   "01.08.2011   08:01"  );
        hm.addScore("Marge",300, "22",  "11.08.2011   09:01" );
        hm.addScore("Maggie",220, "45", "01.07.2011   10:01" );
        hm.addScore("Homer",100, "33",  "21.08.2011   11:01" );
        hm.addScore("Lisa",270,  "22",  "01.11.2011   08:01" );

        System.out.print(hm.getHighscoreString());
    }
}
}