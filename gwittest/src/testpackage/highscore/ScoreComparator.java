package testpackage.highscore;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ScoreComparator implements Comparator<Score> {
		private String criteria;

		public ScoreComparator(String sortingcritiria) {
			this.criteria = sortingcritiria;
		}
	
        public int compare(Score score1, Score score2) {

            if (criteria.equals("score")) {
            	return ((Integer) score1.getScore()).compareTo(score2.getScore());
            } else if (criteria.equals("name")) {
            	return score1.getName().compareTo(score2.getName());
            } else if (criteria.equals("neededTime")) {
            	return ((Integer) score1.getNeededTime()).compareTo(score2.getNeededTime());
            } else if (criteria.equals("date")) {
            	return score1.getDate().compareTo(score2.getDate());
            } else {
            	throw new RuntimeException("Invalid criteria: " + criteria);
            }
           
        }
}