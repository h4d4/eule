package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name FieldSensitivity4
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description An object is created. It has a field with a default value which is sent to a sink. Afterwards a tainted 
 *  value is written to the field. 
 * @dataflow -
 * @number_of_leaks 0
 * @challenges the analysis has to consider the order of the statements for tainted fields.
 */
public class FieldSensitivity4 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        Datacontainer data1 = new Datacontainer();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+49 1234", null, data1.value, null, null);
        data1.value = imei;
    }

    class Datacontainer {

        String value = "android";
    }
}
