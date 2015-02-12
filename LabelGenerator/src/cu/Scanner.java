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
		     	for (Map.Entry<String, Boolean> mList : methodsList.entrySet()) {
			     	 	String ms = mList.getKey();//method source
			     	 	for (Map.Entry<String, String> varsDec : varsDeclaration.entrySet()) {
			     		 	 if(  varsDec.getValue().contains( ms ) ){
			     		 		System.out.println( " varsDec.getKey(): "+varsDec.getKey()+ " varsDec.getValue(): " +varsDec.getValue() +" // " + " ms: " + ms);
			     		 		varsSources.add( varsDec.getKey() );
			     		 		//System.out.println( "VariableSource:" +varsDec.getKey() + varsDec.getValue() );
			     		 	 } 		 
					}
		     	}
		}
		
		public static void foundSourcesinM(){
			Map<String,String> matchest = new HashMap<String,String>();
			for ( int i=0; i<methodsCalls.size(); i++) {
				for (Map.Entry<String, String> varsDec : varsDeclaration.entrySet()) {
					int posVarInit = methodsCalls.get(i).indexOf( varsDec.getKey() );//position of var defined
					if( posVarInit > -1 ){
						int posVarEnd = posVarInit + varsDec.getKey().length();
						String var = methodsCalls.get(i).substring(posVarInit, posVarEnd );
						for (Map.Entry<String, Boolean> ml : methodsList.entrySet()) {
							int posImet = methodsCalls.get(i).indexOf( ml.getKey() );
							if( posImet > -1 ){
								int posEndM = posImet + ml.getKey().length();
								String met =  methodsCalls.get(i).substring( posImet, posEndM );
								System.out.println( "Method: " + met );
								//System.out.println(( var+"."+ met) );
								if( methodsCalls.get(i).indexOf( var+"."+ met) > -1){
									String match = var+"."+ met;
									System.out.println("Match: "+match);
									matchest.put(var, match);
									/*for (Map.Entry<String, String> vDecl : varsDeclaration.entrySet()) {
										System.out.println("Var : "+ var);
										if( vDecl.getValue().indexOf(var) == -1){
											varsSources.add( var );
											System.out.println("VarYaDefinida: "+var);
										}else if( vDecl.getValue().indexOf(var) == -1 ){
											System.out.println("no esta: "+var);
										}
									}*/
							   }
								
							}
						}
					}
				}
			}
			
			for (Map.Entry<String, String> mat : matchest.entrySet()) {
				System.out.println(mat.getKey()+" "+ mat.getValue());
				for( int j=0; j<varsSources.size(); j++ ){
					if( varsDeclaration.get( varsSources.get(j) ).indexOf( mat.getKey()) > -1 )
						System.out.println( " Ya estaba definida: "+ varsDeclaration.get( varsSources.get(j) ) );
					else
						System.out.println( " NoDefinida: "+ varsDeclaration.get( varsSources.get(j) ) );;
				}
			}
		}
		
		
}

