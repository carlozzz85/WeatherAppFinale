package com.cst2335.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView weatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.textView_weather);
        Button refreshButton = findViewById(R.id.button_refresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchWeatherTask().execute("http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=0c75f7d45db81f81013b6071385d59d4");
            }
        });
    }
    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to fetch data";
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray listArray = jsonObject.getJSONArray("list");
                StringBuilder formattedResult = new StringBuilder();

                for (int i = 0; i < listArray.length(); i++) {
                    JSONObject listObject = listArray.getJSONObject(i);
                    long dateTimeInMillis = listObject.getLong("dt") * 1000;
                    Date date = new Date(dateTimeInMillis);
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a", Locale.getDefault());
                    String dateText = dateFormatter.format(date);

                    JSONObject main = listObject.getJSONObject("main");
                    double temp = main.getDouble("temp") - 273.15;
                    String temperature = String.format(Locale.getDefault(), "%.1f°C", temp);

                    JSONArray weatherArray = listObject.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);
                    String description = weather.getString("description");

                    formattedResult.append(dateText)
                            .append("\nWeather: ").append(description)
                            .append("\nTemperature: ").append(temperature)
                            .append("\n\n");
                }

                weatherTextView.setText(formattedResult.toString());
            } catch (Exception e) {
                e.printStackTrace();
                weatherTextView.setText("Failed to parse data");
            }
        }
    }
}
