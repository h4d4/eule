package test;

import test.Activity;
import android.os.Bundle;
import test.Log;
import android.view.Menu;
import test.R;
import jif.runtime.Runtime;

/**
 * @testcase_name RegisterGlobal1
 * @version 0.1
 * @author Secure Software Engineering Group (SSE), European Center for Security and Privacy by Design (EC SPRIDE) 
 * @author_mail steven.arzt@cased.de
 * 
 * @description Both source and sink are part of a global (application-level) lifecycle handler.
 * @dataflow OnCreate: source -> imei; sendMessage: imei -> sink
 * @number_of_leaks 1
 * @challenges The analysis must support globally-registered callback handlers
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate{}(Bundle savedInstanceState) {
        Log.d("EX", "Activity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout);
    }

    @Override
    protected void onRestoreInstanceState{}(Bundle savedInstanceState) {
        Log.d("EX", "Activity.onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState{}(Bundle outState) {
        Log.d("EX", "Activity.onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostCreate{}(Bundle savedInstanceState) {
        Log.d("EX", "Activity.onPostCreate()");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu{}(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart{}() {
        super.onStart();
        Log.d("EX", "Activity.onStart()");
    }

    @Override
    protected void onPause{}() {
        super.onPause();
        Log.d("EX", "Activity.onPause()");
    }
}
