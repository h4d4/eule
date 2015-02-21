package test;

import test.Activity;
import android.os.Bundle;
import test.Log;
import android.widget.EditText;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name PrivateDataLeak2
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail siegfried.rasthofer@cased.de
 * 
 * @description A value from a password field is written to the log
 * @dataflow source -> sink
 * @number_of_leaks 1
 * @challenges the analysis has to treat the value of password fields as source
 */
public class PrivateDataLeak2 extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
        EditText mEdit = (EditText) findViewById(R.id.pwField);
        Log.v("Password", mEdit.getText().toString());
    }
}
