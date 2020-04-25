package com.ginitsys.electors.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;

public class selectconstituency extends AppCompatActivity {



    Spinner dropdown1, dropdown2;
    ArrayAdapter<String> adapter_state;
    ArrayAdapter<String> adapter_constituency;

    String states[] = {"Select State"};
    ArrayList<String> state_list = new ArrayList<String>(Arrays.asList(states));
    String constituency[] = {"Select Constituency"};
    ArrayList<String> constituency_list = new ArrayList<String>(Arrays.asList(constituency));
    String get_state,selected_constituency;
    Button mBtn;

    Intent updateprofile;
    shared_pref pref = new shared_pref(this);
    domain return_url = new domain();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectconstituency);

// create spinner and call state and condtituency spinner **************************************************************************************************************

        loadstates();
        dropdown1 = (Spinner) findViewById(R.id.selectstate);
        adapter_state = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, state_list);
        adapter_state.setDropDownViewResource(android.R.layout.simple_list_item_1);
        dropdown1.setAdapter(adapter_state);


        dropdown2 = (Spinner) findViewById(R.id.select_constituency);
        adapter_constituency = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, constituency_list);
        adapter_constituency.setDropDownViewResource(android.R.layout.simple_list_item_1);
        dropdown2.setAdapter(adapter_constituency);
        dropdown2.setEnabled(false);






//**************** on state selected*********************************************************************************************
        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                get_state = parentView.getItemAtPosition(position)+"";
                //Toast.makeText(getBaseContext(), "Loading Constituencies for the state "+get_state, Toast.LENGTH_SHORT).show();
                loadconstituency();
                dropdown2.setEnabled(false);
                constituency_list.clear();
                constituency_list.add(0,"Select Constituency");

                if (get_state.equals("Select State") || get_state.equals("") || get_state.equals(" ") || get_state.equals("c_error")) {
                    dropdown2.setEnabled(false);
                    mBtn.setEnabled(false);
                }
                else {
                    dropdown2.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(),"Please Select State",Toast.LENGTH_LONG).show();
            }
        });


//**************on Constituency selected ******************************************************************************
        dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                selected_constituency = parentView.getItemAtPosition(position)+"";
                if (selected_constituency.equals("Select Constituency") || selected_constituency.equals("") || selected_constituency.equals(" ") || selected_constituency.equals("c_error")){
                     mBtn.setEnabled(false);
                }
                else{
                    mBtn.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getBaseContext(),"Please Select Constituency",Toast.LENGTH_LONG).show();
            }
        });
//******************************************************************************************************************
        mBtn = (Button)findViewById(R.id.goto_profile);
        mBtn.setEnabled(false);
    }

//**************** Logout Button ***********************************************************************************
    public void onlogout(View view){
        pref.logout();
        updateprofile = (Intent) new Intent(this, MainActivity.class);
        finish();
        startActivity(updateprofile);
    }


    public void select_candidate(View view) {
        if(!pref.login_status()) {
            Intent change_page = new Intent(this, selectcandidate.class);
            change_page.putExtra("constituency", selected_constituency);
            change_page.putExtra("state", get_state);
            startActivity(change_page);
        }
        else{
            onlogout(view);
        }
    }


//*************Populate states in the spinner****************************************************************************
    private void loadstates() {
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"sendarray.php?get_what=state",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        states = response.split(",");
                        setStates(states);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }

    private void setStates(String[] s){
        for (int i=0;i<states.length;i++){
               state_list.add(i,s[i]);
               adapter_state.notifyDataSetChanged();
        }
    }
//**********************************************************************************************************************


//*************Populate constituencies based on states in the spinner***************************************************************

    private void loadconstituency() {
        get_state = get_state.replace(" ","+");
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"sendarray.php?get_what=constituency&state=" + get_state,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constituency = response.split(",");
                        //Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                        setConstituencies(constituency);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }

    private void setConstituencies(String[] c){
        for (int i=0;i<constituency.length;i++){
            constituency_list.add(i,c[i]);
            adapter_constituency.notifyDataSetChanged();
        }
    }

//******************* Back button pressed ************************************************************************************
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

//******************* Class ends here ************************************************************************************
}


