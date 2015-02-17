package cu;

import japa.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;


public class Main {
	
	static File dirIn = null, dirOut, dirsIn[];
	static ArrayList<String> filesIn = new ArrayList<String>(),
			filesOut = new ArrayList<String>();
	
	private static void ReadPath() throws IOException{
		String inputs;
		InputStreamReader lector = new InputStreamReader(System.in); 
		BufferedReader buffer = new BufferedReader( lector );
		System.out.println( "Ingrese la ruta completa para el directorio de entrada, y para el directorio de salida:\n" );
		System.out.println( "Ejemplo: dir-entrada@dir-salida \n" );
		
		try{
			inputs = buffer.readLine();
			if( inputs != null ){
				dirIn = new File( inputs.split("@")[0] );  
				dirOut = new File( inputs.split("@")[1] );
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		buffer.close();
		
		dirsIn = dirIn.listFiles();
		FileFilter fileFilter = new FileFilter() {
		    public boolean accept(File file) {
		            return file.isDirectory();
		    }
		};
		dirsIn = dirIn.listFiles(fileFilter);
		
		if( dirsIn.length >= 1 ){
			for( int i =0; i<dirsIn.length; i++ ){
				File[] fi = dirsIn[i].listFiles();
				filesIn.add( fi[0].toString() );
				String tmp = fi[0].toString().substring( fi[0].toString().lastIndexOf('/')+1 );
				filesOut.add( dirOut+"/"+tmp );
			}
		}
	}
	
	
	public static void main(String[] args) throws ParseException, IOException {
		// /home/h4d4/Escritorio/inLabelGenerator/@/home/h4d4/Escritorio/outLabelGenerator/
		String fileIn, fileOut;
		ReadPath();
		for( int f =0; f<filesIn.size(); f++ ){
			fileIn = filesIn.get(f);
			fileOut = filesOut.get(f);
			Source.varSources.clear();//P1-A: Identificar variables sources
			Source.getSources(fileIn);
			Annotation.setFiles(fileIn, fileOut);//inicializar archivos clase Annotation
			Annotation.generateCu();
			new Annotation.MethodVisitor().visit(Annotation.cu, null);//P1: identificar el total de metodos de la clase
			Annotation.IsClass();
			
			if( Source.varSources.isEmpty() ){//Annotation AA-c
				new Annotation.MethodChangerVisitorZeroSources().visit(Annotation.cu, null);
				new Annotation.ChangerSetContentView().visit(Annotation.cu, null);
				new Annotation.ChangeImports().visit(Annotation.cu, null);
				Annotation.changePackage();
				Annotation.addImports();
				Annotation.printFile();
				BufferWriter.init(fileOut.toString(), fileOut.toString());
			}else{	//Annotation AA
				new Annotation.MethodCallsVisitor().visit(Annotation.cu, null);
				Annotation.filterMethodsCalls();
				Annotation.methodsCallsSources();
				Annotation.filterMethodsNoSources();
				new Annotation.MethodChangerVisitorSources().visit(Annotation.cu, null);
				new Annotation.MethodChangerVisitorNS2().visit(Annotation.cu, null);
				new Annotation.ChangerSetContentView().visit(Annotation.cu, null);
				new Annotation.ChangeImports().visit(Annotation.cu, null);
				Annotation.changePackage();
				Annotation.addImports();
				Annotation.printFile();
				BufferWriter.init(fileOut.toString(), fileOut.toString());
			}

			Annotation.declaredMethods.clear();
			Annotation.methodsCalls.clear();
			Annotation.methodClassCall.clear();
			Annotation.methodsNoSources.clear();
		}
	}
}
