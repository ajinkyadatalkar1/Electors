package com.ginitsys.electors.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class comments extends AppCompatActivity {

    WebView comments_loader;
    TextView title;
    String constituency;
    String state;
    boolean isLegit=false;

    domain return_url = new domain();

    shared_pref pref = new shared_pref(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent getExtraData = getIntent();

        state = getExtraData.getStringExtra("state");
        constituency = getExtraData.getStringExtra("constituency");

        title = (TextView) findViewById(R.id.constituency_name);
        title.setText(constituency + " Constituency");
        comments_loader = (WebView) findViewById(R.id.comments_loader);
        verify_and_allow(this, pref.return_phone(),pref.return_cookie());
        WebSettings webSettings = comments_loader.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    protected void verify_and_allow(final Context context,final String p_number,final String cookie){
        StringRequest request = new StringRequest(Request.Method.POST, return_url.return_domain()+"verify.php?what=first",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("error")){
                            try {
                                String postdata = "p_number=" + URLEncoder.encode(pref.return_phone(),"UTF-8") + "&cookie=" + URLEncoder.encode(pref.return_cookie(),"UTF-8");
                                comments_loader.postUrl(return_url.return_domain()+"comments.php?district=" + state + "&constituency=" + constituency, postdata.getBytes());
                                comments_loader.setWebViewClient(new WebViewClient());
                            }
                            catch (UnsupportedEncodingException e){

                            }
                        }
                        else{
                            pref.logout();
                        }

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
                params.put("cookie", cookie);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}