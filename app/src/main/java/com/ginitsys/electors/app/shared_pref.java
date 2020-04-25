package com.ginitsys.electors.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class shared_pref {
    Context context;

    String cookie_in_the_pot;
    domain return_url = new domain();

    shared_pref(Context context){
        this.context = context;
    }

    protected void cookies(String pno, String cookie){
        SharedPreferences sharedPreferences = context.getSharedPreferences("logindetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone",pno);
        editor.putString("ac_name",cookie);
        editor.apply();
    }

    protected boolean login_status(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("logindetails", Context.MODE_PRIVATE);
        boolean isEmailEmpty = sharedPreferences.getString("phone", "").isEmpty();
        boolean isPasswordEmpty = sharedPreferences.getString("ac_name", "").isEmpty();
        return isEmailEmpty || isPasswordEmpty;
    }

    protected String return_phone(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("logindetails", Context.MODE_PRIVATE);
        final String phone_no;
        phone_no = sharedPreferences.getString("phone",null);
        return phone_no;
    }

    protected String return_cookie(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("logindetails", Context.MODE_PRIVATE);
        final String cookie;
        cookie = sharedPreferences.getString("ac_name",null);
        return cookie;
    }


    protected void logout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("logindetails",Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }



    protected void get_required_prefs(final String p_number,final String pass){
        StringRequest request = new StringRequest(Request.Method.POST, return_url.return_domain()+"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        cookie_in_the_pot = response;
                        cookies(p_number,cookie_in_the_pot);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"There was an error contacting server!",Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(context).add(request);
    }
}