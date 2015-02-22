package de.ecspride;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
/**
 * @testcase_name ImplicitFlow1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description A value from a source gets obfuscated by two different ways and is then written to the log
 * @dataflow source -> userInputPassword -> if-condition -> sink
 * @number_of_leaks 2
 * @challenges the analysis must be able to handle implicit flows and
 *  treat the value of password fields as source 
	*ADAPTAIONES:
	*	- En metodo obfuscateIMEI: se cambia iterador por for; se adiciona try-catch para NullPointerExceptions
	* - Se omite reallyHardObfuscatedIMEI jif no soporta : la inicialización del array Integer, y newOldIMEI.toString() 
 * - Se adiciona try-cath 
 */
public class ImplicitFlow1 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_implicit_flow1);
			try{
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId(); //source 
		String obfuscatedIMEI = obfuscateIMEI(imei);
		writeToLog(obfuscatedIMEI);
		}catch (ClassCastException ignore) {
		}catch(NullPointerException e){}
		}
		
		//hard to detect
		//obfuscatedIMEI = reallyHardObfuscatedIMEI(imei);
		//writeToLog(obfuscatedIMEI); 

	private String obfuscateIMEI(String imei){
		String arg = imei;	
		String result = "";
		try{
			char [] array = arg.toCharArray();
		
			for(int i=0; i< array.length; i++){
			if ( array[i] == '0' )
				result +='a';
			else if ( array[i] == '1' )
				result +='b';
			else if ( array[i] == '2' )
				result +='c';
			else if ( array[i] == '3' )
				result +='d';
			else if ( array[i] == '4' )
				result +='e';
			else if ( array[i] == '5' )
				result +='f';
			else if ( array[i] == '6' )
				result +='g';
			else if ( array[i] == '7' )
				result +='h';
			else if ( array[i] == '8' )
				result +='i';
			else if ( array[i] == '9' )
				result +='j';
			/*else 
				System.err.println("Problem in obfuscateIMEI for character: " + array[i]);*/
			}
		}catch(NullPointerException e){
		}
	        return  result;
		
	}
	
		/*private String reallyHardObfuscatedIMEI(String imei){
		//ASCII values for integer: 48-57
	Integer[] numbers = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,
				20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,
				40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57};

		char[] imeiAsChar = imei.toCharArray();
		
		char[] newOldIMEI = new char[imeiAsChar.length];
		
		for(int i = 0; i < imeiAsChar.length; i++){
			newOldIMEI[i] = Character.forDigit(numbers[(int)imeiAsChar[i]], 10);
		}
		
		return newOldIMEI.toString();
	}*/
	
	private void writeToLog(String message){
		Log.i("INFO", message); //sink
	}
}
