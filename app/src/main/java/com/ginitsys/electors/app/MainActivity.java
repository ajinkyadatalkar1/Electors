package com.ginitsys.electors.app;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    shared_pref pref = new shared_pref(this);
    EditText phone_number, password;
    domain return_url = new domain();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getBaseContext(),"Hi and Welcome",Toast.LENGTH_SHORT).show();

        if(!pref.login_status()){
            startActivity(new Intent(this, selectconstituency.class));
        }
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(), notify.class));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!pref.login_status()){
            startActivity(new Intent(this, selectconstituency.class));
        }
    }


    public void login(View view){
        phone_number = (EditText) findViewById(R.id.phonenumber);
        password = (EditText) findViewById(R.id.newpassword);

        if(phone_number.length() == 10 && password.length() >=7 && password.length() <= 12) {
            final String p_number = phone_number.getText().toString();
            final String pass = password.getText().toString();
            final Intent change_constituency = new Intent(this, selectconstituency.class);

            Toast.makeText(getBaseContext(), "Verifying Phone No. and Password with the server.", Toast.LENGTH_LONG).show();
            StringRequest request = new StringRequest(Request.Method.POST, return_url.return_domain()+"login.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length() == 64) {
                                pref.get_required_prefs(p_number,pass);
                                startActivity(change_constituency);
                            } else if (response.equals("c_error")) {
                                Toast.makeText(getBaseContext(), "Credentials could not be verified.Please check username and password!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Your activity will be reported!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), "Error Connecting to the server!" + error, Toast.LENGTH_LONG).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("username", p_number);
                    params.put("password", pass);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);
        }
        else{
            Toast.makeText(getApplicationContext(),"Phone number should be 10 characters long and password should be between 7 to 12 characters", Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    public void register(View view){
        startActivity(new Intent(this, registeration_page.class));
    }

    public  void forgotpass(View view) {
        startActivity(new Intent(this, forgotpassword.class));
    }
}
