package gruppe16;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Score  implements Serializable {
    private int score;
    private String name;
    private int neededTime;
    private Date date;

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    
    
    public int getNeededTime(){
    	
    	return neededTime;
    }
    
    
    public Date getDate(){
    	
      return date;
    }

    public String toString () {
    	return getName() + "\t\t" + getScore() +
		"\t" + getNeededTime() + "\t" + getDate();
    }

    public Score(String name, int score, int neededTime, Date date) {
        this.score = score;
        this.name = name;
        this.neededTime= neededTime;
        this.date= date;   
    }

	
	
    
    
}
 