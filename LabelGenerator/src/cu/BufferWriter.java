package cu;

import japa.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


//ANOTAR VARIABLES SOURCE E INSTANCIAR CLASES

public class BufferWriter {
	
	public static File fileIn = null, dirOut = null; 
	public static String test;
	static ArrayList<String> fileCont = new ArrayList<String>();
	static BufferedReader in = null;
	
	public static void SetPath(String inFile, String outFile){
		fileIn = new File( inFile );  
		dirOut = new File( outFile.split("\\.")[0]+"-out.jif" );
	}
	
	public static void createBuffer(){
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));			
		} catch (FileNotFoundException e) { e.printStackTrace(); System.exit(-1); }
	}
	
	public static void writeFile( ){
		try
		{
			PrintWriter p = new PrintWriter( dirOut );
			for( int i=0; i< fileCont.size(); i++ )
			{
				p.println( fileCont.get(i));
				p.flush();
			}
			p.flush();
			p.close();
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	
	public static void putFileToArray() throws IOException{
		createBuffer();
		String line = null; 
		do {
			 line = in.readLine(); 
			 
			 if( line != null) {
				 fileCont.add(line);
		     }
		} while (line != null);	
	}

	//busca el inicio de la clase y adiciona una linea para instanciar una clase
	public static void findInitClass(String instance ) throws IOException{
		for( int i =0; i<fileCont.size(); i++){
			String line = fileCont.get(i);
			int extend = line.indexOf("extends");
			 if( extend > -1 ){
				 char tmp[] = instance.trim().toCharArray();
				 String fc =  Character.toString( tmp[0] ).toLowerCase();
				 String nl = "static "+instance+ " "+fc+instance.substring(1)+';';
				 fileCont.set(i+1,nl);
			 }
		}
		for( int i =0; i<fileCont.size(); i++){
			String line = fileCont.get(i);
			if( line.indexOf("(TelephonyManager)") >-1  && line.indexOf("getSystemService") >-1 ){
				String s = line.substring(line.indexOf("getSystemService"));
				String f = line.substring(0, line.indexOf("getSystemService"));
				char tmp[] = instance.trim().toCharArray();
				String fc =  Character.toString( tmp[0] ).toLowerCase();
				String inst = fc+instance.substring(1)+".";
				String fs = f+inst+s;
				fileCont.set(i, fs);
			}
		}
	}
	public static void findTelephonyManager(){
		for( int i =0; i<fileCont.size(); i++){
			
		};
	}
	//busca la variable y la modifica
	public static void findVarSource( String nameVar ){
		for( int i=0; i< fileCont.size(); i++ )
		{
			 String line = fileCont.get(i);
			 String[] tokens;
			 int initVar = line.indexOf(nameVar);
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
			     fileCont.set(i, sb.toString());
			 }
		}
	}
	
	public static void addInstances() throws IOException{
		if( Annotation.instances.size() >= 1 )
		{
			for( int i=0; i<Annotation.instances.size(); i++ ){
				findInitClass(Annotation.instances.get(i) );
			}
		}
			
	}
	
	public static void init(String in, String out) throws IOException{
		SetPath( in, out );
		putFileToArray();
		if( Source.varSources.size() >= 1 ){
			for( int i=0; i<Source.varSources.size(); i++ ){
				String var = Source.varSources.get(i);
				findVarSource( var );
			}
		}
		addInstances() ;
		writeFile( );
		Annotation.clearInstances();
		fileCont.clear();
	}
	/*public static void main(String[] args) throws ParseException, IOException {
		String in = "/home/h4d4/Escritorio/inLabelGenerator/ImplicitFlow1/ImplicitFlow1.java";
		String out = "/home/h4d4/Escritorio/outLabelGenerator/result.java";
		SetPath( in, out );
		putFileToArray();
		findVarSource( "imei");
		findInitClass("static Context context;");
		//findVarSource( "imei");
		writeFile( );
		
		findInitClass("extends");
		writeFile( );
	}*/

}
