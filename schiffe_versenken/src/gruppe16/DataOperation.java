package gruppe16;


import java.io.*;

/**
 * Responsible for reading and writing score data.
 * 
 * @author yanai
 *
 */
public class DataOperation {
	
	/**
	 * 
	 * @param filename
	 * @return
	 */
	public static Object[][] readDataFromFile(String filename) {
		
		Object[][] result = null;
		
		FileInputStream fis;
		
		try {
			
			fis = new FileInputStream(filename);
			
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			result = (Object[][]) ois.readObject();
			
			ois.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param filename
	 * @param data
	 */
	public static void writeDataIntoFile (String filename, Object[][] data) {
		
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(filename);
			
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(data);
			
			oos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}


