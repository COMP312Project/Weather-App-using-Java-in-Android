package com.frsarker.weatherapp;

import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.frsarker.weatherapp.BuildConfig;
import com.frsarker.weatherapp.WeatherResponse;
import com.frsarker.weatherapp.WeatherApiService;
import com.frsarker.weatherapp.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * MainActivity is the main screen of the Weather App
 *
 * It allows users to:
 *    - Search for a city
 *    - Fetch real-time weather data from OpenWeatherMap API
 *    - Display detailed weather information (temperature, humidity, etc.)
 *    - Dynamically change the background based on current weather conditions
 */
public class MainActivity extends AppCompatActivity {
    // API endpoint base URL for weather data
    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    // API key, stored securely in BuildConfig
    private final String API_KEY = BuildConfig.WEATHER_API_KEY;
    // UI elements for user input
    private EditText searchCityEditText;
    private Button searchButton;

    // UI elements for displaying weather data
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;


    /**
     * Initializes the app when activity is created.
     * Sets up UI components, binds views and fetches default city weather ("Chicago").
     *
     */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "onCreate: Started");      // Shows in Logcat
        setContentView(R.layout.activity_main);             // This loads UI...
        Log.d("DEBUG", "setContentView: End");    // Shows in Logcat

         // Initialize your EditText and Button views...
        searchCityEditText = findViewById(R.id.searchCity);
        searchButton = findViewById(R.id.searchButton);
        Log.d("DEBUG", "searchButton: End");      // Shows in Logcat

         // Bind views from the layout...
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        Log.d("DEBUG", "findViewById: End");     // Shows in Logcat

        // Set default city weather when app launches...
        fetchWeatherData("Chicago");

        // Set up search...
        searchButton = findViewById(R.id.searchButton);

        // Set up search button click listener...
        searchButton.setOnClickListener(v -> {
            Log.d("BUTTON", "CLICKED");      // Shows in Logcat
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();  // Shows in GUI

            String city = searchCityEditText.getText().toString().trim();
                if (!city.isEmpty()) {
                    //Debugging Toast - Confirm if the click is being registered & city is being passed...
                    Toast.makeText(this, "Search for: " + city, Toast.LENGTH_SHORT).show();   // Shows in GUI
                    fetchWeatherData(city);
                }
            });
        }


    /**
     * Makes an asynchronous API call to fetch current weather data for the specified city using Retrofit.
     *
     * @param cityName Name of the city to retrieve weather information for...
     */
    private void fetchWeatherData(String cityName) {
            WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);
            // Logs for Debugging...
            Log.d("API_CHECK", "Using API key: " + API_KEY);    // Shows in Logcat
            Log.d("API_CHECK", "City: " + cityName);            // Shows in Logcat
            Call<WeatherResponse> call = apiService.getCurrentWeather(cityName, API_KEY, "metric");

            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                     if (response.isSuccessful() && response.body() != null) {

                         // Update the UI here...
                        WeatherResponse weather = response.body();

                        // Logs for Debugging...
                        Log.d("WEATHER_RESPONSE", "City: " + weather.getCityName());          // Shows in Logcat
                        Log.d("WEATHER_RESPONSE", "Temp: " + weather.getMain().getTemp());    // Shows in Logcat
                        Log.d("WEATHER_RESPONSE", "Min: " + weather.getMain().getTempMin());  // Shows in Logcat
                        Log.d("WEATHER_RESPONSE", "Max: " + weather.getMain().getTempMax());  // Shows in Logcat

                        // Extract data from the response...
                        String address = weather.getCityName() + "," + weather.getSys().getCountry();
                        String updatedAt = "Updated just now";

                        // Convert temperatures from Celsius to Fahrenheit...
                         float tempCelsius = weather.getMain().getTemp();
                         float tempMinCelsius = weather.getMain().getTempMin();
                         float tempMaxCelsius = weather.getMain().getTempMax();

                         float tempFahrenheit = (tempCelsius * 9 / 5) + 32;
                         float tempMinFahrenheit = (tempMinCelsius * 9 / 5) - 32;
                         float tempMaxFahrenheit = (tempMaxCelsius * 9 / 5) + 32;

                        // Format and extract weather data for UI update...
                        String temp = String.format(Locale.getDefault(), "%.1f°F", tempFahrenheit);
                        String tempMin = "Min Temperature: " + String.format(Locale.getDefault(), "%.1f°F", tempMinFahrenheit);
                        String tempMax = "Max Temperature: " + String.format(Locale.getDefault(), "%.1f°F", tempMaxFahrenheit);
                        String wind = String.format(Locale.getDefault(), "%.1f m/s", weather.getWind().getSpeed());
                        String pressure = weather.getMain().getPressure() + " hPa";
                        String humidity = weather.getMain().getHumidity() + "%";

                        String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.
                            getSys().getSunrise() * 1000));
                        String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.
                            getSys().getSunset() * 1000));

                        String weatherDescription = weather.getWeather().get(0).getDescription();

                        Log.d("WEATHER_RESPONSE", "Calling updateWeatherUI...");        // Shows in Logcat
                        updateWeatherUI(address, updatedAt, weatherDescription, temp, tempMin, tempMax, sunrise,
                                sunset, wind, pressure, humidity);
                        } else {
                            Toast.makeText(MainActivity.this, "City not found!", Toast.LENGTH_SHORT).show();    // Shows in GUI
                    }
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    /**
     * Update the UI elements with fetched and formatted weather data.
     * Also triggers dynamic background color updates based on the weather condition.
     *
     * @param address address City and country
     * @param updatedAt Update timestamp
     * @param weatherDescription Weather description (clear, rain, cloudy, etc.)
     * @param temp Current temperature
     * @param tempMin Minimum temperature
     * @param tempMax Maximum temperature
     * @param sunrise Sunrise time
     * @param sunset Sunset time
     * @param wind Wind speed
     * @param pressure Atmospheric pressure
     * @param humidity Humidity percentage
     */
    public void updateWeatherUI(String address, String updatedAt, String weatherDescription, String temp, String tempMin,
                                String tempMax, String sunrise, String sunset, String wind, String pressure, String
                                        humidity) {
        // Debug - Make sure it is visible...
        findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
        // Debug log - Toast...
        Toast.makeText(this, "UI Updated!", Toast.LENGTH_SHORT).show();                  // Shows in GUI
        Log.d("WEATHER_UI", "Updating UI with fetched weather data");                       // Shows in Logcat
        Toast.makeText(this, "UI updated for: " + address, Toast.LENGTH_LONG).show();    // Shows in GUI
        Log.d("WEATHER_UI", "UI updated for: " + address);                                  // Shows in Logcat
        addressTxt.setText(address);
        updated_atTxt.setText(updatedAt);
        statusTxt.setText(weatherDescription.toUpperCase());
        tempTxt.setText(temp);
        temp_minTxt.setText(tempMin);
        temp_maxTxt.setText(tempMax);
        sunriseTxt.setText(sunrise);
        sunsetTxt.setText(sunset);
        windTxt.setText(wind);
        pressureTxt.setText(pressure);
        humidityTxt.setText(humidity);

        // This function will call the setDynamicBackground to update the color (GUI Background) of the current weather...
        setDynamicBackground(weatherDescription);
    }


    /**
     * Dynamically changes teh screen background color based on the current weather description.
     *
     * @param weatherDescription Current weather condition (e.g., clear, clouds, rain)
     */
    public void setDynamicBackground(String weatherDescription) {
        RelativeLayout mainContainer = findViewById(R.id.mainContainer);

        // Default text color...
        int textColor = getResources().getColor(android.R.color.black);                                  // Default to black text

        if (weatherDescription.contains("clear")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));  // Light Blue...
            Log.d("WEATHER_UI", "Changing background color to: Light Blue");
            textColor = getResources().getColor(android.R.color.white);
        } else if (weatherDescription.contains("clouds")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));      // Gray for Clouds...
            textColor = getResources().getColor(android.R.color.white);
        } else if (weatherDescription.contains("rain")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));   // Dark Blue for Rain...
            textColor = getResources().getColor(android.R.color.white);
        } else if (weatherDescription.contains("snow")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.white));            // White for Snow...
            textColor = getResources().getColor(android.R.color.black);
        } else if (weatherDescription.contains("thunderstorm")) {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.black));            // Dark for Storm...
            textColor = getResources().getColor(android.R.color.white);
        } else {
            // Change background and text color based on weather condition...
            mainContainer.setBackgroundColor(getResources().getColor(android.R.color.background_light)); // Default light...
            textColor = getResources().getColor(android.R.color.black);
        }

        // Applies text color to ALL important textViews...
        applyTextColor(textColor);

        // Debug log - Toast...
        Toast.makeText(this, "Changing background color...", Toast.LENGTH_SHORT).show();    // Shows in GUI
        Log.d("WEATHER_UI", "Changing background color...");                                   // Shows in Logcat
    }


    /**
     * Applies a specified text color to all weather-related Textviews.
     *
     * @param Color The color to apply to text elements.
     */
    private void applyTextColor(int Color) {
         addressTxt.setTextColor(Color);
         updated_atTxt.setTextColor(Color);
         statusTxt.setTextColor(Color);
         tempTxt.setTextColor(Color);
         temp_minTxt.setTextColor(Color);
         temp_maxTxt.setTextColor(Color);
         sunriseTxt.setTextColor(Color);
         sunsetTxt.setTextColor(Color);
         windTxt.setTextColor(Color);
         pressureTxt.setTextColor(Color);
         humidityTxt.setTextColor(Color);
    }
}

