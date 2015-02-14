package cu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChangeVarDefinition {
	
	
	public static File fileIn = null, dirOut = null; 
	public static String test;

	
	/**
	 * Function ReadPath
	 * Gives directory class to analize
	 * @throws IOException 
	 */
	public static void ReadInputPath() throws IOException{
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
		buffer.close();
	}
	public static void SetPath(String inFile, String outFile){
		fileIn = new File( inFile );  
		dirOut = new File( outFile );
	}
	public static void findVar( String nameVar ){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));			
		} catch (FileNotFoundException e) { e.printStackTrace(); System.exit(-1); }
		
		String line = null; 
		try {
			File file = dirOut;
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			do {
				 line = in.readLine(); System.out.println("YA");
				 //System.out.println(line.length());
				 
				 if( line != null) {
					 String[] tokens, tmp;
					 ArrayList<String> matches = new ArrayList<String>();
					 int initVar = line.indexOf(nameVar), cs=0;//spaces counter
					 boolean esVar = false;
					 if( initVar > -1 ){
						 tokens = line.split("\\s+");
						 if( tokens.length >=3 ){
							if( tokens[0].length() == 0 ){
								if( tokens[2].equals(nameVar) || tokens[2].equals(nameVar+"=")){
									 esVar = true;
								 }else if( tokens[2].equals(nameVar) && tokens[3].equals("=") ){
								 	esVar = true;
								 }
								 
							}else{
								if( tokens[1].equals(nameVar) || tokens[1].equals(nameVar+"=")){
									 esVar = true;
								 }else if( tokens[1].equals(nameVar) &&  tokens[1].equals("=") ){
								 	esVar = true;
								 }
							}
						 }
					 }
					 
					 if( esVar ){
						 StringBuffer sb = new StringBuffer(line);
					     sb.insert(initVar,"{Alice:}");
					     bw.write(sb.toString());
					     bw.write("\n");
					 }else{
						 bw.write(line); 
						 bw.write("\n");
					 }
					// bw.flush();
			     }
			} while (line != null);	
			bw.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileIn = "/home/h4d4/Escritorio/eule/InputLabelGenerator/ImplicitFlow4.java";
		String fileOut = "/home/h4d4/Escritorio/outLabelGenerator/ImplicitFlow4.java";
		Source.getSources(fileIn);
		SetPath(fileOut,fileOut);
		findVar( "username" );
		/*for( int i=0; i<Source.varSources.size(); i++){
			if( i > 1 ){
				System.out.println("SO");
				SetPath(fileOut,fileOut);
			}else if( i== 0 )
					SetPath(fileIn,fileOut);
			
			findVar( Source.varSources.get(i) );
			
			SetPath(fileIn,fileOut);
			findVar( Source.varSources.get(i) );
		}*/
		
	}

}
