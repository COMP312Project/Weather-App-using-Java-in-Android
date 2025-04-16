package com.frsarker.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.frsarker.weatherapp.WeatherResponse;

// Defines the Retrofit interface with endpoints (@GET methods) for fetching weather data
public interface WeatherApiService {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather (
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String units // e.g., "metric" or "imperial"
    );
}
