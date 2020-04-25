package com.ginitsys.electors.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class browser extends AppCompatActivity {

    WebView wbview;
    String link[];
    domain return_url = new domain();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        wbview = (WebView) findViewById(R.id.wbview);
        wbview.setWebViewClient(new WebViewClient());

        StringRequest StringRequest = new StringRequest(Request.Method.POST,  return_url.return_domain()+"notify.php?what=link",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        link = response.split(",");
                        wbview.loadUrl(link[1]);

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
            }
        }
        );
        Volley.newRequestQueue(this).add(StringRequest);
    }

    @Override
    protected void onResume(){
        super.onResume();
        wbview = (WebView) findViewById(R.id.wbview);
        wbview.setWebViewClient(new WebViewClient());

        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"notify.php?what=link",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        link = response.split(",");
                        wbview.loadUrl(link[1]);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
            }
        }
        );
        Volley.newRequestQueue(this).add(StringRequest);
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent go_back = new Intent(browser.this, MainActivity.class);
        startActivity(go_back);
    }
}
