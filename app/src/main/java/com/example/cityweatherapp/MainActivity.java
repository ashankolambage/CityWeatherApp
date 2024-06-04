package com.example.cityweatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText editTextCity;
    private Button buttonFetch;
    private TextView textViewLocation, textViewTime, textViewWeather;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String CITY_NAME = "cityName";
    private static final String API_KEY = "f9b694db75b777ad458fe9061a313244";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonFetch = findViewById(R.id.buttonFetch);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewTime = findViewById(R.id.textViewTime);
        textViewWeather = findViewById(R.id.textViewWeather);

        // Load last searched city
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String lastCity = sharedPreferences.getString(CITY_NAME, "");
        editTextCity.setText(lastCity);

        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = editTextCity.getText().toString().trim();
                fetchGeoLocation(cityName);

                // Save last searched city
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(CITY_NAME, cityName);
                editor.apply();
            }
        });

        // Display current time
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        textViewTime.setText("Time: " + currentTime);
    }

    private void fetchGeoLocation(String cityName) {
        String cityWithCountryCode = cityName + ",LK";

        GeoLocationApiService apiService = GeoLocationApiService.RetrofitClient.getApiService();
        Call<List<GeoResponse>> call = apiService.getGeoLocation(cityWithCountryCode, 1, API_KEY);

        Log.d("MainActivity", "GeoLocation API URL: " + call.request().url().toString());

        call.enqueue(new Callback<List<GeoResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GeoResponse>> call, @NonNull Response<List<GeoResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        double lat = jsonObject.getDouble("lat");
                        double lon = jsonObject.getDouble("lon");

                        fetchWeatherData(lat, lon);
                    } catch (JSONException e) {
                        Log.e("MainActivity", "Error parsing JSON", e);
                    }
                } else {
                    textViewWeather.setText("Error fetching geolocation data");
                    Log.e("MainActivity", "Error response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GeoResponse>> call, @NonNull Throwable t) {
                textViewWeather.setText("Network error: " + t.getMessage());
                Log.e("MainActivity", "Network error", t);
            }
        });
    }

    private void fetchWeatherData(double lat, double lon) {
        WeatherApiService apiService = WeatherApiService.RetrofitClient.getApiService();
        Call<WeatherResponse> call = apiService.getWeather(lat, lon, "hourly", API_KEY);

        Log.d("MainActivity", "Weather API URL: " + call.request().url().toString());
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();

                    WeatherResponse.Current currentWeather = weatherResponse.getCurrent();
                    double currentTemp = currentWeather.getTemp();
                    int currentHumidity = currentWeather.getHumidity();
                    String currentDescription = currentWeather.getWeather().get(0).getDescription();

                    String location = "Latitude: " + lat + ", Longitude: " + lon;
                    String currentWeatherStr = "Current Weather: " + currentDescription + ", Temperature: " + currentTemp + "K, Humidity: " + currentHumidity + "%";

                    textViewLocation.setText(location);
                    textViewWeather.setText(currentWeatherStr);
                } else {
                    textViewWeather.setText("Error fetching weather data");
                    Log.e("MainActivity", "Error response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                textViewWeather.setText("Network error: " + t.getMessage());
                Log.e("MainActivity", "Network error", t);
            }
        });
    }
}