package com.frsarker.weatherapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


// This class sets up and provides the Retrofit client instance with logging...
public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Logging interceptor: prints HTTP request and response info to Logcat...
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Add the logging interceptor to the HTTP client...
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // Build the Retrofit instance...
            retrofit = new Retrofit.Builder()
                    // Base URL of the API
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    // Auto-converts JSON to Java objects
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}