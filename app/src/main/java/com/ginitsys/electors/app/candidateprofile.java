package com.ginitsys.electors.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;



public class candidateprofile extends AppCompatActivity {

    domain return_url = new domain();

    Bitmap bmp;
    String profilepicurl = return_url.return_domain()+"mantris/";
    String partypicture = return_url.return_domain()+ "mantris/";
    String picturesurl = return_url.return_domain()+"mantris/";
    String about;
    int noofpics=0;
    int i = noofpics;
    Map<String,String> params = new HashMap<>();

    TextView load_text;
    LinearLayout layout;
    Intent GetIntent;
    String profile[]={""};

    TextView t_view;

    shared_pref pref = new shared_pref(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetIntent = getIntent();
        get_text_from_server();
        setContentView(R.layout.activity_candidateprofile);

// Check login status **************************************************************************************************************
        if (pref.login_status()){
            startActivity(new Intent(this, MainActivity.class));
        }

// Load text data from the server **************************************************************************************************************
        set_text_data();
        t_view = (TextView)findViewById(R.id.bios);
        t_view.setMovementMethod(new ScrollingMovementMethod());

// Load picture functions **************************************************************************************************************
        this.loadprofilepic();
        this.loadpartysymbol();
        this.loadpictures();
    }

// Check login status on resume *********************************************************************************
    @Override
    protected void onResume(){
        super.onResume();
        if(pref.login_status()){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    // Set mantris text data                    ******************************************************************************************************************************************
    protected void set_text_data(){
        load_text = (TextView)findViewById(R.id.textView7);
        load_text.setText(GetIntent.getStringExtra("mantri_name"));
        //Toast.makeText(getApplicationContext(),GetIntent.getStringExtra("serial"),Toast.LENGTH_LONG).show();
        profilepicurl += GetIntent.getStringExtra("serial") + "/profile.png";
        partypicture +=  GetIntent.getStringExtra("serial") + "/partysymbol.png";
        picturesurl += GetIntent.getStringExtra("serial") + "/pictures/";
        this.load_picture_nos();
        this.load_about();
    }


    // Get mantris text data                      ******************************************************************************************************************************************
    protected void get_text_from_server(){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"sendarray.php?get_what=profile&serial="+GetIntent.getStringExtra("serial"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            profile = response.split("%");
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            profile[profile.length] = " ";
                        }


                        try{
                            load_text = (TextView)findViewById(R.id.textView8);
                            load_text.setText(profile[0]);
                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            load_text = (TextView)findViewById(R.id.textView8);
                            load_text.setText(" ");
                        }


                        try{
                            load_text = (TextView)findViewById(R.id.textView9);
                            load_text.setText("Age: "+profile[1]);
                        }catch(ArrayIndexOutOfBoundsException e){
                            load_text = (TextView)findViewById(R.id.textView9);
                            load_text.setText(" ");
                        }

                        try{
                            load_text = (TextView)findViewById(R.id.textView11);
                            load_text.setText(profile[2]);
                        }catch (ArrayIndexOutOfBoundsException e){
                            load_text = (TextView)findViewById(R.id.textView11);
                            load_text.setText(" ");
                        }


                        try{
                            load_text = (TextView)findViewById(R.id.textView12);
                            load_text.setText(profile[4]);
                        }catch (ArrayIndexOutOfBoundsException e){
                            load_text = (TextView)findViewById(R.id.textView12);
                            load_text.setText("Address and Contact details unavailable!");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }


    // load and display mantris profile picture ******************************************************************************************************************************************
    protected void loadprofilepic() {
        new DownloadImageFromInternet((ImageView) findViewById(R.id.imageView))
            .execute(this.profilepicurl);
    }

    // load and display party symbol            ******************************************************************************************************************************************
    protected void loadpartysymbol(){
        new DownloadImageFromInternet((ImageView) findViewById(R.id.imageView2))
                .execute(this.partypicture);
    }

    // load photos downloaded on the profile        ******************************************************************************************************************************************
    protected void loadpictures(){
        layout = (LinearLayout) findViewById(R.id.gallery);
        final Intent magnify = new Intent(this, showpicture.class);
        while(this.i>=0){
            final ImageView image = new ImageView(this);
            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(200,150));
            image.setMaxHeight(27);
            image.setMaxWidth(64);
            image.setId(i);
            image.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    magnify.putExtra("piclink", picturesurl + image.getId() + ".png");
                    startActivity(magnify);
                }
            });
            new DownloadImageFromInternet((ImageView) image)
                .execute(this.picturesurl + i + ".png");
            layout.addView(image);
            this.i--;
        }
    }


    // load the number of pictures a manti has on his profile. ******************************************************************************************************************************************
    protected void load_picture_nos(){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"sendarray.php?get_what=pictures&serial=" + GetIntent.getStringExtra("serial"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        noofpics = Integer.parseInt(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }

    //load mantris about data.                                  ******************************************************************************************************************************************
    protected void load_about(){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"sendarray.php?get_what=about&serial=" + GetIntent.getStringExtra("serial"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        about = response;
                        t_view.setText(about);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Error Connecting to the server!", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(StringRequest);
    }


    //Download all sorts of images present on the mantris profile. ******************************************************************************************************************************************
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}