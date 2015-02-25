package cu;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Character;
import java.util.*;

public class Source {
	
	/**
	 * @param fileIn
	 * @return
	 */
	public static ArrayList<String> varSources = new ArrayList<String>();
	public static ArrayList<String> tmpSources = new ArrayList<String>();
	public static String nameEdit = null;
	
	
	public static void finEditTextSource(String inFile) throws IOException{
		
		BufferedReader in = null;
		File fileIn = new File( inFile );  
		ArrayList<String> fileCont = new ArrayList<String>();
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)));			
		} catch (FileNotFoundException e) { e.printStackTrace(); System.exit(-1); }
		
		String line = null; 
		do {
			 line = in.readLine(); 
			 
			 if( line != null) {
				 fileCont.add(line);
		     }
		} while (line != null);	
		
		ArrayList<String> idsEditPasswd = XmlExtract.getIdTextEdit( Main.xmlFileIn ),
		nameVars = new ArrayList<String>(); 
		
		
		for( int i=0; i<fileCont.size(); i++ ){
			String line1 = fileCont.get(i);
			if( line1.indexOf("findViewById") > -1 ){
				String tmp = line1.substring(line1.indexOf("findViewById"));
				boolean vnt =false;
				//System.out.println(tmp.split("\\(")[1].split("\\)")[0].split("\\.")[2]);
				String idR = tmp.split("\\(")[1].split("\\)")[0].split("\\.")[2];
				for( int k=0; k<idsEditPasswd.size(); k++ ){
					if( idsEditPasswd.get(k).equals(idR) ){
						System.out.println("Passwd Field: "+ line1);
						vnt = true;
						break;
					}
				}
				if(vnt){
					if( line1.indexOf("=") > -1 ){
						String[] t = line1.split("=")[0].split("\\s+");
						System.out.println("t.size "+t.length);
						for( int p=0; p<t.length; p++ ){
							System.out.println("t["+p+"] = "+ t[p]);
						}
						if( t.length >= 1 ){
							if( t[0].isEmpty() ){
								nameVars.add( t[t.length-1] );
							}else{
								nameVars.add(t[0]);
							}
							
						}
					}
				}
			}
		}
		//System.out.println("NameVar  "+ nameVars.get(0));
		nameEdit = nameVars.get(0);
	}
	
	public static void getSources( String fileIn ){

    File file = new File(fileIn);
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    DataInputStream dis = null;
    boolean flag = false;

    ArrayList<String> sources = new  ArrayList<String>();
    sources.add("getDeviceId");
    sources.add("getSimSerialNumber");
    sources.add("findViewById");
    sources.add("getLatitude");
    sources.add("getLongitude");
    sources.add("getSubscriberId()");
    //sources.add("getText().toString()");
    
    if( nameEdit != null && !nameEdit.isEmpty() ){
    	sources.add(nameEdit);
    	System.out.println("nameEdit  "+ nameEdit);
    	flag = true;
    }

    try {
      fis = new FileInputStream(file);
      bis = new BufferedInputStream(fis);
      dis = new DataInputStream(bis);

      while (dis.available() != 0) {
        @SuppressWarnings("deprecation")
		String line = dis.readLine();

        for (int i = 0; i < sources.size(); ++i) {
          int pos = 0;
          while (pos < line.length()) {
            String ssub = line.trim();
            ssub = ssub.substring(pos);
            
            char sub[] = ssub.toCharArray();
            pos = ssub.indexOf( sources.get(i) );

            if (pos == -1) break;
            
            if (sub[pos - 1] == '.') {
              int j = pos - 2;
              while ( j > 0 && Character.isLetter(sub[j])) j--;
              int a = j + 1;
              while ( j > 0 && sub[j] == ' ') j--;

              String name;
              if (sub[j] == '(' || sub[j] == ',') {
                name = ssub.substring(a, pos - 1);
                name = name.replaceAll("\\s+","");
                varSources.add(name);
              }
              else if (sub[j] == '=') {
                j--;
                while ( j > 0 && sub[j] == ' ') j--;
                int end = j;
                while ( j > 0 && Character.isLetter(sub[j]) ) j--;
                name = ssub.substring(j, end+1);
                name = name.replaceAll("\\s+","");
                varSources.add(name);
              }
            }else if(flag && (pos+sources.get(i).length() < line.length()) && sub[pos+sources.get(i).length()] == '.' ){
            	boolean f = false;
            	if((pos+sources.get(i).length()+1 < line.length()) && ssub.substring( pos+sources.get(i).length()+1 ).indexOf("getText()") > -1 )
            		f = true;
            	else if( (pos+sources.get(i).length()+1 < line.length()) && ssub.substring( pos+sources.get(i).length()+1 ).indexOf("toString()") > -1 )
            		f = true;
            		//System.out.println("ssub: "+ssub);
            	if (f){
            		int j = pos;
            		while (sub[j] != '=') j --;
            		if (j > 0) j --;
            		while ( j > 0 && sub[j] == ' ') j--;
            		int end = j;
            		while ( j > 0 && Character.isLetter(sub[j]) ) j--;
            		String name = ssub.substring(j, end+1);
                    name = name.replaceAll("\\s+","");
                    System.out.println("--->>>>>> " + name);
                    varSources.add(name);
                    //sub[-1] = 'd';
            	} 
            }
            else break;
            pos += sources.get(i).length() + 1;
          }
        }
      }

      fis.close();
      bis.close();
      dis.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //cleanSpaces();
    System.out.println("Me sources: ");
    for (String i : varSources) {
      System.out.println(i);
    }
    
    //return meSource;
  }

 public static void init( String fileIn) throws IOException{
	 //System.out.println("nameEditvvvvvvvvvvvvvvvvvvvvvvvvvvv " +nameEdit );
	 if( Main.xmlFileIn != null && !Main.xmlFileIn.isEmpty())
		 finEditTextSource(fileIn);
	 
	 getSources( fileIn );
	 nameEdit = null;
	 
 }

	/*public static void cleanSpaces(){
	   for(int i=0; i<tmpSources.size(); i++){
		   char caracteres[] = tmpSources.get(i).toCharArray();
		   String tmp;
		    if( caracteres[0] == ' ' && caracteres[caracteres.length-1]==' ' )
		    	tmp = tmpSources.get(i).substring( 1,caracteres.length-1 ); 
		    else if( caracteres[0] == ' ' )
		    	tmp = tmpSources.get(i).substring( 1 );
		    else if( caracteres[caracteres.length-1]==' ' )
		    	tmp = tmpSources.get(i).substring( 0,caracteres.length-1);
		    else
		    	tmp = tmpSources.get(i);
		    
		    varSources.add(tmp);
	   }
   }*/
   
  /* public static void main(String[] args){
	getSources("/home/h4d4/Escritorio/eule/InputLabelGenerator/ImplicitFlow4.java");
   }*/
}