package gruppe16;

public class SchiffeVersenken {
	public static void main ( String[] args ) {
		
		String text = 
						"lr--t---lr|lr-------t\n"+
						"----b-----|---lhr---b\n"+
						"----------|----------\n"+
						"--t--lhhr-|------lhr-\n"+
						"--v-------|--lhr-----\n"+
						"--v-lhhr--|t---------\n"+
						"--v------t|v-t--lhhr-\n"+
						"--b-lhr--b|v-v-------\n"+
						"----------|v-v--t----\n"+
						"lhr----lhr|b-b--b--lr\n";
		
		Level lp;
		try {
			lp = new Level(text);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Character[][] p1lvlarr = lp.getPlayerBoard(0);

		Map2DHelper<Character> helper = new Map2DHelper<Character>();
		helper.print(p1lvlarr);
		
		System.out.println(lp.toString());
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

