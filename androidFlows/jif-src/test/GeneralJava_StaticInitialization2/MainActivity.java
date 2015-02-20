package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name StaticInitialization2
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail Steven.Arzt@cased.de
 * 
 * @description sensitive data is obtained during static initialization of a class and leaked in non-static code
 * @dataflow source -> im -> sink
 * @number_of_leaks 1
 * @challenges the analysis has to consider static initialization
 */
public class MainActivity extends Activity {

    public static String im;

    public static Context c;

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        c = this;
        StaticInitClass1 s1 = new StaticInitClass1();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+49 1234", null, im, null, null);
    }

    public static class StaticInitClass1 {

        static {
            im = ((TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
    }
}
