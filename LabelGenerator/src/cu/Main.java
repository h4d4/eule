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
import java.util.List;
import java.util.Map;


public class Main {
	
	static File dirIn = null, dirOut, dirsIn[];
	static ArrayList<String> filesIn = new ArrayList<String>(),
			test = new ArrayList<String>(),
			filesOut = new ArrayList<String>();
	static ArrayList<ArrayList<String>> allFilesIn = new ArrayList<ArrayList<String>>(); 
	
	static String xmlFileIn = null;
	
	private static void ReadPath() throws IOException{
		String inputs;
		InputStreamReader lector = new InputStreamReader(System.in); 
		BufferedReader buffer = new BufferedReader( lector );
		System.out.println( "Ingrese la ruta completa para el directorio de entrada, y para el directorio de salida:\n" );
		System.out.println( "Ejemplo: dir-entrada@dir-salida \n" );
		//SANITIZAR ENTRADA DE DIRECTORIOS: ASEGURAR / AL FINAL DE CADA DIR 
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
				ArrayList<File> fi = new ArrayList<File>();
				File[] fit = dirsIn[i].listFiles(); 
				for( int fs=0; fs<fit.length; fs++ ){ //clean shadow files
					if( ! fit[fs].toString().endsWith("~") && !fit[fs].isHidden() )//clean temporary and shadow files
						fi.add( fit[fs] );
				}
				//File[] fi = dirsIn[i].listFiles(); 
				//for( int f=0; f<fi.length; f++ ){System.out.println( "##############################  fi["+f+"]  "+ fi[f] );}
				//System.out.println( "fi.size ="+ fi.length);
				ArrayList<String> tmp = new ArrayList<String>();
				for( int k=0; k<fi.size(); k++ ){
					//System.out.println(fi[k].toString());
					tmp.add(fi.get(k).toString());
					/*test.add(fi[k].toString());
					allFilesIn.add(test);*/
					if( fi.get(k).toString().indexOf(".java") > -1 ){
						String dout1 = fi.get(k).toString().substring(fi.get(0).toString().lastIndexOf('/'));
						String doutmp = fi.get(k).toString().substring( 0, fi.get(0).toString().lastIndexOf('/'));
						String dout2 = doutmp.substring(doutmp.lastIndexOf('/') );
						String f = dout2+dout1 ;
						new File(dirOut+dout2).mkdir(); 
						filesOut.add( dirOut+f );
					}
				}
				allFilesIn.add(tmp);
				/*filesIn.add( fi[0].toString() );
				String dout1 = fi[0].toString().substring(fi[0].toString().lastIndexOf('/'));
				String doutmp = fi[0].toString().substring( 0, fi[0].toString().lastIndexOf('/'));
				String dout2 = doutmp.substring(doutmp.lastIndexOf('/') );
				String f = dout2+dout1 ;
				new File(dirOut+dout2).mkdir(); 
				filesOut.add( dirOut+f );*/
			}
		}
		/*for(int j=0;j<allFilesIn.size(); j++){
			System.out.println("allFilesIn[ "+j+"]");
			ArrayList<String> tmp = allFilesIn.get(j);
			for(int m=0; m<tmp.size(); m++){
				System.out.println("tmp["+m+"] = " +tmp.get(m));
				
			}
		}
		*/
	}
	
	
	public static void main(String[] args) throws ParseException, IOException {
		
		// /home/h4d4/Escritorio/Debug/inLabelDebug/@/home/h4d4/Escritorio/Debug/outLabelDebug/  // /home/h4d4/Escritorio/inLabelGenerator/@/home/h4d4/Escritorio/outLabelGenerator/
		
		// /home/h4d4/Escritorio/eule/InputLabelGenerator/@/home/h4d4/Escritorio/outLabelGenerator/
		String javaFileIn = null, fileOut;
		ReadPath();
		for( int f =0; f<allFilesIn.size(); f++ ){
			ArrayList<String> tmp = allFilesIn.get(f);
			for( int k=0;k<tmp.size(); k++ ){
				if( tmp.get(k).indexOf(".java") > -1 )
					javaFileIn = tmp.get(k);
				else if( tmp.get(k).indexOf(".xml") > -1  )
					xmlFileIn = tmp.get(k);
			}
			//javaFileIn = filesIn.get(f);
			System.out.println("FileIN: "+ javaFileIn);
			fileOut = filesOut.get(f);
			Source.varSources.clear();//P1-A: Identificar variables sources
			//Source.getSources(javaFileIn);
			Source.init(javaFileIn);
			
			Annotation.setFiles(javaFileIn, fileOut);//inicializar archivos clase Annotation
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
				//new Annotation.MethodCallsVisitor().visit(Annotation.cu, null);
				new Annotation.MethodCallsVisitor2().visit(Annotation.cu, null);
				Annotation.filterMethodsCalls2();
				Annotation.methodsCallsSources2();
				Annotation.filterMethodsNoSources();
				new Annotation.ArraysMethosSources().visit(Annotation.cu, null);
				//new Annotation.VisitorArrayDeclaration().visit(Annotation.cu, null);
				//new Annotation.VariableDeclarationVisitor().visit(Annotation.cu, null);
				//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>><"+fileIn);
				//Annotation.checkSet(Annotation.arraysSources);
				//System.out.println("****************************************");
				new Annotation.arraysVariables().visit(Annotation.cu, null);
				//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>><"+fileIn);
				//Annotation.checkSet(Annotation.arraysSources);;
				//new Annotation.VisitorFieldDeclaration().visit(Annotation.cu, null);
				new Annotation.MethodChangerVisitorSources().visit(Annotation.cu, null);
				new Annotation.MethodChangerVisitorNS2().visit(Annotation.cu, null);
				//
				
				//
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
			Annotation.methodsSources.clear();
			Annotation.arraysSources.clear();
			xmlFileIn = null;
		}
		//clean .java files in out dirs
		
		System.out.println("FINISH...........");
	}
	
}
