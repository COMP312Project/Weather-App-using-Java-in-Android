package com.frsarker.weatherapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;


//The data model to map JSON responses from OpenWeatherMap...
public class WeatherResponse {
    @SerializedName("name")
    private String cityName;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("sys")
    private Sys sys;

    public Sys getSys() {
        return sys;
    }

    public class Sys {
        @SerializedName("sunrise")
        private long sunrise;

        @SerializedName("sunset")
        private long sunset;

        @SerializedName("country")
        private String country;

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public String getCountry() {
            return country;
        }
    }

    public String getCityName() {
        return cityName;
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }


    public class Main {
        @SerializedName("temp")
        private float temp;

        @SerializedName("feels_like")
        private float feelsLike;

        @SerializedName("humidity")
        private int humidity;

        @SerializedName("pressure")
        private float pressure;

        @SerializedName("temp_min")
        private float tempMin;

        @SerializedName("temp_max")
        private float tempMax;

        public float getTemp() {
            return temp;
        }
        public float getFeelsLike() {
            return feelsLike;
        }
        public int getHumidity() {
            return humidity;
        }
        public float getPressure() {
            return pressure;
        }
        public float getTempMin() {
            return tempMin;
        }
        public float getTempMax() {
            return tempMax;
        }
    }

    public class Weather {
        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        @SerializedName("icon")
        public String icon;

        private String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }
    }


    public class Wind {
        @SerializedName("speed")
        private float speed;

        public float getSpeed() {
            return speed;
        }
    }
}