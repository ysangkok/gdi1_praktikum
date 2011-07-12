package gruppe16;

import java.io.InputStream;

public class LevelParser {

	LevelParser(String lvlname)  {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("/de/tu_darmstadt/gdi1/resources/levels/defaultlevels/" + lvlname);
		
	}

	public Character[][] getLevelArr() {
		return null;
		// TODO Auto-generated method stub
		
	}

	
}

