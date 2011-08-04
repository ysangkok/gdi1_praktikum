package gruppe16;
import java.io.Serializable;

public class Score  implements Serializable {
    private int score;
    private String naam;
    private String neededTime;
    private String date;

    public int getScore() {
        return score;
    }

    public String getNaam() {
        return naam;
    }

    
    
    public String getNeededTime(){
    	
    	return neededTime;
    }
    
    
    public String getDate(){
    	
      return date;
    }



    public Score(String naam, int score, String neededTime, String date) {
        this.score = score;
        this.naam = naam;
        this.neededTime= neededTime;
        this.date= date;   
    }

	
	
    
    
}
 