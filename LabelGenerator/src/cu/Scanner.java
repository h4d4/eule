package cu;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.NodeList;

import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class Scanner {
	static CompilationUnit cu;
	static String fileIn;
	//static String methodsOb[] = {"getDeviceId","getSimSerialNumber","findViewById","getLatitude","getLongitude"};
	static ArrayList<String> methodsCalls = new ArrayList<String>(); 
	static Map<String,Boolean> methodsList = new HashMap<String,Boolean>();
	static Map<String,String> varsDeclaration = new HashMap<String,String>();
			//methodsCalls = new HashMap<String,String>();
	static ArrayList<String> varsSources = new ArrayList<String>();	// variables identified as sources
	static ArrayList<String> varsNotDefinition = new ArrayList<String>();
	//Scanner(){}
	
	public static void setFileIn( String inFile ){
		fileIn = inFile;
	}
	public static void fullingMethodsList(){
		methodsList.put("getDeviceId", true);
		methodsList.put("getSimSerialNumber", true);
		methodsList.put("findViewById", true);
		methodsList.put("getLatitude", true);
		methodsList.put("getLongitude", true);
	}
	/**
	 * @param sourceFile
	 * @throws japa.parser.ParseException
	 * @throws IOException
	 * Generates compiler unit to input file
	 */
	public static void generateCu( ) throws japa.parser.ParseException, IOException{
		FileInputStream in = new FileInputStream( fileIn );
		try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
	}
	
	public static class VariableDeclarationVisitor extends VoidVisitorAdapter<Object> {
		  @Override
		  public void visit(VariableDeclarationExpr n, Object arg)
		  {  
		      java.util.List<VariableDeclarator> myVars = n.getVars();
		      for (VariableDeclarator vars: myVars){
		    	  varsDeclaration.put( vars.getId().getName(), vars.getInit().toString());
		    	  
		       }
		  }
	 }
	
	 public static class MethodCallsVisitor extends VoidVisitorAdapter<Object> {
	        @Override
	        public void visit(MethodCallExpr n, Object arg) {
	        	methodsCalls.add( n.toString());
	        	System.out.println( "NameMethod: "+n.getName() + "  Allcall: "+n.toString() );
	        } 
	    }

	//check if name of method called are in methodsList[]
		public static void checkArrayList( ArrayList<String> array ){
			for( int i=0; i<array.size(); i++){
					System.out.println( array.get(i) );
			}
		}
		public static void checkStringMaps( Map<String,String> map ){
			for (Map.Entry<String, String> entry : map.entrySet()) {
			     	System.out.println("Key = " + entry.getKey() + ", "
			     		+ "Value = " + entry.getValue());
			}
		}
		
		public static void checkBooleanMaps( Map<String,Boolean> map ){
			for (Map.Entry<String, Boolean> entry : map.entrySet()) {
		     	System.out.println("Key = " + entry.getKey() + ", "
		     		+ "Value = " + entry.getValue());
			}
		}
		
		public static void varSourcesFound(){
			 //vars no found as sources in variable definitions
			/*for (Map.Entry<String, Boolean> mList : methodsList.entrySet()) {
		     	  String ms = mList.getKey();*/
		     	 /*for (Map.Entry<String, String> varsDec : varsDeclaration.entrySet()) {
		     		 	String tmp;
				     	System.out.println("Key = " + varsDec.getKey() + ", "
				     		+ "Value = " + varsDec.getValue());
		     		 	//System.out.println(ms);
				     	//if ( varsDec.getValue().split("\\.")[1].split("(") )
		     		 	tmp = varsDec.getValue().split("\\.")[1].split("\\(")[0];
				     	//System.out.println( tmp );
						for (Map.Entry<String, Boolean> mList : methodsList.entrySet()) {
							 System.out.println("ListaMethods: "+ mList.getKey() +"\n");
							 System.out.println( "varsDeclarations: "+tmp+"\n" );
							//if( mList.getKey().matches( tmp ) )
								//System.out.println( varsDec.getKey() );
						}			     	
				}*/
		//}*/
		     	 
		     	for (Map.Entry<String, Boolean> mList : methodsList.entrySet()) {
			     	 	String ms = mList.getKey();//method source
			     	 	for (Map.Entry<String, String> varsDec : varsDeclaration.entrySet()) {
			     		 	 if(  varsDec.getValue().contains( ms ) ){
			     		 		System.out.println( " varsDec.getKey(): "+varsDec.getKey()+ " varsDec.getValue(): " +varsDec.getValue() +" // " + " ms: " + ms);
			     		 		varsSources.add( varsDec.getKey() );
			     		 		//System.out.println( "VariableSource:" +varsDec.getKey() + varsDec.getValue() );
			     		 	 }
			     		 	 //else
			     		 		 
					}
		     	}
		     	
		     	/*for( int i=0; i<varsNotDefinition.size(); i++ ){
		     		System.out.println("NS-DV:"+varsNotDefinition.get(i));
		     		for ( int j=0; j<methodsCalls.size(); j++ ) {
		     			if( methodsCalls.get(j).contains( varsNotDefinition.get(i) ))
		     				System.out.println( "varName:"+varsNotDefinition.get(i)+ "contenidaen: "+ varsNotDefinition.get(i)  );
		     		}
		     	}*/
		}
		
		public static void foundSourcesInMethodsCall(){
			for (Map.Entry<String, Boolean> mList : methodsList.entrySet()) {
	     	 	String ms = mList.getKey();//method source
	     	 	System.out.println("PimeraPasada: "+mList.getKey() );
	     	 	for ( int i=0; i<methodsCalls.size(); i++) {
	     		 	 if(  methodsCalls.get(i).contains( ms) ){
	     		 		System.out.println( " CallsMethodsMatchets: "+ methodsCalls.get(i));
	     		 	 	for (Map.Entry<String, String> varsDec : varsDeclaration.entrySet()) {
			     		 	 if( methodsCalls.get(i).contains(  varsDec.getKey() ) ){
			     		 		for (Map.Entry<String, Boolean> ml : methodsList.entrySet()) {
			     		 			System.out.println( "SegundaPasada: "+ml.getKey());
			     		 			String methodV = ml.getKey();
			     		 			if( methodsCalls.get(i).contains(methodV))
			     		 				System.out.println( "Encontro: "+methodsCalls.get(i) );
			     		 		}
			     		 		//System.out.println( "VarName: " +varsDec.getKey() + "VarContetn: "+varsDec.getValue()); 
			     		 		 //System.out.println( methodsCalls.get(i) );
			     		 	 }
			     		 	 //else
			     		 		 
					}
	     		 		//varsSources.add( varsDec.getKey() );
	     		 		//System.out.println( "VariableSource:" +varsDec.getKey() + varsDec.getValue() );
	     		 	 }
	     		 	 //else
	     		 		 
			}
			}
		}
		
		public static void foundSourcesinM(){
			for ( int i=0; i<methodsCalls.size(); i++) {
				for (Map.Entry<String, String> varsDec : varsDeclaration.entrySet()) {
					int pos = methodsCalls.get(i).indexOf( varsDec.getKey() ), f=0;
					System.out.println(pos);
					if(pos > -1 ){
						//end = methodsCalls.get(i).indexOf( '(' );
						//System.out.println("end: "+end);
						System.out.println( methodsCalls.get(i).substring( pos, methodsCalls.get(i).length()   ) );
						String tmp = methodsCalls.get(i).substring( pos, pos+varsDec.getKey().length() );
						System.out.println( methodsCalls.get(i).substring( pos, pos+varsDec.getKey().length()   ) );
						System.out.println( methodsCalls.get(i).substring( pos, end   ) );
						for (Map.Entry<String, Boolean> ml : methodsList.entrySet()) {
							f = tmp.indexOf( ml.getKey());
							if( f   > -1) 
								System.out.println( tmp );;
								
						}
						
					}
					
				}
			}
		}
		
		
}

