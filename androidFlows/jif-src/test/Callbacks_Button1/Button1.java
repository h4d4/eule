package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name Button1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description The sink is called after the user clicks a button. The button
 *  handler is defined via XML.
 * @dataflow OnCreate: source -> imei; sendMessage: imei -> sink
 * @number_of_leaks 1
 * @challenges the analysis must analyze the layout xml file and take the lifecycle into account (onCreate is executed before user interaction)
 */
public class Button1 extends Activity {

    private static String imei = null;

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
    }

    public void sendMessage{}(View {}view) {
        Toast.makeText(this, imei, Toast.LENGTH_LONG).show();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+49", null, imei, null, null);
    }
}
