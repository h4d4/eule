package de.ecspride;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
/**
 * @testcase_name ArrayAccess1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description an array is created which is filled with untainted and tainted (deviceId source) data.
 *  The untainted data of a constant array position is retrieved and sent via sms.
 * @dataflow -
 * @number_of_leaks 0
 * @challenges the analysis must distinguish between different array positions to recognize that the tainted
 *  data does not get leaked. 
 */
public class ArrayAccess1 extends Activity {
	public static String[] arrayData;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_access1);
        
        arrayData = new String[3];
		
		try{
							arrayData[0] = "element 1 is tainted:";
					}catch(ArrayIndexOutOfBoundsException  e){
					}catch( NullPointerException e){}

		try{
							arrayData[1] = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(); //source
					}catch(ArrayIndexOutOfBoundsException  e){
					}catch(ClassCastException e){
					}catch( NullPointerException e){}
	
				//arrayData[2] is not tainted
				try{
								arrayData[2] = "neutral text";
					}catch(ArrayIndexOutOfBoundsException  e){
					}catch( NullPointerException e){}
		
				try{
		  SmsManager sms = SmsManager.getDefault();
				//no data leak: 3rd argument of sendTextmessage() is not tainted
    sms.sendTextMessage("+49 1234", null, arrayData[2], null, null);  //sink, no leak
				}catch(ArrayIndexOutOfBoundsException  e){
				}catch( NullPointerException e){}

    }    
}
