package test;

import test.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import test.Log;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name ImplicitFlow1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description A value from a source gets obfuscated by two different ways and is then written to the log
 * @dataflow source -> userInputPassword -> if-condition -> sink
 * @number_of_leaks 2
 * @challenges the analysis must be able to handle implicit flows and
 *  treat the value of password fields as source 
	*ADAPTAIONES:
	*	- En metodo obfuscateIMEI: se cambia iterador por for; se adiciona try-catch para NullPointerExceptions
	* - Se omite reallyHardObfuscatedIMEI jif no soporta : la inicialización del array Integer, y newOldIMEI.toString() 
 * - Se adiciona try-cath 
 */
public class ImplicitFlow1 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            String obfuscatedIMEI = obfuscateIMEI(imei);
            writeToLog(obfuscatedIMEI);
        } catch (ClassCastException ignore) {
        } catch (NullPointerException e) {
        }
    }

    private String {Alice:}obfuscateIMEI{Alice:}(String {Alice:}imei) {
        String arg = imei;
        String result = "";
        try {
            char[] array* = arg.toCharArray();
            for (int i = 0; i < array.length; i++) {
                if (array[i] == '0') result += 'a'; else if (array[i] == '1') result += 'b'; else if (array[i] == '2') result += 'c'; else if (array[i] == '3') result += 'd'; else if (array[i] == '4') result += 'e'; else if (array[i] == '5') result += 'f'; else if (array[i] == '6') result += 'g'; else if (array[i] == '7') result += 'h'; else if (array[i] == '8') result += 'i'; else if (array[i] == '9') result += 'j';
            }
        } catch (NullPointerException e) {
        }
        return result;
    }

    private void writeToLog{}(String {}message) {
        Log.i("INFO", message);
    }
}