package de.ecspride;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
/**
 * @testcase_name ArrayAccess2
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description an array is created which is filled with untainted and tainted (deviceId source) data.
 *   The untainted data of a calculated array position is retrieved and sent via sms.
 * @dataflow -
 * @number_of_leaks 0
 * @challenges the analysis must distinguish between different array positions and has to evaluate the function to recognize that the tainted
 *  data does not get leaked. 
 */
public class ArrayAccess2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_access2);
        
        String[] array = new String[10];
		try{
						TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); //source
						try{
											array[5] = telephonyManager.getDeviceId();
									}catch(ArrayIndexOutOfBoundsException  e){
									}catch( NullPointerException e){}
									try{
													array[4] = "no taint";
												}catch(ArrayIndexOutOfBoundsException  e){
												}catch( NullPointerException e){}
									try{	
													SmsManager sm = SmsManager.getDefault();
													sm.sendTextMessage("+49 1234", null, array[calculateIndex()], null, null); //sink, no leak
												}catch(ArrayIndexOutOfBoundsException  e){}
					}catch(ClassCastException e){
					}catch( NullPointerException e){}
	}
	
	private int calculateIndex(){
		int index = 1;
		index++;
		index *= 5;
		index = index%10;
		index += 4;
		
		return index;
	}
}
