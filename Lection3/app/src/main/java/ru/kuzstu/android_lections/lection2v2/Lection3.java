package ru.kuzstu.android_lections.lection2v2;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by bug on 25.09.16.
 */
//Помимо того что этот класс наследуется от фрагмента, в нём мы ещё и реализуем обработчики событий сенсоров и gps
public class Lection3 extends Fragment implements SensorEventListener, LocationListener
{
    //Код нашего приложения чтобы сверить результаты. Почему-то только младший байт, никаких 0xDEADFACE'ов, сын.
    final public static int PERMISSION_REQUEST_CODE = 0x00AD;

    private TextView tvSensors;
    private TextView tvInternet;

    private Button btnInternet;
    private Button btnPicture;

    private ImageView ivPicture;

    //Массивы чтобы хранить показания датчиков
    private double[] accel_data;
    private double[] gps_data;

    //Сами датчики
    private Sensor accelerometer;

    //Менаджеры
    SensorManager sensManager;
    LocationManager locManager;

    //Строка в которую мы всё запихуиваем
    private String sensorsFormat = "Accel = {%.4f %.4f %.4f}\n GPS = {%.4f %.4f}";



    public void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.INTERNET,
                },
                PERMISSION_REQUEST_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_lection3,container,false);

        tvSensors = (TextView)result.findViewById(R.id.tvSensors);
        tvInternet = (TextView)result.findViewById(R.id.tvInternet);
        btnInternet = (Button)result.findViewById(R.id.btnInternet);
        btnPicture = (Button) result.findViewById(R.id.btnPhoto);
        ivPicture = (ImageView) result.findViewById(R.id.ivPhoto);

        requestMultiplePermissions();
        //Запрос разрешений выполняется асинхронно, поэтому лучше убедиться что все необходимые разрешения получены
        if(

                ActivityCompat.checkSelfPermission(result.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(result.getContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(result.getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(result.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(result.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(result.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ){
            barrelRoll();
        }

        return result;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Проверяем сколько разрешений получили
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 6) {
            barrelRoll();
        }else {
            Toast.makeText(this.getContext(),"Ну вот чё ты?",Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Если разрешения получили, начинаем творить беззаконие
    public void barrelRoll(){
        Log.e("LEC3","Знаешь, откуда эти шрамы?");
        gps_data = new double[2];
        accel_data = new double[3];
        //Настраиваем менеджеры сенсоров и локации
        sensManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        locManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);


        //Инициализируем сами сенсоры
        accelerometer = sensManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //Данные с gps биндятся чуть иначе
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, this);

        btnInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadStuff task = new DownloadStuff();
                task.execute();
            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAShot();
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        gps_data[0] = location.getLatitude();
        gps_data[1] = location.getLongitude();
        refreshSensorsText();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accel_data[0] = event.values[0];
        accel_data[1] = event.values[1];
        accel_data[2] = event.values[2];
        refreshSensorsText();
    }

    public void refreshSensorsText(){
        if(gps_data == null || accel_data==null || tvSensors == null){
            Log.e("LEC3", "что-то сильно не так");
            return;
        }
        tvSensors.setText(String.format(sensorsFormat, accel_data[0], accel_data[1], accel_data[2],
                gps_data[0], gps_data[1]));
    }
    //чистим за собой
    @Override
    public void onStop() {
        if(locManager != null){
            locManager.removeUpdates(this);
        }
        if(sensManager != null && accelerometer != null){
            sensManager.unregisterListener(this, accelerometer);
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(locManager != null){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, this);
        }
        if(sensManager != null && accelerometer != null){
            sensManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //Код в активити и обработчиках кнопок выполняется в том же потоке что и отрисовка интерфейса,
    //поэтому, чтобы интерфейс не подвисал на продолжительных задачах их нужно выносить в AsyncTask
    //Также из AsyncTask можно отображать прогресс
    private class DownloadStuff extends AsyncTask<Object,Object,String>{

        //Этот метод будет исполняться в фоне
        @Override
        protected String doInBackground(Object... params) {
            String jsonText = "";
            String result = "";
            //Используемые далее методы любят иногда подкинуть эксепшенов. Такие вещи нужно обрабатывать.
            try {
                //Отображаем прогресс. Передаваемвый объект может быть любого типа, подробнее - в документации к AsyncTask
                this.publishProgress("Подключение...");
                //Этот сайт генерирует случайные пользовательские данные, по этому урл функция
                //возвращает обычный json объект
                URL url = new URL("https://randomuser.me/api/");
                //подключаемся
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                //готовимся читать строки
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuffer buff = new StringBuffer();

                String line;
                this.publishProgress("Чтение данных...");
                while ((line = reader.readLine()) != null) {
                    buff.append(line);
                }
                jsonText = buff.toString();
                connection.disconnect();
            } catch(Exception e){
                Log.e("LECT3",e.getMessage());
                this.publishProgress("Не удалось подключиться.");
            }

            try{
                //Возвращаемый объект хранит в себе массив сгенерированных пользователей + некоторые метаданные
                //здесь я просто беру первый эелемент этого массива и достаю из него поле "email"
                this.publishProgress("Разбор объекта...");
                result = new JSONObject(jsonText).getJSONArray("results").getJSONObject(0).getString("email");
            } catch (Exception e){
                Log.e("LECT3", e.getMessage());
                this.publishProgress("Не удалось распарсить объект.");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvInternet.setText(s);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            if(values != null && values.length == 1){
                tvInternet.setText((String)values[0]);
            }
            super.onProgressUpdate(values);
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 0xFE;
    //Здесь мы знакомым уже способом вызовем что-нибудь, что сделает нам фотографию
    private void takeAShot() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    //А здесь - обработаем результаты запущенной ранее активити камеры
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPicture.setImageBitmap(imageBitmap);
        }
    }

    //Эти методы обрабатывают прочие события датчиков и локации(изменение точности, смена источника координат итд.), не будем их трогать
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
