package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name ActivityLifecycle3
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description The return value of source method is stored to a static variable in one callback method
 *  and sent to a sink in a different callback method
 * @dataflow onSaveInstanceState: source -> s; onRestoreInstanceState: s -> sink
 * @number_of_leaks 1
 * @challenges the analysis must be able to handle the activity lifecycle correctly,
 *  in particular the less common instanceState callbacks
 */
public class MainActivity extends Activity {

    public String s;

    @Override
    protected void {Alice:}onCreate{Alice:}(Bundle {Alice:}savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
    }

    @Override
    public void onSaveInstanceState{}(Bundle {}outState) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        s = telephonyManager.getSubscriberId();
    }

    @Override
    public void onRestoreInstanceState{}(Bundle {}outState) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+49 1234", null, s, null, null);
    }
}
