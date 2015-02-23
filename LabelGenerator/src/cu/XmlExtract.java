package cu;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlExtract {
	
	static File  fXManifest, fXmlFile;
	static Element root, editText;
	static NodeList nListEditText;
	static ArrayList<String> ids = new ArrayList<String>();
	public static void extractRoot( String xml){
		fXManifest = new File(xml);
		fXmlFile = fXManifest;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
				dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse( fXmlFile );
				root = (Element) doc.getDocumentElement(); //Extract the root element	
			}catch(Exception e) {
					e.printStackTrace();
			}
	}
	
	public static void getTagEditText(){
		nListEditText = root.getElementsByTagName( "EditText" );
		for( int i=0; i<nListEditText.getLength(); i++ ){
			editText = (Element) nListEditText.item(i);
			String inputType = editText.getAttribute("android:inputType");
			if( inputType.equals("textPassword") ){
				String tmp = editText.getAttribute("android:id");
				CharSequence cs= "@+id/";
				if( tmp.contains(cs) )
					ids.add( tmp.split("/")[1] );
				else
					ids.add( tmp );
			}
		}

	}
	
	public static ArrayList<String> getIdTextEdit(String xmlF){
		extractRoot(xmlF );
		getTagEditText();
		return ids;
	}
	/*public static void main(String[] args){
		
		extractRoot( "/home/h4d4/Escritorio/testXml.xml" );
		getTagEditText();
		for( int i=0; i<ids.size(); i++){
			System.out.println( ids.get(i));
		}
	}*/
}
