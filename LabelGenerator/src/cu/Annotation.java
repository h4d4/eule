package cu;

import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * @author h4d4
 *
 */
public class Annotation {
	
	static String sourcesVar[] = {"imei"};
	//static String sourcesVar[] = {};
	static CompilationUnit cu;
	static String fileIn, fileOut;
	static ArrayList<String> declaredMethods = new ArrayList<String>(),//metodos declarados en la clase
			methodsCallSource = new ArrayList<String>(), //metodos de la clase que son llamados con Sources
			//methodsSources = new ArrayList<String>(), //methodos a anotar con source
			methodsNoSources = new ArrayList<String>();	//metodos a anotar sin source
	static Map<String,String> methodsCalls = new HashMap<String,String>(), //todas las llamadas a metodos
			methodClassCall = new HashMap<String,String>();	//llamadas solo a metodos de la clase	
	static Set<String> methodsSources = new HashSet<String>(); //methodos a anotar con source
	
	
	public static void setFiles( String inFile, String outFile ){
		fileIn = inFile;
		fileOut = outFile;
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
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
	}
	public static boolean getNumberSources(){ //identificar si existen sources
		boolean existSources = false;
		if( sourcesVar != null ){
			if( sourcesVar.length >= 0 )
				existSources = true;
		}
		return existSources;
	}
	
	public static class MethodVisitor extends VoidVisitorAdapter<Object> {//obtener el total de metodos declarados dentro de la clase
		@Override
	    public void visit(MethodDeclaration n, Object arg) {
			declaredMethods.add( n.getName() ); 
	    }
	 }
	
	public static class MethodVisitorExceptions extends VoidVisitorAdapter<Object> {
		@Override
	    public void visit(MethodDeclaration n, Object arg) {
			System.out.println("Thows: "+n.getThrows()); 
	    }
	 }
	
	public static class MethodCallsVisitor extends VoidVisitorAdapter<Object> {//todas las llamadas a metodos
        @Override
        public void visit(MethodCallExpr n, Object arg) {
        	methodsCalls.put(n.getName(), n.toString());
        	//System.out.println( "7777777777777777777777777777777777777777777: "+n.getName()+" "+n.toString());
        } 
	}

	public static void filterMethodsNoSources(){
		int i;
		for( i=0; i<declaredMethods.size(); i++ ){
			boolean f = false;
			for( String s : methodsSources ){
				if ( declaredMethods.get(i).equals( s ) ){
					f = true;
					
					break;
				}
			}
			if (!f) 
				methodsNoSources.add( declaredMethods.get(i) );
		}
	}
	
	public static class MethodChangerVisitorSources extends VoidVisitorAdapter<Object> {//anotar methods sources
		@Override
        public void visit(MethodDeclaration n, Object arg) {
			for( String s : methodsSources ){
				if( s.equals(n.getName()) ){
					System.out.println( n.getName() );
					n.setName("{Alice:}"+n.getName()+"{Alice:}");
					java.util.List<japa.parser.ast.body.Parameter> parameters = n.getParameters(); 
		            for (japa.parser.ast.body.Parameter param : parameters){
			              param.getId().setName("{Alice:}"+param.getId().getName());
			        }
				}
			}
        }
    }
	
	public static class ChangerSetContentView extends VoidVisitorAdapter<Object> {
	        @Override
	        public void visit(MethodCallExpr n, Object arg) {
	        	if( n.getName().equals("setContentView") ){ 
	        		Expression newExp = ASTHelper.createNameExpr("R.layou");
	        		java.util.List<japa.parser.ast.expr.Expression> param = n.getArgs();
	        		param.clear();
	        		param.add(newExp);
	        	}
	        }
	}
	
