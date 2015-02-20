package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import test.Log;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name MethodOverride1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description This example contains a location information leakage in the attachBaseContext(Context base) callback method.
 * @dataflow attachBaseContext: source -> uid -> sink 
 * @number_of_leaks 1
 * @challenges the analysis must be able to detect the leak in an internal Android method that is overwritten.
 */
public class MethodOverride1 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
    }

    @Override
    protected void attachBaseContext{}(Context {}base) {
        super.attachBaseContext(base);
        TelephonyManager tManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String uid = tManager.getDeviceId();
        Log.d("EX", uid);
    }
}
