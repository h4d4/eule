package test;

import java.util.LinkedList;
import java.util.List;
import test.Activity;
import android.content.Context;
import android.os.Bundle;
import test.SmsManager;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name ListAccess1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description a list is created which is filled with untainted and tainted (deviceId source) data.
 *   The untainted data of a constant list position is retrieved and sent via sms.
 * @dataflow -
 * @number_of_leaks 0
 * @challenges the analysis must distinguish between different list positions to recognize that the tainted
 *  data does not get leaked. 
 */
public class ListAccess1 extends Activity {

    List<String> listData;

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        listData = new LinkedList<String>();
        listData.add("not tainted");
        listData.add(((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
        listData.add("neutral text");
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("+49 1234", null, listData.get(0), null, null);
    }
}