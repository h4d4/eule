package cu;

import japa.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class Main {
	
	public static void main(final String[] args) throws ParseException, IOException {
		String fileIn = "/home/h4d4/Escritorio/inLabelGenerator/Exceptions1.java"; //MainActivity.java  Exceptions1.java Exceptions3.java test-sources.java ImplicitFlow1.java
		//Scanner scanner = new Scanner();
		Scanner.setFileIn(fileIn);
		Scanner.fullingMethodsList();
		Scanner.generateCu();
		new Scanner.VariableDeclarationVisitor().visit(Scanner.cu, null);
		System.out.println(">>>>>>>>>>>>>>>  varsDeclaration  >>>>>>>>>>>>>>>>>>>");
		Scanner.checkStringMaps(Scanner.varsDeclaration);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		new Scanner.MethodCallsVisitor().visit(Scanner.cu, null);
		System.out.println(">>>>>>>>>>>>  methodsCalls >>>>>>>>>>>>>>>>>>>>>>");
		Scanner.checkArrayList(Scanner.methodsCalls);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(">>>>>>>>>>>>  varsSources >>>>>>>>>>>>>>>>>>>>>>");
		Scanner.varSourcesFound();
		System.out.println(">>>>>>>>>>>>>>>>MethodList>>>>>>>>>>>>>>>>>>");
		//Scanner.foundSourcesInMethodsCall();
		//Scanner.checkBooleanMaps(Scanner.methodsList);
		System.out.println(">>>>>>>>>>>>  test >>>>>>>>>>>>>>>>>>>>>>");
		//Scanner.checkArrayList( Scanner.varsNotDefinition);
		//Scanner.foundSourcesInMethodsCall();
		Scanner.foundSourcesinM();
		System.out.println(">>>>>>>>>>>>  VARS_SOURCES >>>>>>>>>>>>>>>>>>>>>>");
		Scanner.checkArrayList(Scanner.varsSources);
		System.out.println(Scanner.varsSources.size());
		
		
		
	}
}
