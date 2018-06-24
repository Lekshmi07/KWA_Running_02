package com.example.lekshmi.testing;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //DatePicker pickerDate;
    TimePicker pickerTime;
    Button buttonSetAlarm,cancel;
    TextView tv;
    LinearLayout ll;

    private int PHONE_REQUEST = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll=new LinearLayout(this);
        ll=findViewById(R.id.AddedAlarms);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 1);
        //pickerDate = findViewById(R.id.pickerdate);
        pickerTime = findViewById(R.id.pickertime);



        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));

        buttonSetAlarm = findViewById(R.id.setalarm);
        buttonSetAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Calendar cal = Calendar.getInstance();
                cal.set(
                        pickerTime.getCurrentHour(),
                        pickerTime.getCurrentMinute(),
                        0);


                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                Context context = getApplicationContext();
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                final int alarmId = (int) System.currentTimeMillis();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                assert manager!=null;
                // manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

             //  finish();


                //Displaying added Alarms

                String s= cal.getTime().toString();

                tv=new TextView(context);
                tv.setText(s);
                ll.addView(tv);
                cancel=new Button(context);
                cancel.setText("Cancel");
                ll.addView(cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelAlarm(alarmId);
                    }
                });

            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PHONE_REQUEST);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PHONE_REQUEST) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "No permission to use Phone. App won't work as expected", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void cancelAlarm( int ID){



        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        ll.removeView(tv);
        ll.removeView(cancel);


    }

}
