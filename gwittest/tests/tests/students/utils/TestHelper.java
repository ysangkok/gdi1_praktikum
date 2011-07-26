package tests.students.utils;

public class TestHelper {
	
	
	/**
	 * compares two level strings. 
	 * if the both levels differ only in exactly maxDiffer positions the method will
	 * return true, if there were more or less differences it will return false.
	 * 
	 * All differences has to be on the players half (means before the '|' char in a row).
	 * 
	 * @param lvlone
	 * 			the first level representation
	 * @param lvltwo
	 * 			the second level representation
	 * @param maxDiffer
	 * @return
	 */
	public static boolean compareLevel(String lvlone, String lvltwo, int maxDiffer) {
		lvlone = lvlone.trim();
		lvltwo = lvltwo.trim();
		if (lvlone.length() != lvltwo.length()) {
			return false;
		}
		
		boolean playersHalf = true;
		for (int i = 0; i < lvlone.length(); i++) {
			//chars are equal:
			if ((lvlone.charAt(i) == lvltwo.charAt(i))) {
				if (lvlone.charAt(i) == '|') {
					playersHalf = false;
				} else if (lvlone.charAt(i) == '\n') {
					playersHalf = true;
				}
			} else {
				//chars differ:
				if (playersHalf) {
					//we found a difference on the players half, is there one allowed differences missing?
					if (maxDiffer <= 0) {
						return false;
					} else {
						maxDiffer--;
					}
				} else {
					//we found a difference on the computers half, thats illegal:
					return false;
				}
			}
		} 
		return maxDiffer == 0;
		
	}

}
