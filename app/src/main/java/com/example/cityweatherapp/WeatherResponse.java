package com.example.cityweatherapp;

import java.util.List;

public class WeatherResponse {
    private double lat;
    private double lon;
    private String timezone;
    private int timezone_offset;
    private Current current;
    private List<Minute> minutely;
    private List<Daily> daily;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public Current getCurrent() {
        return current;
    }

    public List<Minute> getMinutely() {
        return minutely;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public class Current {
        private long dt;
        private long sunrise;
        private long sunset;
        private double temp;
        private double feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double uvi;
        private int clouds;
        private int visibility;
        private double wind_speed;
        private int wind_deg;
        private double wind_gust;
        private List<Weather> weather;

        public long getDt() {
            return dt;
        }

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public double getTemp() {
            return temp;
        }

        public double getFeels_like() {
            return feels_like;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getDew_point() {
            return dew_point;
        }

        public double getUvi() {
            return uvi;
        }

        public int getClouds() {
            return clouds;
        }

        public int getVisibility() {
            return visibility;
        }

        public double getWind_speed() {
            return wind_speed;
        }

        public int getWind_deg() {
            return wind_deg;
        }

        public double getWind_gust() {
            return wind_gust;
        }

        public List<Weather> getWeather() {
            return weather;
        }
    }

    public class Minute {
        private long dt;
        private double precipitation;

        public long getDt() {
            return dt;
        }

        public double getPrecipitation() {
            return precipitation;
        }
    }

    public class Daily {
        private long dt;
        private long sunrise;
        private long sunset;
        private long moonrise;
        private long moonset;
        private double moon_phase;
        private String summary;
        private Temp temp;
        private FeelsLike feels_like;
        private int pressure;
        private int humidity;
        private double dew_point;
        private double wind_speed;
        private int wind_deg;
        private double wind_gust;
        private List<Weather> weather;
        private int clouds;
        private double pop;
        private double rain;
        private double uvi;

        public long getDt() {
            return dt;
        }

        public long getSunrise() {
            return sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public long getMoonrise() {
            return moonrise;
        }

        public long getMoonset() {
            return moonset;
        }

        public double getMoon_phase() {
            return moon_phase;
        }

        public String getSummary() {
            return summary;
        }

        public Temp getTemp() {
            return temp;
        }

        public FeelsLike getFeels_like() {
            return feels_like;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getDew_point() {
            return dew_point;
        }

        public double getWind_speed() {
            return wind_speed;
        }

        public int getWind_deg() {
            return wind_deg;
        }

        public double getWind_gust() {
            return wind_gust;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public int getClouds() {
            return clouds;
        }

        public double getPop() {
            return pop;
        }

        public double getRain() {
            return rain;
        }

        public double getUvi() {
            return uvi;
        }
    }

    public class Temp {
        private double day;
        private double min;
        private double max;
        private double night;
        private double eve;
        private double morn;

        public double getDay() {
            return day;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public double getNight() {
            return night;
        }

        public double getEve() {
            return eve;
        }

        public double getMorn() {
            return morn;
        }
    }

    public class FeelsLike {
        private double day;
        private double night;
        private double eve;
        private double morn;

        public double getDay() {
            return day;
        }

        public double getNight() {
            return night;
        }

        public double getEve() {
            return eve;
        }

        public double getMorn() {
            return morn;
        }
    }

    public class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
}
