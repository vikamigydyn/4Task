package com.example.lab4;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private TextView condition;
    private EditText cityInput;
    private Button updateButton;

    private final String apiKey = "239941f4a6124fdf99a101215242108";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        temp = findViewById(R.id.temp);
        condition = findViewById(R.id.condition);
        cityInput = findViewById(R.id.cityInput);
        updateButton = findViewById(R.id.updateButton);

        updateButton.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeatherData(city);
            } else {
                cityName.setText("Введите название города");
            }
        });
    }

    private void fetchWeatherData(String city) {
        String url = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + city + "&aqi=no";
        var queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject location = response.getJSONObject("location");
                        JSONObject current = response.getJSONObject("current");
                        JSONObject conditionObj = current.getJSONObject("condition");

                        cityName.setText(location.getString("name"));
                        temp.setText(current.getString("temp_c") + "°C");
                        condition.setText(conditionObj.getString("text"));
                    } catch (JSONException e) {
                        Log.e("WeatherApp", "JSON Parsing error: " + e.getMessage());
                        cityName.setText("Error parsing data!");
                    }
                },
                error -> {
                    Log.e("WeatherApp", "Error: " + error.getMessage());
                    cityName.setText("Error fetching data! Error: " + error.getMessage());
                }
        );

        queue.add(jsonObjectRequest);
    }
}