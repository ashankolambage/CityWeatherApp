package com.example.cityweatherapp;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("onecall")
    Call<WeatherResponse> getWeather(@Query("lat") double lat, @Query("lon") double lon, @Query("exclude") String exclude, @Query("appid") String apiKey);

    class RetrofitClient {
        private static final String BASE_URL = "https://api.openweathermap.org/data/3.0/";
        private static Retrofit retrofit;

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }

        public static WeatherApiService getApiService() {
            return getRetrofitInstance().create(WeatherApiService.class);
        }
    }
}
