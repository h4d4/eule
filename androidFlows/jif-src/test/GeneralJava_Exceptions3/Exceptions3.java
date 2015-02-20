package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name Exceptions3
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description tainted data is created, but the exception handler which would send it out is never invoked
 * @dataflow source -> imei -> /
 * @number_of_leaks 0
 * @challenges the analysis must precisely model which exceptions can occur and which ones can't
	* ADAPTACIONES:
	* - 38 y 41 try-catch NullPointerException
 */
public class Exceptions3 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            int[] arr = new int[42];
            if (arr[32] > 0) imei = "";
        } catch (RuntimeException ex) {
            try {
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage("+49 1234", null, imei, null, null);
            } catch (NullPointerException e) {
            }
        }
    }
}
