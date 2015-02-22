package test;

import test.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name IntentSink1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description The value of a source is stored in an intent which is set as a result of this activity. The activity finishes afterwards.
 * @dataflow onCreate: source -> imei -> intent -> sink
 * @number_of_leaks 1
 * @challenges the analysis must be able to track the taint in the intent and recognize the setResult sink. 
 */
public class IntentSink1 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        Intent intent = this.getIntent();
        intent.putExtra("secret", imei);
        this.setResult(RESULT_OK, intent);
        finish();
    }
}