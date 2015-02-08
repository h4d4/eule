package parser;

import java.io.*;
public class Reader {
	public static File fileIn = null, dirOut = null; 
	public static String test;
	/**
	 * Function ReadPath
	 * Gives directory class to analize
	 * @throws IOException 
	 */
	private static void ReadPath() throws IOException{
		String inputs;
		InputStreamReader lector = new InputStreamReader(System.in); 
		BufferedReader buffer = new BufferedReader( lector );
		System.out.println( "Ingrese ruta clase.java@dir-salida \n" );
		try{
			inputs = buffer.readLine();
			if( inputs != null ){
				fileIn = new File( inputs.split("@")[0] );  test = inputs.split("@")[0];
				dirOut = new File( inputs.split("@")[1] );
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	private static void ReadFile(){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));			
		} catch (FileNotFoundException e) { e.printStackTrace(); System.exit(-1); }
		
		String line = null;
		try {
			do {
				 line = in.readLine();
				 if(line != null) {
					 String[] tokens = line.split("//")[0].split("\\s+");
					 for( int i=0; i<tokens.length; i++ ){
						 if( tokens.length > 0 )
						 {
							System.out.println(tokens.length); 	
						 }
							 
					 }
			      }
			} while (line != null);	
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
	///home/h4d4/Escritorio/implicit-test-sig-src/program-android-src/ImplicitFlow1.java@/home/h4d4/Escritorio/implicit-test-sig-src/program-android-src/
		ReadPath();
		ReadFile();
	}
}
