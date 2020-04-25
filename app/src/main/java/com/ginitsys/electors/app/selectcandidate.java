package com.ginitsys.electors.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;

public class selectcandidate extends AppCompatActivity{

    String candidates[] = {""};
    String candidate_serial[] = {""};
    ListView list_candidates;
    ArrayAdapter adapter;

    String getconstituency;
    String getState;

    domain return_url = new domain();

    ArrayList<String> list = new ArrayList<String>(Arrays.asList(candidates));
    String get_candidate_from_adapter;
    shared_pref pref = new shared_pref(this);


    Intent getprofile;
    Intent load_comment_screen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcandidate);
        // Check login status **************************************************************************************************************
        if (pref.login_status()){
            startActivity(new Intent(this, MainActivity.class));
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        list.clear();
        Intent getExtraData = getIntent();
        getconstituency = getExtraData.getStringExtra("constituency");
        getState = getExtraData.getStringExtra("state");
        list_candidates = (ListView) findViewById(R.id.candidate_list);
        loadcandidates();


        list_candidates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                get_candidate_from_adapter = adapterView.getItemAtPosition(position)+"";
                getprofile = (Intent) new Intent(selectcandidate.this, candidateprofile.class);
                getprofile.putExtra("mantri_name",get_candidate_from_adapter);
                getprofile.putExtra("serial",candidate_serial[position]);
                startActivity(getprofile);
            }
        });
    }

    // Check login status on resume *********************************************************************************
    @Override
    protected void onResume(){
        super.onResume();
        if(pref.login_status()){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    protected void loadcandidates(){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"sendarray.php?get_what=candidate&constituency=" + getconstituency,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        candidates = response.split(",");
                        //Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                        setcandidates(candidates);
                        list_candidates.setAdapter(adapter);
                        setserialnos();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }

    protected void setcandidates(String c[]){
        for (int i=0;i<candidates.length-1;i++){
            list.add(i,c[i]);
        }
    }

    protected void setserialnos(){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"sendarray.php?get_what=serial&constituency=" + getconstituency,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        candidate_serial = response.split(",");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }

    public void load_comments(View view){
        load_comment_screen = (Intent) new Intent(this, comments.class);
        load_comment_screen.putExtra("constituency", getconstituency);
        load_comment_screen.putExtra("state",getState);
        startActivity(load_comment_screen);
    }
}