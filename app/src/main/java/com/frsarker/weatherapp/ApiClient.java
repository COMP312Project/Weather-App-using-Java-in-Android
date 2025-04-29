package com.frsarker.weatherapp;

// Import necessary Retrofit and networking libraries...
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;



/**
 * ApiClient is responsible for creating and providing a Retrofit instance configured to communicate
 * with the Weather API.  It also sets up an OkHttpClient with logging for debugging purposes.
 */
public class ApiClient {

    // Singleton Retrofit instance...
    private static Retrofit retrofit = null;

    /**
     * Returns a singleton Retrofit instance configured with:
     *    - Base URL for the weather API
     *    - JSON converter (Gson)
     *    - HTTP client with logging enabled
     *
     * @return Retro fit instance for making API calls
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Logging interceptor: prints HTTP request and response info to Logcat...
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Add the logging interceptor to the HTTP client...
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // Build and configure the Retrofit instance...
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")  // Base URL of the weather APP
                    .addConverterFactory(GsonConverterFactory.create())   // Auto-converts JSON responses
                    .client(client)                                       // Use custom OkHttpClient with logging
                    .build();
        }
        return retrofit;
    }
}