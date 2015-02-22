package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name Loop1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description tainted data is created and sent to a sink after it was transformed in a loop.
 * @dataflow source -> imei -> obfuscated -> sink
 * @number_of_leaks 1
 * @challenges the analysis must handle standard java constructs
	* ADAPTACIONES: -cambio de iterador por for
	* - captura de excepciones  NullPointerException, ClassCastException ignore
 */
public class LoopExample1 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            String obfuscated = "";
            char c[] = imei.toCharArray();
            for (int i = 0; i < c.length; i++) {
                obfuscated += c[i] + "_";
            }
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage("+49 1234", null, obfuscated, null, null);
        } catch (NullPointerException e) {
        } catch (ClassCastException ignore) {
        }
    }
}