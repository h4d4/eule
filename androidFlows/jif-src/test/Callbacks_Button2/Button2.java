package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.Log;
import android.view.View;
import android.widget.Button;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name Button2
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description Sources and sinks are called in button callbacks. There is only one data leak iff first button3 and then button1 is pressed!
 * @dataflow clickOnButton3: source -> imei; onClick (button1): imei -> sinks 
 * @number_of_leaks 2
 * @challenges the analysis must be able to analyze listeners, know that callback of button3 is ClickOnButton3 (defined in xml file)
 *  and has to handle the arbitrary order of the listener callbacks.
 */
public class Button2 extends Activity {

    private String imei = null;

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SmsManager sm = SmsManager.getDefault();
                String number = "+49 1234";
                sm.sendTextMessage(number, null, imei, null, null);
                Log.i("TAG", "sendIMEI: " + imei);
                imei = null;
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imei = null;
                Log.i("TAG", "Button 2: " + imei);
            }
        });
    }

    public void clickOnButton3{}(View {}view) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        Log.i("TAG", "Button3: " + imei);
    }
}
