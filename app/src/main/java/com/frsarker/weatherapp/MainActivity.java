package com.frsarker.weatherapp;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.frsarker.weatherapp.WeatherResponse;
import com.frsarker.weatherapp.WeatherApiService;
import com.frsarker.weatherapp.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


// The Main Screen - Makes the API call and displays weather data on the UI
public class MainActivity extends AppCompatActivity {

    private TextView cityNameTextView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView windTextView;
    private TextView descriptionTextView;
    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String API_KEY = "663484fe3560c9ab323dce14009b0db5";

    // Declare EditText and Button
    private EditText searchCityEditText;
    private Button searchButton;


    String CITY = "dhaka,bdtest";
    String API = "8118ed6ee68db2debfaaa5a44c832918";

    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // This loads UI

        // Initialize your EditText and Button views...
        searchCityEditText = findViewById(R.id.searchCity);
        searchButton = findViewById(R.id.searchButton);

        // Bind views from the layout...
        cityNameTextView = findViewById(R.id.cityName);
        temperatureTextView = findViewById(R.id.temp);
        humidityTextView = findViewById(R.id.humidity);
        windTextView = findViewById(R.id.wind);
        descriptionTextView = findViewById(R.id.textDescription);
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

        // new weatherTask(MainActivity.this, CITY, API).execute();

        // Default city on launch...
        // fetchWeatherData("Chicago");

        // Set up search...
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            Log.d("BUTTON", "CLICKED");
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
            // String city = searchCityEditText.getText().toString().trim();
            // if (!city.isEmpty()) {
                //Debugging Toast - Confirm if the click is being registered & city is being passed...
                //Toast.makeText(this, "Searching for: " + city, Toast.LENGTH_SHORT).show();
                //fetchWeatherData(city);
            //}
        });
    }


    private void fetchWeatherData(String cityName) {
        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);

        Call<WeatherResponse> call = apiService.getCurrentWeather(cityName, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // Update the UI here...
                    WeatherResponse weather = response.body();

                    // Logs for Debugging...
                    Log.d("WEATHER_RESPONSE", "City: " + weather.getCityName());
                    Log.d("WEATHER_RESPONSE", "Temp: " + weather.getMain().getTemp());

                    // Extract data from the response...
                    String address = weather.getCityName() + "," + weather.getSys().getCountry();
                    String updatedAt = "Updated just now";

                    String temp = String.format(Locale.getDefault(), "%.1f°C", weather.getMain().getTemp());
                    String tempMin = "Min Temperature: " + String.format(Locale.getDefault(), "%.1f°C", weather.
                            getMain().getTempMin());
                    String tempMax = "Max Temperature: " + String.format(Locale.getDefault(), "%.1f°C", weather.
                            getMain().getTempMax());

                    String wind = String.format(Locale.getDefault(), "%.1f m/s", weather.getWind().getSpeed());
                    String pressure = weather.getMain().getPressure() + " hPa";
                    String humidity = weather.getMain().getHumidity() + "%";

                    String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.
                            getSys().getSunrise() * 1000));
                    String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(weather.
                            getSys().getSunset() * 1000));

                    String weatherDescription = weather.getWeather().get(0).getDescription();

                    // Update the UI
                    updateWeatherUI(address, updatedAt, weatherDescription, temp, tempMin, tempMax, sunrise, sunset,
                            wind, pressure, humidity);
                } else {
                    Toast.makeText(MainActivity.this, "City not found!", Toast.LENGTH_SHORT).show();

                    /** Not Yet sure If I will use this yet...
                    // cityNameTextView.setText(weather.getCityName());
                    // temperatureTextView.setText("Temperature: " + weather.getMain().getTemp() + "°C"); **/
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    public void updateWeatherUI(String address, String updatedAt, String weatherDescription, String temp, String tempMin,
                                String tempMax, String sunrise, String sunset, String wind, String pressure, String
                                        humidity) {
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
    }
}
