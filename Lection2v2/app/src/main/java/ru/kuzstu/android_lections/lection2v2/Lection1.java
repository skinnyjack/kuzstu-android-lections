package ru.kuzstu.android_lections.lection2v2;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by bug on 24.09.16.
 */
public class Lection1 extends Fragment {
    private Button btn1;
    private ImageView img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_lection1,container,false);
        btn1 = (Button)result.findViewById(R.id.btn1);
        img = (ImageView)result.findViewById(R.id.imgv1);
        btn1.setText("Не нажимать");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Всё пропало. Смирись.", Toast.LENGTH_SHORT).show();
                img.setImageResource(R.drawable.raging_code_monkey_by_plognark);
                Log.e("LECTION2","Ты всё сломал.");
            }
        });

        return result;
    }
}
