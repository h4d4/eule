package jifpolicytool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

public class Parser {
	static boolean debug = true; 
	
	public Parser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File filename = null;
		File outputdir = null;
		if (args.length < 2) { 
			System.err.println("usage: <parser> filename output-directory"); System.exit(-1);
		}
		else {
			filename = new File(args[0]);
			outputdir = new File(args[1]);
		}

		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));			
		} catch (FileNotFoundException e) { e.printStackTrace(); System.exit(-1); }
		
		List principals = new ArrayList();
		List declassifiers = new ArrayList();
		
		String line = null;
		try {
			do {
				line = in.readLine();
				if (line != null) {
					String[] tokens = line.split("//")[0].split("\\s+");
					if (tokens.length > 1) {  // we have a real policy line, not a comment or blank line
						if (debug) System.out.print("parsing separator: " + tokens[1]);
						
						// only allow three connectors for now: '->' , 'trusts', ':' 
						if (0 == tokens[1].compareTo("->")) { // a delegation
							Principal p = new Principal(tokens[0]);
							int i;
							
							if ((i = principals.indexOf(p)) != -1) p = (Principal)principals.get(i);
							else principals.add(p);
							
							if (tokens.length < 3) System.err.println("ERROR: ill-formed delegation--no delegation principal");
							else {
								Principal p2 = new Principal(tokens[2]);
								
								// exclude two special names
								if (!tokens[2].startsWith("OS.")) {
									int i2;
									if ((i2 = principals.indexOf(p2)) != -1) p2 = (Principal)principals.get(i2);
									else principals.add(p2);
								}
								
								p.addDelegation(p2);
							}
							
							if (tokens.length > 3) System.err.println("ERROR: too many tokens on delegation line");
						} else if (0 == tokens[1].compareTo("allows")) { // trusting a declassifier
							Principal p = new Principal(tokens[0]);
							int i;
							
							if ((i = principals.indexOf(p)) != -1) p = (Principal)principals.get(i);
							else principals.add(p);
							
							if (tokens.length < 3) System.err.println("ERROR: ill-formed allows--no delegation principal");
							else {
								Declassifier d = new Declassifier(tokens[2]);
								int i2;
								if ((i2 = declassifiers.indexOf(d)) != -1) d = (Declassifier)declassifiers.get(i2);
								else declassifiers.add(d);
								
								p.addDeclassifier(d);
							}
							
							if (tokens.length > 3) System.err.println("ERROR: too many tokens on allows line");
							
						} else { // presume a declassifier decl
							System.out.println(tokens[0] + " === declassifier");
						}
					}

					if (debug) System.out.print("\n");
				}
			} while (line != null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Object[] allprincipals = principals.toArray();
		Object[] alldeclassifiers = declassifiers.toArray();
		
		for (int i = 0; i < allprincipals.length; i++) {
			// These should each go into their own individual files
			Principal p = (Principal)allprincipals[i];
			PrintStream out = null;
			try {
				out = new PrintStream(new FileOutputStream(outputdir + "/" + Principal.packageName + "/" + p.toClassName() + ".jif"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			System.out.println("<begin PRINCIPAL " + p.getName() + ">\n");
			out.println(p.toJifCode());
			out.close();
			System.out.println("<end PRINCIPAL " + p.getName() + ">\n");
		}
		
		// write out PrincipalStoreUtil.setupPolicy(...)
		// begin method
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream(outputdir + "/" + Principal.packageName + "/JifPolicy.jif"));
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		
		out.println("package " + Principal.packageName + ";");
		out.println("");
		out.println("public class JifPolicy {");
		out.println("");
		out.println("public static PolicyStore setupPolicy{}(Principal{} userP, String{} mainPname) throws (SecurityException) where caller(userP)");
		out.println("{");
		out.println("    if (userP == null || mainPname == null) return null;");
		out.println("    String keystoreFilename = \"certs/.keystore\";");
		out.println("    String trustedCAfilename = \"certs/cacert.pem\";");
		out.println("    String pwdFile = \"password-keystore\";");
		out.println("");
		out.println("    final principal{} user = userP;");
		out.println("    final PolicyStore policy = new PolicyStore();");
		out.println("");
		out.println("	// need to bootstrap runtime by getting main principal access to all keys");
		out.println("	jif.runtime.Runtime[user] runtime = null;");
		out.println("	try {");
		out.println("		runtime = jif.runtime.Runtime[user].getRuntime();");
		out.println("	} catch (SecurityException e) {}");
		out.println("");
		out.println("	// set up the main principal for the sake of establishing the KeyStore");
		out.println("	Principal mainP = new MainPrincipal(mainPname); // MainPrincipal only allows delegation closures");
		out.println("");		
		out.println("	final principal main = mainP;");
		out.println("	jif.runtime.Runtime[main]{main:} _mainRT = null;");
		out.println("");
		out.println("	char{user:}[]{main:} pwd = declassify(util.Password.getPassword(pwdFile,new label{user:},user),{main:});");
		out.println("");
		out.println("	PolicyUtil.delegate(main,user,new label{main:}); // main trusts the user");
		out.println("	// declassify pwd from user -> main");
		out.println("	int{main:} len = pwd != null ? declassify(pwd.length,{main:}) : 0;");
		out.println("	char{main:}[]{main:} mainPwd = new char[len];");
		out.println("	try {");
		out.println("		for (int i = 0; i < len; i++) mainPwd[i] = declassify(pwd[i],{main:});");
		out.println("	} catch (ArrayIndexOutOfBoundsException e) {");
		out.println("	} catch (NullPointerException e) {} // due to array accesses");
		out.println("");
		out.println("	try {");
		out.println("		// will work if user trusts main, i.e. main's privateKey is in keystore");
		out.println("		_mainRT = jif.runtime.Runtime[main].getRuntime(keystoreFilename,mainPwd,trustedCAfilename);");
		out.println("	} catch (SecurityException se) {} // we don't have a valid main principal");
		out.println("");
		out.println("	//	 declare principals");
		out.println("	if (user actsfor main) if (main actsfor user) {");
		out.println("		final jif.runtime.Runtime[main]{} mainRT = declassify(_mainRT,{}); // we do this for the sake of NPE analysis");			
		out.println("");
		out.println("	// use mainRT (which now has easy access to the local keystore) for the rest of the policy setup");
		out.println("		if (mainRT == null) throw new SecurityException(\"can't initialize runtime system\");");
		for (int i = 0; i < allprincipals.length; i++)
		{
			Principal p = (Principal)allprincipals[i];
			out.println("	// creating new " + p.toPrincipalName() + " principal");
			out.println("		Principal " + p.toPrincipalVarName() + " = null;");
			out.println("		if (mainPname.equals(\"" + p.toPrincipalName() + "\")) {");
			out.println("			" + p.toPrincipalVarName() + " = new " + p.toClassName() + "(\"" + p.toPrincipalName() + "\",mainRT.getPublicKey(\"" + p.toPrincipalName() + "\"),pwd,main);  // has accessor for private key");
			out.println("			PolicyUtil.delegate(main," + p.toPrincipalVarName() +",new label{main:}); // main trusts the user");
			out.println("		}");			
			out.println("		else"); 
			out.println("			" + p.toPrincipalVarName() + " = new " + p.toClassName() + "(\"" + p.toPrincipalName() + "\",mainRT.getPublicKey(\"" + p.toPrincipalName() + "\"));   // public key only");
//			out.println("    Principal{} " + p.toPrincipalVarName() + " = new " + p.toClassName() + "(\"" + p.toPrincipalName() + "\");");
		}

		out.println("");
		for (int i = 0; i < allprincipals.length; i++) {
			Principal p = (Principal)allprincipals[i];
			out.println("    policy.addPrincipal(" + p.toPrincipalVarName() + ",\"" + p.getName() + "\", new label{});");
		}
		out.println("");
		
		for (int i = 0; i < allprincipals.length; i++)
		{
			Principal p = (Principal)allprincipals[i];
			out.println("    final principal{} " +  p.toPrincipalName() + " = policy.getPrincipal(\"" + p.toPrincipalName() + "\",new label{});");
		}
		out.println("");
		for (int i = 0; i < allprincipals.length; i++) {
			Principal p = (Principal)allprincipals[i];
			Object[] del = p.getDelegations();
			if (del.length > 0) out.println("\n    // delegations for " + p.toPrincipalName());
			for (int j = 0; j < del.length; j++) {
				Principal delP = (Principal)del[j];
				if (delP.getName().startsWith("OS.")) {
//					String delPname = delP.getName().split("\\.")[1];
					out.println("		if (mainPname.equals(\"" + p.toPrincipalName() + "\"))");
					out.println("			PolicyUtil.delegate(" + p.toPrincipalName() + ",user,new label{" + p.toPrincipalName() + ":});");
				}
				else 
					out.println("    	PolicyUtil.delegate(" + p.toPrincipalName() + "," + delP.toPrincipalName() + ",new label{"+ p.toPrincipalName() +":});");
			}
		}
		out.println("    } // end (main == user)");  
		out.println("    return policy;");
		out.println("    } // end setupPolicy method"); 
		out.println("} // end class definition"); 
		out.close();
		try {
			in.close();
		} catch (IOException ignore) {}
	}
}
