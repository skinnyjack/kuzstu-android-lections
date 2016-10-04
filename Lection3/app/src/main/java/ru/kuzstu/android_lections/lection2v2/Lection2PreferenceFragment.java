package ru.kuzstu.android_lections.lection2v2;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class Lection2PreferenceFragment extends Fragment  {

    private EditText etText;
    private Button btnSave, btnLoad;

    private SharedPreferences sPref;

    private final String SAVED_TEXT = "saved_text";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View result = inflater.inflate(R.layout.fragment_lection2_preference,container,false);
        etText = (EditText) result.findViewById(R.id.etText);

        btnSave = (Button)result.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPref = getActivity().getPreferences(0);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(SAVED_TEXT, etText.getText().toString());
                ed.commit();
                Toast.makeText(v.getContext(), "текст схоронен", Toast.LENGTH_SHORT).show();
                Log.e("LECTION2","Ты всё сломал.");
            }
        });

        btnLoad = (Button)result.findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPref = getActivity().getPreferences(0);
                String savedText = sPref.getString(SAVED_TEXT, "");
                etText.setText(savedText);
                Toast.makeText(v.getContext(), "текст загружен", Toast.LENGTH_SHORT).show();
                Log.e("LECTION2","Ты всё сломал.");
            }
        });

        return result;
    }

}
