package gruppe16;


import java.util.Comparator;



public abstract class CompareToVar extends HighScore {

	super(name, score, startTime, endTime, neededTime, endOfGame);
  	int  parm1;
    int  parm2;
	
    
  /**
   * 
   * Constructor defualt 
   */

	 public CompareToVar(){
		 
		
	 }
	
 
 
 
 
	  @Override
	  public int compareTo(parm1 p1, parm2 p2) {
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

	
	

