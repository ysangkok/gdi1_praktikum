package gruppe16;


import java.util.Comparator;



public abstract class CompareToVar extends HighScore {

	super(name, score, startTime, endTime, neededTime, endOfGame);
	int  p1;
int  p2;
	

	 
	
 
 
 
 
	  @Override
	  public int compareTo( p1,  p2) {
	    if (p1.getName() == null && p2.getName() == null) {
	      return 0;
	    }
	    if (p1.getName() == null) {
	      return 1;
	    }
	    if (p2.getName() == null) {
	      return -1;
	    }
	    return p1.getName().compareTo(p2.getName());
	  }
	  
}

	
	

