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

public class forgotpassword extends AppCompatActivity {

    Random random = new Random();
    EditText phoneno,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
    }


    public void fgtpass(View view){
        String id = String.format("%04d", random.nextInt(10000));
        Intent goto_otp = new Intent(this, enter_otp.class);
        goto_otp.putExtra("what_activity","fgtpass");
        phoneno = (EditText) findViewById(R.id.phoneno);
        password = (EditText) findViewById(R.id.newpassword);

        String phoneno_to_string = phoneno.getText().toString();
        String password_to_string = password.getText().toString();

        if(phoneno_to_string.length() == 10 &&  password_to_string.length() > 7  && password_to_string.length() < 15){
            goto_otp.putExtra("phoneno",phoneno_to_string);
            goto_otp.putExtra("password",password_to_string);
            goto_otp.putExtra("secret",id);
            //Toast.makeText(getBaseContext(),phoneno_to_string + " " + id,Toast.LENGTH_LONG).show();
            StringRequest StringRequest = new StringRequest(Request.Method.POST, "https://2factor.in/API/V1/2d9a3bf8-0bfe-11e9-a895-0200cd936042/SMS/"+phoneno_to_string+"/"+id+"/Login+OTP",
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
            startActivity(goto_otp);
            Volley.newRequestQueue(this).add(StringRequest);
        }
        else{
            Toast.makeText(getBaseContext(),"Please complete all the fields! Password should be 8 to 14 characters long.", Toast.LENGTH_LONG).show();
        }
    }
}
