package com.ginitsys.electors.app;

import android.content.Intent;
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
import java.util.Random;


public class enter_otp extends AppCompatActivity {

    EditText id;
    private String firstname = "firstname";
    private String lastname = "lastname";
    private String phoneno = "123456789";
    private String password = "password";

    domain return_url = new domain();

    Random random = new Random();
    static String secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
    }

    public void verify(View view){
        Intent extras = getIntent();
        secret = extras.getStringExtra("secret");
        final String what_activity = extras.getStringExtra("what_activity");
        if (what_activity.equals("register")) {
            firstname = extras.getStringExtra("firstname");
            lastname = extras.getStringExtra("lastname");
            phoneno = extras.getStringExtra("phoneno");
            password = extras.getStringExtra("password");
            //Toast.makeText(getBaseContext(),firstname+" " +lastname+" "+phoneno,Toast.LENGTH_LONG).show();
        }
        else if (what_activity.equals("fgtpass")){
            phoneno = extras.getStringExtra("phoneno");
            password = extras.getStringExtra("password");
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
        }

        id = (EditText) findViewById(R.id.otptextbox);
        final String identity = id.getText().toString();
        final Intent intent = new Intent(this,MainActivity.class);
        final Intent fgtpass = new Intent(this, forgotpassword.class);
        final Intent regac = new Intent(this, registeration_page.class);

        if (identity.equals(secret)){
            StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"register.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("OK")){
                                Toast.makeText(getBaseContext(), "Your account has been successfully created.", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                            else if(response.equals("c_error")){
                                Toast.makeText(getBaseContext(),"Error creating account.",Toast.LENGTH_LONG).show();
                            }
                            else if(response.equals("ace_error")){
                                Toast.makeText(getBaseContext(),"Account already exists!",Toast.LENGTH_LONG).show();
                                startActivity(fgtpass);
                            }
                            else if(response.equals("u_error")){
                                Toast.makeText(getBaseContext(),"Update error!",Toast.LENGTH_LONG).show();
                            }
                            else if(response.equals("acne_error")){
                                Toast.makeText(getBaseContext(),"Account does not exists!",Toast.LENGTH_LONG).show();
                                startActivity(regac);
                            }
                            else if(response.equals("p_change")){
                                Toast.makeText(getBaseContext(),"Password has been updated!",Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), "Error Connecting to the server!" + error, Toast.LENGTH_LONG).show();
                }
            }
            )
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<>();
                    if(what_activity.equals("register")){
                    params.put("what","register");
                    params.put("firstname", firstname);
                    params.put("lastname", lastname);
                    params.put("phoneno",phoneno);
                    params.put("password",password);
                    return params;
                    }
                    else{
                    params.put("what","change_pass");
                    params.put("phoneno",phoneno);
                    params.put("password",password);
                    return params;
                    }
                }
            };
            Volley.newRequestQueue(this).add(StringRequest);
        }
        else{
            Toast.makeText(getBaseContext(), "OTP did not match.", Toast.LENGTH_LONG).show();
        }
    }

    public void resendOtp(View view){
        Intent extras = getIntent();
        phoneno = extras.getStringExtra("phoneno");
        password = extras.getStringExtra("password");
        secret = extras.getStringExtra("secret");
        Toast.makeText(getBaseContext(),"Requesting new one time password.", Toast.LENGTH_LONG).show();
        StringRequest StringRequest = new StringRequest(Request.Method.POST, "https://2factor.in/API/V1/2d9a3bf8-0bfe-11e9-a895-0200cd936042/SMS/"+phoneno+"/"+secret+
                "/Login+OTP",
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
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }
}