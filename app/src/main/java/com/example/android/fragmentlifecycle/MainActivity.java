package com.example.android.fragmentlifecycle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String TAG="MainActivity";
    private String currentAppOpenTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, new OneFragment()).commit();

        Button btnLastAppOpenTime =  findViewById(R.id.btn_lastAppTime);
        Button btnLastAdditionTime = findViewById(R.id.btn_lastAdditionTime);
        Button btnLastDisplayTime = findViewById(R.id.btn_lastDisplayTime);

        PrefManager.setContext(getApplicationContext());


        btnLastAppOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "App was last opened on: "+PrefManager.getLastAppOpenTime(),Toast.LENGTH_LONG).show();
            }
        });

        btnLastAdditionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupMessage();
            }
        });

        btnLastDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " notification button");
                addNotification();
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();

        currentAppOpenTime = getTimeStamp(System.currentTimeMillis());
    }


    @Override
    protected void onStop() {
        super.onStop();
        PrefManager.setLastAppOpenTime(currentAppOpenTime);
    }


    private void ShowPopupMessage() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(PrefManager.getLastAddedTime());
        dialog.setTitle("Last Employee Addition Time:");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }



    private void addNotification() {

        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.O)
        {
            NotificationChannel channel= new NotificationChannel("channel_id_1", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this,"channel_id_1")
                        .setSmallIcon(R.drawable.ic_message)
                        .setContentTitle("Last Display Time")
                        .setContentText(PrefManager.getLastDisplayTime())
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(0, builder.build());

        Log.d(TAG, " notification function");
    }


    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);

        return date;
    }
}