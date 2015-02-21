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
			//System.out.println("Annotation-fileIn: "+ fileIn);
			System.out.println("Annotation-fileOUT: "+ dirOut);
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
	public static void instanceContext(String instance ) throws IOException{
		int pos = 0;
		boolean add = false;
		char tmp[] = instance.trim().toCharArray();
		String fc =  Character.toString( tmp[0] ).toLowerCase();
		String nl = "static "+instance+ " "+fc+instance.substring(1)+';';
		
		for( int i =0; i<fileCont.size(); i++){
			String line = fileCont.get(i);
			int extend = line.indexOf("extends");
			 if( extend > -1 ){
				 add=true;
				 pos = i;
			 }
		}
		if( add )
			fileCont.set(pos+1,nl);
		
		for( int i =0; i<fileCont.size(); i++){
			String line = fileCont.get(i);
			if( line.indexOf("(TelephonyManager)") >-1  && line.indexOf("getSystemService") >-1 ){
				String s = line.substring(line.indexOf("getSystemService"));
				String f = line.substring(0, line.indexOf("getSystemService"));
				char tmp1[] = instance.trim().toCharArray();
				String fc1 =  Character.toString( tmp1[0] ).toLowerCase();
				String inst = fc1+instance.substring(1)+".";
				String fs = f+inst+s;
				fileCont.set(i, fs);
			}
		}
	}
	public static void instanceTelephony( String instance ) throws IOException{
		int pos = 0;
		String nl = "import android.content.Context;" ;
		boolean add = false;
		for( int i =0; i<fileCont.size(); i++){
			String line = fileCont.get(i);
			 if( line.indexOf("import") > -1 ){
				 add=true;
				 pos = i;
			 }
		}
		if( add )
			fileCont.set(pos+1,nl);
		
		
		int posInst = 0;
		boolean addInst = false;
		char tmpInst[] = instance.trim().toCharArray();
		String fcInst =  Character.toString( tmpInst[0] ).toLowerCase();
		String nlInst = "static "+instance+ " "+fcInst+instance.substring(1)+';';
		
		for( int i =0; i<fileCont.size(); i++){
			String lineInst = fileCont.get(i);
			int extendInst = lineInst.indexOf("extends");
			 if( extendInst > -1 ){
				 addInst=true;
				 posInst = i;
			 }
		}
		if( addInst )
			fileCont.set(posInst+1,nlInst);
		
		for( int i =0; i<fileCont.size(); i++){
			String line = fileCont.get(i);
			if( line.indexOf("(TelephonyManager)") >-1  && line.indexOf("getSystemService") >-1 && line.indexOf("this") >-1 ){
				String f = line.substring(0, line.indexOf("this"));
				String s = line.substring(line.indexOf("this")+4);
				char tmp[] = instance.trim().toCharArray();
				String fc =  Character.toString( tmp[0] ).toLowerCase();
				String div1 = fc+instance.substring(1)+s.split("\\(")[0];
				String div2 = instance+"."+s.split("\\(")[1];
				fileCont.set(i,f+div1+"("+div2);
			}
		}
	}
	
	//busca la variable y la anota
	public static void findVarSource( String nameVar ){
		for( int i=0; i< fileCont.size(); i++ ){
			 String line = fileCont.get(i), tokens[];
			 int initVar = line.indexOf(nameVar);
			 boolean esVar = false;
			 if( initVar > -1 ){
				 if( line.indexOf("=") > -1 ){	//si la var source está inicializada
					 tokens = line.split("="); 
					 if( tokens.length == 2 ){
						 String f[] = tokens[0].split("\\s+"); 
						 if( f[f.length-1].equals(nameVar)){
							 if( f[0].isEmpty() ){
								 if( f.length ==3  || f.length ==4 || f.length ==5){ 
									 char tmp[] = f[f.length-2].trim().toCharArray(); 
					 				 String t = Character.toString(tmp[0]).toUpperCase();
					 				 if( Character.toString(tmp[0]).equals(t) ){ 
					 					 esVar = true;}
								 }
							 }else{
								 	if( f.length ==2  || f.length ==3 || f.length ==4){
								 		char tmp[] = tokens[tokens.length-2].trim().toCharArray();
						 				 String t = Character.toString(tmp[0]).toUpperCase();
						 				 if( Character.toString(tmp[0]).equals(t) )
						 					 esVar = true;
								 }
							 }
						 }
					 }
				 }else{	//si var source no esta inicializada 
					 	tokens = line.split(";")[0].split("\\s+"); 
					 	if( tokens[tokens.length-1].equals(nameVar) ){
					 		if( tokens[0].isEmpty() ){
					 			 if( tokens.length ==3  || tokens.length ==4 || tokens.length ==5){
					 				 char tmp[] = tokens[tokens.length-2].trim().toCharArray();
					 				 String t = Character.toString(tmp[0]).toUpperCase();
					 				 if( Character.toString(tmp[0]).equals(t) )
					 					  esVar = true;
								 }
					 		}else{
					 			if( tokens.length ==2  || tokens.length ==3 || tokens.length ==4){
					 				char tmp[] = tokens[tokens.length-2].trim().toCharArray();
					 				 String t = Character.toString(tmp[0]).toUpperCase();
					 				 if( Character.toString(tmp[0]).equals(t) )
					 					esVar = true;
					 			}
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

	public static void commenOverride(){
		for( int i =0; i<fileCont.size(); i++){
			if( fileCont.get(i).indexOf("@Override") >-1 )
				fileCont.set(i, "//"+fileCont.get(i));
		}
	}
	//saber si importa TelephonyManager y Context
	public static void typeContext() throws IOException{
		boolean telephonyManager =false, context = false;
		for( int i=0; i<Annotation.instances.size(); i++){
			if( Annotation.instances.get(i).equals("TelephonyManager") ){
				telephonyManager = true;
				break;
			}
		}
		for( int i=0; i<Annotation.instances.size(); i++){
			if( Annotation.instances.get(i).equals("Context") ){
				context = true;
				break;
			}
		}
		
		if( context &&  telephonyManager ){	//importa ambas clases
			instanceContext("Context"); 
		}
		else if( !context &&  telephonyManager ){
			//Annotation.AddImports("android.content.Context");
			instanceTelephony( "Context" );
		}
	}
	
	public static void arraysMethodsSources(){//anotat arrays methodos sources
		for( String as : Annotation.arraysSources )
			for( int i=0; i<fileCont.size(); i++ ){
				boolean change = false;
				String line = fileCont.get(i), union = "";
				if( line.indexOf(as+"*") > -1){
					//System.out.println(">>>>>>>>>>>>                  "+line+"         >>>>>>>>>>>> ");
					String f = line.split("=")[0];
					String s = line.split("=")[1];
					String div[] = f.split("\\[\\]"); 
					String div0 = div[0]+"{Alice:}";
					String div1="{Alice:}"+div[1].substring(0, div[1].length()-2);
					union = div0+"[]"+div1+" = "+s;
					change = true;
					//System.out.println(">>>>>>>>>>>> "+f+">>>>>>>>>>>> "+div[0]+"  "+div[1]+"  "+div.length);
					//System.out.println(">>>>>>>>>>>>                  "+union+"         >>>>>>>>>>>> ");
				}
				if( change )
					fileCont.set(i, union);
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
		//addInstances() ;
		typeContext();
		commenOverride();
		arraysMethodsSources();
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
