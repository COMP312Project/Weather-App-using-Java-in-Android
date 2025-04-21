package com.frsarker.weatherapp;

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
    private final String API_KEY = "YOUR_API_KEY";

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

        // Step #1 - Bind views from the layout
        cityNameTextView = findViewById(R.id.cityName);
        temperatureTextView = findViewById(R.id.temp);
        humidityTextView = findViewById(R.id.humidity);
        windTextView = findViewById(R.id.wind);
        descriptionTextView = findViewById(R.id.textDescription);

        searchCityEditText = findViewById(R.id.searchCity);
        searchButton = findViewById(R.id.searchButton);

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

        new weatherTask(MainActivity.this, CITY, API).execute();

        // Default city on launch...
        fetchWeatherData("Chicago");

        // Set up search...
        searchButton.setOnClickListener(v -> {
            String city = searchCityEditText.getText().toString().trim();
            if (!city.isEmpty()) {
                //Debugging Toast - Confirm if the click is being registered & city is being passed...
                Toast.makeText(this, "Searching for: " + city, Toast.LENGTH_SHORT).show();
                fetchWeatherData(city);
            }
        });
    }


    //Enter #6 - fetchWeatherData (07APR25)
    private void fetchWeatherData(String cityName) {
        WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);

        Call<WeatherResponse> call = apiService.getCurrentWeather(cityName, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update the UI here...
                    WeatherResponse weather = response.body();
                    // Extract data from the response...
                    String address = weather.getCityName();
                    String updatedAt = "Updated just now";
                    String temp = String.format(Locale.getDefault(), "%.1f°C", weather.getMain().getTemp());
                    String tempMin = "Min Temp: N/A";
                    String tempMax = "Max Temp: N/A";
                    String wind = String.format(Locale.getDefault(), "%.1f m/s", weather.getWind().getSpeed());

                    String humidity = weather.getMain().getHumidity() + "%";
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


    public void updateWeatherUI(String address, String updatedAt, String description, String temp, String tempMin,
                                String tempMax, String sunrise, String sunset, String wind, String pressure, String
                                        humidity) {
        addressTxt.setText(address);
        updated_atTxt.setText(updatedAt);
        statusTxt.setText(description.toUpperCase());
        tempTxt.setText(temp);
        temp_minTxt.setText(tempMin);
        temp_maxTxt.setText(tempMax);
        sunriseTxt.setText(sunrise);
        sunsetTxt.setText(sunset);
        windTxt.setText(wind);
        pressureTxt.setText(pressure);
        humidityTxt.setText(humidity);
    }


    class weatherTask extends AsyncTask<String, Void, String> {
        private MainActivity activity;
        private String city;
        private String apiKey;

        public weatherTask(MainActivity activity, String city, String apiKey) {
            this.activity = activity;
            this.city = city;
            this.apiKey = apiKey;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            activity.findViewById(R.id.loader).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.mainContainer).setVisibility(View.GONE);
            activity.findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + apiKey);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");

                String sunriseFormatted = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000));
                String sunsetFormatted = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000));

                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");

                // Call UI update method()...
                activity.updateWeatherUI(address, updatedAtText, weatherDescription, temp, tempMin, tempMax,
                        sunriseFormatted, sunsetFormatted, windSpeed, pressure, humidity);

                /* Views populated, Hiding the loader, Showing the main design */
                activity.findViewById(R.id.loader).setVisibility(View.GONE);
                activity.findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                activity.findViewById(R.id.loader).setVisibility(View.GONE);
                activity.findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
        }
    }
}
