package cu;

import japa.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class Main {
	
	public static void main(String[] args) throws ParseException, IOException {
		String fileIn = "/home/h4d4/Escritorio/eule/InputLabelGenerator/ImplicitFlow4.java";
		String fileOut = "/home/h4d4/Escritorio/outLabelGenerator/ImplicitFlow4.java";
		Annotation.setFiles(fileIn, fileOut);
		Source.getSources(fileIn);
		
		if( Source.varSources.isEmpty() ){
			//Annotation AA-c
			
		}else{	//Annotation AA
			//ANOTAR VARIABLES SOURCES
			
			
			Annotation.generateCu();
			//P1: identificar el total de metodos de la clase
			new Annotation.MethodVisitor().visit(Annotation.cu, null);
			//Annotation.checkArrayList(Annotation.declaredMethods);
			//P2: del total de metodos identificar los que son llamados con la clase
			new Annotation.MethodCallsVisitor().visit(Annotation.cu, null);
			//Annotation.checkStringMaps(Annotation.methodsCalls);
			Annotation.filterMethodsCalls();
			System.out.println("mcc");
			Annotation.checkStringMaps(Annotation.methodClassCall);
			System.out.println("mcc");
			Annotation.methodsCallsSources();
		/*	for(int i=0; i<Source.varSources.size(); i++){
				System.out.println("Source: "+Source.varSources.get(i));
			}*/
			Annotation.filterMethodsNoSources();
			System.out.println("MNS");
			Annotation.checkArrayList(Annotation.methodsNoSources);
			System.out.println("MNS");
			//Annotation.checkArrayList(Annotation.methodsSources);
			System.out.println("Methods sources: "+Annotation.methodsSources.size());
			new Annotation.MethodChangerVisitorSources().visit(Annotation.cu, null);
			new Annotation.MethodChangerVisitorNS().visit(Annotation.cu, null);
			Annotation.printFile();

		}	
	
	}
}
