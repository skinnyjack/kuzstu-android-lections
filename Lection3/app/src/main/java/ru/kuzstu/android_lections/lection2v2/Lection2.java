package ru.kuzstu.android_lections.lection2v2;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by bug on 24.09.16.
 */
public class Lection2 extends Fragment {
    private Button btn;
    private EditText edt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_lection2,container,false);
        btn = (Button)result.findViewById(R.id.btn2);
        edt = (EditText)result.findViewById(R.id.edt1);

        //Загрузим строку из настроек
        final SharedPreferences sPref = result.getContext().getSharedPreferences("gimmick", Context.MODE_PRIVATE);
        //функция SharedPreferences.getString() Первый параметр - ключ, второй - значение по умолчанию
        String btnCaption = sPref.getString("btnCaption","Очередная бесполезная кнопка");
        edt.setText(btnCaption);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Сохраним введённый текст
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("btnCaption",edt.getText().toString());
                ed.commit();
            //Сделаем немного активности и передадим в неё текст
                Intent intent = new Intent(v.getContext(),Lection2Activity.class);
                intent.putExtra("button_caption",edt.getText().toString());
                startActivity(intent);

            }
        });

        return result;
    }
}
