package gruppe16;

public class SchiffeVersenken {
	public static void main ( String[] args ) {
		LevelParser lp = new LevelParser("01.lvl");
		Character[][] lvlarr = lp.getLevelArr();

		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		helper.print(lvlarr);
	}
	
	static class Map2DHelper<T> {
	    public void print(T[][] a) {
	          for (int i = 0; i < a.length; i++) {
	                  for (int j = 0; j < a[i].length; j++) {
	                          System.out.format("%1$3s", a[i][j]);
	                  }
	                  System.out.print("\n");
	          }
	          System.out.print("\n");
	    }
	}
}

