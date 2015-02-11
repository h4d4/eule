package cu;

	
		import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

		import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Parameter;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
	
		//import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
	
		public class Test {
		  static String file;
		  static CompilationUnit cu;
		  public static void generateCu( ) throws japa.parser.ParseException, IOException{
		    FileInputStream in = new FileInputStream( file );
		    try {
		      // parse the file
		      cu = JavaParser.parse(in);
		    } finally {
		      in.close();
		    }
		  }
	
		  public static void numberLineMethodCalls(){
		    cu.accept(new VoidVisitorAdapter<Void>(){
		      @Override
		      public void visit(final MethodCallExpr n, final Void arg){
		        System.out.println(n);
		/*        if(n.getName().equals( "getDeviceId" )){
		          deviceID_nl.add( n.getBeginLine() );
		        }else if(n.getName().equals( "getSimSerialNumber" )){
	          serialNumber_nl.add( n.getBeginLine() );
	        }else if(n.getName().equals( "findViewById" )){
	          findViewById_nl.add( n.getBeginLine() );
	        }else if(n.getName().equals( "getLatitude" )){
	          latitude_nl.add( n.getBeginLine() );
	        }else if(n.getName().equals( "getLongitude" )){
	          longitude_nl.add( n.getBeginLine() );
	        }*/
	        super.visit(n, arg);
	      }
	    }, null);
	  }
		  
		  
		  private static class MethodVisitor extends VoidVisitorAdapter {
			  @Override
			  public void visit(VariableDeclarationExpr n, Object arg)
			  {  
				  
			      java.util.List<VariableDeclarator> myVars = n.getVars();
			      	
			          for (VariableDeclarator vars: myVars){
			              System.out.println(vars.getId().getName() + " - "+ vars.getInit());
			              }
			  }
		  }
		  
		  /**
		     * Simple visitor implementation for visiting MethodDeclaration nodes.
		     */
		    private static class MethodChangerVisitor extends VoidVisitorAdapter {

		        @Override
		        public void visit(MethodDeclaration n, Object arg) {
		            // change the name of the method to upper case
		            n.setName(n.getName()+"{Alice:}");

		            // create the new parameter
		            japa.parser.ast.body.Parameter newArg = ASTHelper.createParameter(ASTHelper.INT_TYPE, "value");

		            // add the parameter to the method
		            ASTHelper.addParameter(n, newArg);
		        }

		    }
		  
		 /* private static class MethodVisitor2 extends VoidVisitorAdapter {
			  @Override
			  public void visit(InitializerDeclaration n, Object arg)
			  {  
				  
			      Class<? extends InitializerDeclaration> myVars = n.getClass();
			      	
			          for (Class<? extends InitializerDeclaration> vars: myVars){
			              System.out.println(vars.getId().getName() + " - "+ vars.getInit());
			              }
			  }
		  }*/
		  
		  

	  public static void main(final String[] args) throws Exception{
	    	file = "/home/h4d4/Escritorio/inLabelGenerator/MainActivity.java";
	    			//"/home/h4d4/Escritorio/inLabelGenerator/Exceptions1.java";
	    			//"/home/h4d4/Escritorio/inLabelGenerator/test-sources.java";
	    	generateCu(  );
	    	numberLineMethodCalls();
	    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	    	new MethodVisitor().visit(cu, null);
	    	//System.out.println(cu.toString());
	    	System.out.println("Variable nombre - valor");
	    	System.out.println("MAin");
	    	new MethodChangerVisitor().visit(cu, null);
	    	try {
	    		 
				String content = "This is the content to write into file";
	 
				File file = new File("/home/h4d4/Escritorio/outLabelGenerator/parser.java");
	 
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
	 
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(cu.toString());
				bw.close();
	 
				System.out.println("Done");
	 
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	  
	  

	}


