package de.ecspride;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
	* ADAPTACIONES: - adicionar una variable String donde se guarda la info, pasar la variable al log
	* Al -out.jif toca comentarle las lineas de EditText y la asignacion de info, JIF no reconoce el casting de EdidText y View
 */
public class PrivateDataLeak2 extends Activity {
				String info = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_data_leak2);
        
        EditText mEdit   = (EditText)findViewById(R.id.pwField);
								info = mEdit.getText().toString();
		Log.v("Password", info); //source, sink, leak
    }
}
