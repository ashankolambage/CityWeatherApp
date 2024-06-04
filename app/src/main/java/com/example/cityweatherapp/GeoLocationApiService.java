package com.example.cityweatherapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoLocationApiService {

    @GET("geo/1.0/direct")
    Call<List<GeoResponse>> getGeoLocation(@Query("q") String cityName, @Query("limit") int limit, @Query("appid") String apiKey);

    class RetrofitClient {
        private static final String BASE_URL = "http://api.openweathermap.org/";
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

        public static GeoLocationApiService getApiService() {
            return getRetrofitInstance().create(GeoLocationApiService.class);
        }
    }
}
