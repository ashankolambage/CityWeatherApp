package com.example.cityweatherapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ReversGeoLocationApiService {
    @GET("reverse")
    Call<List<ReversGeoResponse>> getReversGeoLocation(@Query("lat") double lat, @Query("lon") double lon, @Query("limit") int limit, @Query("appid") String apiKey);

    class RetrofitClient {
        private static final String BASE_URL = "http://api.openweathermap.org/geo/1.0/";
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

        public static ReversGeoLocationApiService getApiService() {
            return getRetrofitInstance().create(ReversGeoLocationApiService.class);
        }
    }
}
