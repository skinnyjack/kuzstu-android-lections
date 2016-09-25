package ru.kuzstu.android_lections.lection1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private ImageView imgv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgv = (ImageView)findViewById(R.id.imageView);

        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"За нас, за вас, за северный кавказ!",Toast.LENGTH_SHORT).show();
                imgv.setImageResource(R.drawable.raging_code_monkey_by_plognark);
                Log.e("TEST", "Всё хорошо, мам.");
            }
        });
    }
}