	public static class MethodChangerVisitorNS extends VoidVisitorAdapter<Object> {//anotar methods NO sources
		@Override
        public void visit(MethodDeclaration n, Object arg) {
			
			for( int i=0; i<methodsNoSources.size(); i++ ){
				if( n.getName().equals(methodsNoSources.get(i)) ){
						System.out.println( "NS: "+methodsNoSources.get(i) );
						n.setName(n.getName()+"{}");
				}
			}
        }
    }
	
	public static class MethodChangerVisitorNS2 extends VoidVisitorAdapter<Object> {//anotar methods NO sources
		@Override
        public void visit(MethodDeclaration n, Object arg) {
			
			for( int i=0; i<methodsNoSources.size(); i++ ){
				if( n.getName().equals(methodsNoSources.get(i)) ){
						//System.out.println( "NS: "+methodsNoSources.get(i) );
						n.setName(n.getName()+"{}");
						String tmp = n.getName();
						//System.out.println( "name: "+n.getName() );
						//if( ! tmp.equals("onCreate{}") ){
							java.util.List<japa.parser.ast.body.Parameter> parameters = n.getParameters();
							if( parameters != null && !parameters.isEmpty() ){
								for (japa.parser.ast.body.Parameter param : parameters){
									if( ! tmp.equals("onCreate{}") ){
									System.out.println( "PNS<<<<<<<<<<<<<<<<<<<  "+tmp+"  <<<<<<<<<<<<<<<<<<:"+param.getId().getName() );
						              param.getId().setName("{}"+param.getId().getName()); 
									}
					            }
						}
				//}
			}
        }
    }
	}
	
	public static class MethodChangerVisitorZeroSources extends VoidVisitorAdapter<Object> {//anotar methods NO sources
		@Override
        public void visit(MethodDeclaration n, Object arg) {
			
			for( int i=0; i<declaredMethods.size(); i++ ){
				if( n.getName().equals(declaredMethods.get(i)) ){
						System.out.println( "NS: "+declaredMethods.get(i) );
						n.setName(n.getName()+"{}");
				}
			}
        }
    }
	
	public static void filterMethodsCalls(){		//detectar llamadas solo a metodos declarados en la clase
		for (Map.Entry<String, String> entry : methodsCalls.entrySet()) {
			for( int i=0; i<declaredMethods.size(); i++ ){
				if( entry.getKey().equals(declaredMethods.get(i)) ){
					methodClassCall.put( entry.getKey() ,  entry.getValue() );
				}
			}
		}
	}
	
	public static void methodsCallsSources(){ //guarda llamadas a metodos de la clase que tienen en sus argumentos un source
		for( int i=0; i<Source.varSources.size(); i++ ){
			for (Map.Entry<String, String> entry : methodClassCall.entrySet()) {
				if(  entry.getValue().indexOf( Source.varSources.get(i) )  > -1 ){
					methodsSources.add(entry.getKey());
				}
			}
			
		}
		for (String s : methodsSources) {
		    System.out.println("MS: "+s);
		}
	}

	
	public static void printFile(){
		try {
				File file = new File(fileOut);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(cu.toString());
				bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
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
	
	
/*	public static void main(String[] args) throws ParseException, IOException {
		String fileIn = "/home/h4d4/Escritorio/inLabelGenerator/ImplicitFlow1.java"; 
		String fileOut = "/home/h4d4/Escritorio/outLabelGenerator/parser.java";
		//MainActivity.java  Exceptions1.java Exceptions3.java test-sources.java ImplicitFlow1.java
		setFiles(fileIn, fileOut);
		generateCu( );
		new MethodVisitor().visit(cu,null);
		new MethodCallsVisitor().visit(cu, null);
		filterMethodsCalls(); //solo filtra la ultima llamada
		//checkStringMaps(methodClassCall);	
		methodsCallsSources();
		new MethodChangerVisitorSources().visit(cu,null);
		filterMethodsNoSources();
		new MethodChangerVisitor().visit(cu, null);
		printFile();
		new MethodVisitorExceptions().visit(cu, null);
		
	}*/
	
}
