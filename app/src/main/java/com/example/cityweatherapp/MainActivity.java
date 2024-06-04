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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    private static final String TAG = "MainActivity";
    private EditText editTextCity;
    private Button buttonFetch, buttonGetCurrentLocation;
    private TextView textViewLocation, textViewTime, textViewWeather, textViewLocationName;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String CITY_NAME = "cityName";
    private static final String API_KEY = "f9b694db75b777ad458fe9061a313244";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        editTextCity = findViewById(R.id.editTextCity);
        buttonFetch = findViewById(R.id.buttonFetch);
        buttonGetCurrentLocation = findViewById(R.id.buttonGetCurrentLocation);
        textViewLocation = findViewById(R.id.textViewLocation);
        textViewLocationName = findViewById(R.id.textViewLocationName);
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
                textViewLocationName.setText( cityName + ", LK");
                fetchGeoLocation(cityName);

                // Save last searched city
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(CITY_NAME, cityName);
                editor.apply();
            }
        });

        buttonGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocationPermission()) {
                    getCurrentLocation();
                } else {
                    requestLocationPermission();
                }
            }
        });

        // Display current time
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        textViewTime.setText("Time: " + currentTime);
    }

    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                reversGeoLocation(latitude, longitude);
                                fetchWeatherData(latitude, longitude);
                            } else {
                                Toast.makeText(MainActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Permission not granted
            requestLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchGeoLocation(String cityName) {
        String cityWithCountryCode = cityName + ",LK";

        GeoLocationApiService apiService = GeoLocationApiService.RetrofitClient.getApiService();
        Call<List<GeoResponse>> call = apiService.getGeoLocation(cityWithCountryCode, 1, API_KEY);

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

    private void reversGeoLocation(double lat, double lon) {

        ReversGeoLocationApiService reversapiService = ReversGeoLocationApiService.RetrofitClient.getApiService();
        Call<List<ReversGeoResponse>> call = reversapiService.getReversGeoLocation(lat, lon, 1, API_KEY);

        call.enqueue(new Callback<List<ReversGeoResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReversGeoResponse>> call, @NonNull Response<List<ReversGeoResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String reverseLocation = jsonObject.getString("name") + "," + jsonObject.getString("country");
                        textViewLocationName.setText( reverseLocation);
                    } catch (JSONException e) {
                        Log.e("MainActivity", "Error parsing JSON", e);
                    }
                } else {
                    textViewWeather.setText("Error fetching geolocation data");
                    Log.e("MainActivity", "Error response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReversGeoResponse>> call, @NonNull Throwable t) {
                textViewWeather.setText("Network error: " + t.getMessage());
                Log.e("MainActivity", "Network error", t);
            }
        });
    }

    private void fetchWeatherData(double lat, double lon) {
        WeatherApiService apiService = WeatherApiService.RetrofitClient.getApiService();
        Call<WeatherResponse> call = apiService.getWeather(lat, lon, "hourly", API_KEY);
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