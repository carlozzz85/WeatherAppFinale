package com.cst2335.weatherapp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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

import com.cst2335.WeatherApp.R;
import com.cst2335.weatherapp.*;

public class MainActivity extends AppCompatActivity {
    private TextView weatherTextView;
    private Toolbar toolbar;

    private String api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.textView_weather);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

            api = "http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=0c75f7d45db81f81013b6071385d59d4";
        //api = "api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=0c75f7d45db81f81013b6071385d59d4";

        // Assuming you have a button to refresh the weather
        // You may need to modify the FetchWeatherTask call based on the option selected
        findViewById(R.id.button_refresh).setOnClickListener(view ->
                new FetchWeatherTask().execute(api));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.three_day_forecast) {
            api = "https://pro.openweathermap.org/data/2.5/forecast/hourly?lat=44.34&lon=10.99&appid=524901&appid=0c75f7d45db81f81013b6071385d59d4";
            Toast.makeText(this, "4-Day Forecast", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.five_day_forecast) {
            api = "https://pro.openweathermap.org/data/2.5/forecast/climate?lat=35&lon=139&appid=524901&appid=0c75f7d45db81f81013b6071385d59d4";
            Toast.makeText(this, "30-Day Forecast", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.developer_info) {

            Toast.makeText(this, "Developed By Carlos Herrera", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
