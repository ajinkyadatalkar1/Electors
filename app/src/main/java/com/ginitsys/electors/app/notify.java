package com.ginitsys.electors.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

public class notify extends Service {
    int inc=0;
    domain return_url = new domain();
    String title, link;
    String receive_array[];
    static String link_no = "-1";
    Timer timer = new Timer();
// Create Timer *************************************************************************************************************************
    public void AsyncTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                check_max_id();
            }
        };
        timer.schedule(task,0, 1000*60);//start the timer
    }


    @Override
    public  void onCreate(){

    }


//Bind service function *****************************************************************************************************************
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

// Service start function ****************************************************************************************************************
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        AsyncTimer(); // called the function that holds the timer.
        return Service.START_STICKY;
    }


    @Override
    public void onDestroy(){

    }

    public void check_max_id(){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"notify.php?what=id",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!link_no.equals(response)){
                            link_no = response;
                            get_title_link();
                        }
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

    public void get_title_link(){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, return_url.return_domain()+"notify.php?what=link",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        receive_array = response.split(",");
                        link = receive_array[1];
                        title = receive_array[0];
                        notification();
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


//notification manager function *****************************************************************************************************************
    public void notification(){
        NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this,"electors");
        mbuilder.setSmallIcon(R.drawable.electors_logo);
        mbuilder.setContentTitle(title);
        mbuilder.setAutoCancel(true);

        Intent notificationIntent = new Intent(getApplicationContext(), browser.class);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        mbuilder.setContentIntent(contentIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("com.ginitsys.electors.app", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{300});
            assert manager != null;
            mbuilder.setChannelId("com.ginitsys.electors.app");
            manager.createNotificationChannel(notificationChannel);
        }

        manager.notify(0, mbuilder.build());
    }
}
