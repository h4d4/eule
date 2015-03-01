package cu;

import japa.parser.ParseException;
import japa.parser.ast.expr.Expression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
				boolean ctx = false;
				if( line.indexOf("getSystemService")+ 16 <= line.length() ){
					char c = line.charAt(line.indexOf("getSystemService")-1);
					if( c == '.'){
						CharSequence cs = "context.getSystemService";
						if( line.substring(0, line.indexOf("getSystemService")+ 16 ).contains(cs) )
							ctx = true;
					}
				}
				if( ! ctx ){
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
		boolean esArray = false;
		if( nameVar.indexOf('[') >-1  && nameVar.indexOf(']') >-1 ){
			esArray = true;
		}
		if( esArray ){//revision para arrays 
			boolean initialized = false, declared = false;
			nameVar = nameVar.split("\\[")[0];
			for( int i=0; i< fileCont.size(); i++ ){
				 String line = fileCont.get(i), tokens[] = null, t0[], type = "boolean|byte|char|short|int|float|long|double";
				 String identifier = "[A-Za-z][\\w]*", head = null, complete;
				 int initVar = line.indexOf(nameVar);
				 if( initVar > -1 ){
					 if( line.indexOf("=") > -1 ){	//si la var source esta inicializada
						 tokens = line.split("="); 
						 if( tokens.length == 2 ){
							 t0 = tokens[0].split("\\s+");
							 if( t0.length == 3 ){ 
								 if( (t0[1].matches(type+"\\[\\]") || t0[1].matches(identifier+"\\[\\]")) && t0[2].matches(nameVar) ){
									 head = (t0[1].split("\\[")[0])+"{Alice:}[] "+t0[2];
									 initialized = true;
								 }else if( (t0[1].matches(type) || t0[1].matches(identifier)) && t0[2].matches(nameVar+"\\[\\]") ){
									 head = t0[1]+"{Alice:} "+t0[2];
									 initialized = true;
								 }
							 }
						 } 
					 }else{	//si var source no esta inicializada 
						 tokens = line.split(";")[0].split("\\s+");
						 if( tokens[tokens.length-1].equals(nameVar) && (tokens[tokens.length-2].matches(type+"\\[\\]") || tokens[tokens.length-2].matches(identifier+"\\[\\]")) ){
							 String penult = tokens[tokens.length-2].split("\\[")[0]+"{Alice:}[]";
							 head = line.substring(0, line.indexOf(tokens[tokens.length-2]))+penult+tokens[tokens.length-1]+";"; 
							 declared = true;
						 }else if( tokens[tokens.length-1].equals(nameVar+"[]") && (tokens[tokens.length-2].matches(type) || tokens[tokens.length-2].matches(identifier))){
							 String penult = tokens[tokens.length-2];
							 head = line.substring(0, line.indexOf(penult))+penult+"{Alice:} "+tokens[tokens.length-1]+";";
							 declared = true;
						 }
					 }
				 }
				 if( initialized ){
					 if( head!=null && tokens!=null ){
						 complete = head + " = "+tokens[1];
						 fileCont.set(i, complete);
					 }
				 }
				 if( declared ){
					 if( head!=null && tokens!=null ){
						 fileCont.set(i, head); 
					 }
				 }
			}	
		}
		for( int i=0; i< fileCont.size(); i++ ){
			 String line = fileCont.get(i), tokens[];
			 String type = "boolean|byte|char|short|int|float|long|double";
			 String identifier = "[A-Za-z][\\w]*";
			 int initVar = line.indexOf(nameVar);
			 boolean esVar = false;
			 if( initVar > -1 ){
				 if( line.indexOf("=") > -1 ){	//si la var source esta inicializada
					 tokens = line.split("="); 
					 if( tokens.length == 2 ){
						 String f[] = tokens[0].split("\\s+"); 
						 if( f[f.length-1].equals(nameVar)){		//tomar penultima posicion
							 if( f[f.length-2].matches(type) | f[f.length-2].matches(identifier) )
								 esVar = true;
						 }
					 }
				 }else{	//si var source no esta inicializada 
					 tokens = line.split(";")[0].split("\\s+"); 
					 if( tokens[tokens.length-1].equals(nameVar) ){
						 if( tokens[tokens.length-2].matches(type) | tokens[tokens.length-2].matches(identifier))
							 esVar = true;;
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
	
	public static void cleanEditText(){ //actua solo cuando existe EditText
		ArrayList<String>varsToFound = new ArrayList<String>(), varstoMod = new ArrayList<String>();
		CharSequence cs1 = "EditText", cs2 = "(EditText)";
		for( int i=0; i<fileCont.size(); i++ ){
			String line  = fileCont.get(i);
			if( line.contains(cs1) && line.contains(cs2) ){
				String tmp [] = line.split("=")[0].split("\\s+");
				varsToFound.add(tmp[2]);
				fileCont.set(i, "//"+line);
			}
		}
		if( varsToFound.size() >= 1  ){
			Map<Integer,String>addLinesComment = new HashMap<Integer,String>();
			for( int i=0; i<fileCont.size(); i++ ){
				String line  = fileCont.get(i);
				for( int j=0; j< varsToFound.size(); j++){
					CharSequence cseq1 = varsToFound.get(j)+".getText().toString()";
					CharSequence cseq2 = varsToFound.get(j)+".getText()";
					CharSequence cseq3 = varsToFound.get(j)+".toString()";
					if( line.contains(cseq1) || line.contains(cseq2) || line.contains(cseq3) ){
						String tmp[]=line.split("=");
						if( tmp.length == 2 ){
							String tpm2[] = tmp[0].split("\\s+");
							varstoMod.add( tpm2[tpm2.length-1] );
							fileCont.set(i, line.split("=")[0]+" = \"pwd\"; ");
							addLinesComment.put(i, "//"+line);
						}
					}
			 }
		}
		if( !addLinesComment.isEmpty() ){
			for (Map.Entry<Integer, String> entry : addLinesComment.entrySet()) {
				fileCont.add(entry.getKey(), entry.getValue());
			}
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
		//addInstances() ;
		typeContext();
		commenOverride();
		arraysMethodsSources();
		//limpiar EditText antes de imprimir
		cleanEditText();
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
