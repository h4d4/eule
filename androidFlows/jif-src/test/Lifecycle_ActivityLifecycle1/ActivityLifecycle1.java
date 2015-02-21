package test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import test.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name ActivityLifecycle1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description The return value of source method is stored to a static variable in one callback method
 *  and sent to a sink in a different callback method
 * @dataflow onCreate: source -> imei -> URL; onResume: URL -> sink
 * @number_of_leaks 1
 * @challenges the analysis must be able to handle the activity lifecycle correctly and
 *  handle try/catch blocks
 */
public class ActivityLifecycle1 extends Activity {

    private static String URL = "http://www.google.de/search?q=";

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        URL = URL.concat(imei);
    }

    @Override
    protected void onStart{}() {
        super.onStart();
        try {
            connect();
        } catch (Exception ex) {
        }
    }

    private void connect{}() throws IOException {
        URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
    }
}
