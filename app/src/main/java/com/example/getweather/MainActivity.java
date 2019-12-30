package com.example.getweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public class Weather extends AsyncTask<String,Void,String>{

        TextView name = findViewById(R.id.name);
        Button searchbutton = findViewById(R.id.search);
        TextView result = findViewById(R.id.result);

    public void search(View view) {


        final String CityName = name.getText().toString();

        String content;
        Weather weather = new Weather();
        try {
            content = weather.execute("https://openweathermap.org/data/2.5/weather?q="+CityName+"&appid=b6907d289e10d714a6e88b30761fae22").get();
            Log.i("content", content);
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");
            double visibility;

            Log.i("weatherdata", weatherData);

            JSONArray array = new JSONArray(weatherData);

            String main = "";
            String description = "";
            String temperature = "";

            for (int i=0; i<array.length(); i++) {
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature = mainPart.getString("temp");

            visibility = Double.parseDouble(jsonObject.getString("visibility"));
            int visibilityInKilometer = (int)visibility/1000;

               /* Log.i("main",main);
                Log.i("description",description); */

            String resultText = "Main = " +main+
                    "\nDescription = "+description+
                    "\nTemperature = "+temperature+
                    "\nVisibility = "+visibilityInKilometer+ " K";

            result.setText(resultText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... adress) {

        try {
            URL url = new URL(adress[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int data = isr.read();
            String content = "";
            char ch;
            while (data != -1){
                ch = (char) data;
                content = content + ch;
                data = isr.read();
            }
            return content;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

}
