package ru.kuzstu.android_lections.lection2v2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

public class Lection2Activity extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lection2);
        String caption = this.getIntent().getStringExtra("button_caption");
        btn = (Button)findViewById(R.id.lection2btn);
        btn.setText(caption);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(v.getContext());

                builder.setSmallIcon(R.drawable.ic_menu_camera);
                builder.setContentTitle("Уведомление");
                builder.setContentText("Шмуведомление");

                Intent resultIntent = new Intent(v.getContext(),MainActivity.class);
                PendingIntent intent = PendingIntent.getActivity(v.getContext(),0, resultIntent, 0);
                builder.setContentIntent(intent);
                //Немножко магии.
                NotificationManager nmanager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                nmanager.notify(0,builder.build());
            }
        });
    }
}
