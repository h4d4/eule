package test;

import test.Activity;
import android.os.Bundle;
import test.Log;
import android.view.View;
import android.widget.EditText;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name ImplicitFlow4
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description Based on an input of a password field a log message is written
 * @dataflow source -> password -> if-condition -> sink
 * @number_of_leaks 2
 * @challenges the analysis must be able to handle implicit flows,
 *  detect callbacks from layout xml file and treat the value of password fields as source
 */
public class ImplicitFlow4 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
    }

    public void checkUsernamePassword{}(View {}view) {
        EditText editPassword = (EditText) findViewById(R.id.password);
        EditText editUsername = (EditText) findViewById(R.id.username);
        String password = editPassword.getText().toString();
        String username = editUsername.getText().toString();
        Log.i("TAG", "bevor try");
        try {
            boolean passwordCorrect = lookup(username, password);
            if (passwordCorrect) Log.i("INFO", "password correct"); else Log.i("INFO", "password not correct");
        } catch (Exception ex) {
            Log.i("INFO", "username not available");
        }
        Log.i("TAG", "after try");
    }

    private boolean {Alice:}lookup{Alice:}(String {Alice:}username, String {Alice:}password) throws Exception {
        if (!username.equals("hanns")) throw new Exception("username not available"); else if (username.equals("hanns") && !password.equals("superSecure")) return false; else return true;
    }
}
