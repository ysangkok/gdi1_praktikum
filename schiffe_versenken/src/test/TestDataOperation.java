package test;

import gruppe16.DataOperation;

public class TestDataOperation {
	
	private static String filename = "tests/testObject.obj";
	
	public static void main (String[] args) {
		
		testWriteFile();
		testReadFile();
	}
	
	public static void testReadFile() {
		
		Object[][] data = DataOperation.readDataFromFile(filename);
		
		if(data != null)
		
			for(int i=0; i<data.length;i++) {
				for(int j=0; j< data[i].length; j++) {
					
					System.out.print(data[i][j]);
					
					System.out.print(" ");
				}
					
				System.out.println();
			}
	}
	
	public static void testWriteFile () {
		
		Object[][] data = new Object[2][2];
		
		data[0][0] = 1;
		data[0][1] = 2;
		data[1][0] = 3;
		data[1][1] = 4;
		
		DataOperation.writeDataIntoFile(filename, data);
		
	}
}
