package cu;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Character;
import java.util.*;

public class GetSource {

  public static void main(String[] args) {

    File file = new File("/home/h4d4/Escritorio/eule/InputLabelGenerator/MainActivity.java");
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

    ArrayList<String> meSource = new ArrayList<String>();

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
                meSource.add(name);
              }
              else if (sub[j] == '=') {
                j--;
                while ( j > 0 && sub[j] == ' ') j--;
                int end = j;
                while ( j > 0 && Character.isLetter(sub[j]) ) j--;
                name = ssub.substring(j, end+1);
                meSource.add(name);
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

    System.out.println("Me sources: ");
    for (String i : meSource) {
      System.out.println(i);
    }
  }
}