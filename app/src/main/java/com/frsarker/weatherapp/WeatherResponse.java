public class WeatherResponse {
    @SerializedName("name")
    private String cityName;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("wind")
    private Wind wind;


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

        public float getTemp() {
            return temp;
        }
        public float getFeelsLike() {
            return feelsLike;
        }
        public int getHumidity() {
            return humidity;
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