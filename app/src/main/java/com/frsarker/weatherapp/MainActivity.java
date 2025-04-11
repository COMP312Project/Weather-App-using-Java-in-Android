package com.frsarker.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


// The Main Screen - Makes the API call and displays weather data on the UI
public class MainActivity extends AppCompatActivity {

    private TextView cityNameTextView;
    private TextView temperatureTextView;
    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String API_KEY = "YOUR_API_KEY";

    // Declare EditText and Button
    private EditText searchCityEditText;
    private Button searchButton;


    String CITY = "dhaka,bd";
    String API = "8118ed6ee68db2debfaaa5a44c832918";

    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;

    <EditText
            android:id="id/searchCity"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:hint="Enter city name"
            android:inputType="text"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toStartOf="@+id/searchButton"
            android:layout_marginEnd="8dp"/>

    <Button
            android:id="@+id/searchButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Search"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintEnd_toEndOf="parent"/>



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // This loads UI

        // Step #1 - Bind views from the layout
        cityNameTextView = findViewById(R.id.cityName);
        temperatureTextView = findViewById(R.id.temperature);
        humidityTextView = findViewById(R.id.textHumidity);
        windTextView = findViewById(R.id.textWind);
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

        new weatherTask().execute();

        // Default city on launch...
        fetchWeatherData("Chicago");

        // Set up search...
        searchButton.setOnClickListener(v ->) {
            String city = searchCityEditText.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeatherData(city);
            }
        });
    }


    @Override
    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            WeatherResponse weather = response.body();

            String temp = String.format(Locale.getDefault(), "%.1f°C", weather.main.temp - 273.15); // Kelvin to Celsisus
            String humidity = "Humidity: " + weather.main.humidity + "%";
            String windSpeed = "Wind: " + weather.wind.speed + " m/s";
            String description = weather.weather.get(0).description;

            // TextViews...
            temperatureTextView.setText(temp);
            humidityTextView.setText(humidity);
            windTextView.setText(windSpeed);
            descriptionTextView.setText(description);
            cityNameTextView.setText(weather.cityName);
        } else {
            Toast.makeText(this, "Weather data not available", Toast.LENGTH_SHORT).show();
        }
    }



        //Enter #6 - fetchWeatherData (07APR25)
        private void fetchWeatherData(String cityName) {
            WeatherApiService apiService = ApiClient.getClient().create(WeatherApiService.class);

            Call<WeatherResponse> = call = apiService.getCurrent Weather(cityName, API_KEY, "metric");

            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Update the UI here...
                        cityNameTextView.setText(weather.getCityName());
                        temperatureTextView.setText("Temperature: " + weather.getMain().getTemp() +  + "°C");
                    }
                }
                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }






    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
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
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                /* Populating extracted data into our views */
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
        }
    }
}