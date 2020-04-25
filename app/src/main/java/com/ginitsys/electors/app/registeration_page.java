package com.ginitsys.electors.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Random;

public class registeration_page extends AppCompatActivity {

    Random random = new Random();
    EditText f_name,l_name,p_no,p_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_page);
    }

    public void getotp(View view) {
        f_name = (EditText) findViewById(R.id.firstname);
        l_name = (EditText) findViewById(R.id.lastname);
        p_no = (EditText) findViewById(R.id.phone);
        p_word = (EditText) findViewById(R.id.newpassword);
        String f_name_to_string = f_name.getText().toString();
        String l_name_to_string = l_name.getText().toString();
        String p_no_to_string = p_no.getText().toString();
        String p_word_to_string = p_word.getText().toString();

        if(f_name_to_string.length() !=0 && l_name_to_string.length() != 0 && p_no_to_string.length() == 10 &&  p_word_to_string.length() > 7  && p_word_to_string.length() < 15){
            String id = String.format("%04d", random.nextInt(10000));
            StringRequest StringRequest = new StringRequest(Request.Method.POST, "https://2factor.in/API/V1/2d9a3bf8-0bfe-11e9-a895-0200cd936042/SMS/"+p_no_to_string+"/"+id+"/Login+OTP",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getBaseContext(),"Check your messages for OTP.", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
                }
            }
            );

            Intent change_activity = new Intent(this, enter_otp.class);
            //Toast.makeText(getApplicationContext(),id, Toast.LENGTH_LONG).show();
            change_activity.putExtra("firstname",f_name_to_string);
            change_activity.putExtra("lastname",l_name_to_string);
            change_activity.putExtra("phoneno",p_no_to_string);
            change_activity.putExtra("password",p_word_to_string);
            change_activity.putExtra("secret",id);
            change_activity.putExtra("what_activity","register");

            Volley.newRequestQueue(this).add(StringRequest);
            startActivity(change_activity);
        }
        else {
            Toast.makeText(getBaseContext(), "Please complete all the fields! Password should be 8 to 14 characters long.",Toast.LENGTH_LONG).show();
        }
     }
}