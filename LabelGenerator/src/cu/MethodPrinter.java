package cu;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;


/**
 * @author h4d4
 *
 */
/**
 * @author h4d4
 *
 */
public class MethodPrinter {
	static CompilationUnit cu;
	static String fileIn;
	static ArrayList<Integer> deviceID_nl = new ArrayList<Integer>(), //line number by matched methods
			serialNumber_nl = new ArrayList<Integer>(),
			findViewById_nl = new ArrayList<Integer>(),
			latitude_nl = new ArrayList<Integer>(),
			longitude_nl = new ArrayList<Integer>();
	static ArrayList<String> ct_deviceID = new ArrayList<String>(), //content lines 
			ct_serialNumber = new ArrayList<String>(),
			ct_findViewById = new ArrayList<String>(),
			ct_latitude = new ArrayList<String>(),
			ct_longitude = new ArrayList<String>();
	
		public static void setFileIn( String inFile ){
			fileIn = inFile;
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
  
    /**
     * Get line number when specific call method appears
     */
    public static void numberLineMethodCalls(){
	    		cu.accept(new VoidVisitorAdapter<Void>(){
	            	@Override
	            	public void visit(final MethodCallExpr n, final Void arg){
	            		if(n.getName().equals( "getDeviceId" )){
	            			deviceID_nl.add( n.getBeginLine() ); 
	            		}else if(n.getName().equals( "getSimSerialNumber" )){
	            			serialNumber_nl.add( n.getBeginLine() );
	            		}else if(n.getName().equals( "findViewById" )){
	            			findViewById_nl.add( n.getBeginLine() );
	            		}else if(n.getName().equals( "getLatitude" )){
	            			latitude_nl.add( n.getBeginLine() );
	            		}else if(n.getName().equals( "getLongitude" )){
	            			longitude_nl.add( n.getBeginLine() );
	            		}
	            		super.visit(n, arg);
	            	}
	            }, null);
    }
    public static int matchestChar( String s, char c ){
    	char array[] = s.toCharArray();
    	int count = 0;
    	for( int i=0; i<array.length; i++ ){
    		if( array[i] == c )
    			count+=1;
    	}
    	return count;
    }
    
	/**
	 * @param fileIn
	 * @param ln
	 * @return
	 * @throws IOException
	 * return line from specific line number
	 */
	@SuppressWarnings("resource")
	public static String contentLineMethodCalls( int ln, String method ) throws IOException{
		String lineC = null, s ,tokens[], nameVar, tmp;
		int numP = 0;//# de puntos
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));			
		} catch (FileNotFoundException e) { e.printStackTrace(); System.exit(-1); }
		
		for( int i=1; i<ln+1; i++ ){
			s = in.readLine();
			if( i == ln )
				lineC = s;
		}
		numP = matchestChar( lineC, '.' );
		if( numP  > 1 ){
			tokens =  lineC.split("\\s+");
			for( int i=0; i<tokens.length; i++ ){
				
				System.out.println( "Splittt: " + tokens[i]);
			}
			
		}
		//System.out.println( "El punto apararece: " + matchestChar( lineC, '.' ) );
		//tokens = lineC.split("\\.")[0].split("=");
		
		/*tokens = lineC.split("\\.")[0].split("=");
		if( tokens.length > 1 ){
			for( int k=0; k<tokens.length; k++ )
				System.out.println( tokens[k] );
		}else{
			System.out.println( tokens[0] + "No =" );
		}*/
		
		/*if( lineC.split("\\.")[0].con("=") ){
			nameVar = lineC.split(".")[0].split("=")[0];
		}else{
			nameVar = lineC.split(".")[0];
		}*/
		
		return lineC;
	}
	

	//get line nunber to methods found
	public static void setLineMethods() throws IOException{
		if( deviceID_nl.size() > 0 ){
			for( int i=0; i< deviceID_nl.size(); i++ )
				ct_deviceID.add( contentLineMethodCalls(deviceID_nl.get(i), "getDeviceId" ) );
		}
		if( serialNumber_nl.size() > 0 ){
			for( int i=0; i< serialNumber_nl.size(); i++ ){
				ct_serialNumber.add( contentLineMethodCalls(serialNumber_nl.get(i), "getSimSerialNumber" ) );
			}
		}
		if( findViewById_nl.size() > 0 ){
			for( int i=0; i< findViewById_nl.size(); i++ ){
				ct_findViewById.add( contentLineMethodCalls( findViewById_nl.get(i), "findViewById" ) );
			}
		}
		if( latitude_nl.size() > 0 ){
			for( int i=0; i< latitude_nl.size(); i++ ){
				ct_latitude.add( contentLineMethodCalls( latitude_nl.get(i), "getLatitude" ) );
			}
		}
		if( longitude_nl.size() > 0 ){
			for( int i=0; i< longitude_nl.size(); i++ ){
				ct_longitude.add( contentLineMethodCalls( longitude_nl.get(i), "getLongitude" ) );
			}
		}
	}
		

    public static void main(final String[] args) throws Exception{
    	String fileIn = "/home/h4d4/Escritorio/inLabelGenerator/test-sources.java";
    	setFileIn( fileIn );
    	generateCu(  );
    	numberLineMethodCalls();
    	setLineMethods();
    	if( ct_deviceID.size() > 0 ){
    		System.out.println( ct_deviceID.size() );
    		for( int i=0; i<ct_deviceID.size(); i++)
    			System.out.println(ct_deviceID.get(i));
    	}
    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    	if( ct_serialNumber.size() > 0 ){
    		System.out.println( ct_serialNumber.size() );
    		for( int i=0; i<ct_serialNumber.size(); i++)
    			System.out.println(ct_serialNumber.get(i));
    	}
    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    	if( ct_findViewById.size() > 0 ){
    		System.out.println( ct_findViewById.size() );
    		for( int i=0; i<ct_findViewById.size(); i++)
    			System.out.println(ct_findViewById.get(i));
    	}
    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    	if( ct_latitude.size() > 0 ){
    		System.out.println( ct_latitude.size() );
    		for( int i=0; i<ct_latitude.size(); i++)
    			System.out.println(ct_latitude.get(i));
    	}
    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    	if( ct_longitude.size() > 0 ){
    		System.out.println( ct_longitude.size()  );
    		for( int i=0; i<ct_longitude.size(); i++)
    			System.out.println(ct_longitude.get(i));
    	}
    	
    	//setLineMethods();
    }

}
