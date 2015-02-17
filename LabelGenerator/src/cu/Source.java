package cu;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Character;
import java.util.*;

public class Source {
	
	/**
	 * @param fileIn
	 * @return
	 */
	public static ArrayList<String> varSources = new ArrayList<String>();
	public static ArrayList<String> tmpSources = new ArrayList<String>();
	
	public static void getSources( String fileIn ){

    File file = new File(fileIn);
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    DataInputStream dis = null;

    String sources[] = new String[6];
    sources[0] = "getDeviceId";
    sources[1] = "getSimSerialNumber";
    sources[2] = "findViewById";
    sources[3] = "getLatitude";
    sources[4] = "getLongitude";
    sources[5] = "getText().toString()";

    //ArrayList<String> meSource = new ArrayList<String>();

    try {
      fis = new FileInputStream(file);

      bis = new BufferedInputStream(fis);
      dis = new DataInputStream(bis);

      while (dis.available() != 0) {
        @SuppressWarnings("deprecation")
		String line = dis.readLine();

        for (int i = 0; i < sources.length; ++i) {
          int pos = 0;

          while (pos < line.length()) {
            String ssub = line.substring(pos).trim();
            char sub[] = ssub.toCharArray();
            pos = ssub.indexOf( sources[i] );

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
            }
            pos += sources[i].length() + 1;
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
   /* System.out.println("Me sources: ");
    for (String i : varSources) {
      System.out.println(i);
    }*/
    
    //return meSource;
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