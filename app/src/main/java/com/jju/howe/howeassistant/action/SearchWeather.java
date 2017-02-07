package com.jju.howe.howeassistant.action;


import com.jju.howe.howeassistant.activity.MainActivity;

public class SearchWeather {

    private String date, mCity = null, mSourceName = null, mWeatherDate = null, mWeather = null, mTempRange = null, mAirQuality = null, mWind = null, mHumidity = null, mWindLevel = null;
    private MainActivity mActivity = null;

    public SearchWeather(String date, String city, String sourceName, String weatherDate, String weather, String tempRange, String
            airQuality, String wind, String humidity, String windLevel, MainActivity activity) {
        this.date = date;
        mCity = city;
        mSourceName = sourceName;
        mWeatherDate = weatherDate;
        mWeather = weather;
        mTempRange = tempRange;
        mAirQuality = airQuality;
        mWind = wind;
        mHumidity = humidity;
        mWindLevel = windLevel;
        mActivity = activity;
    }

    public void start() {
        mActivity.speakAnswer( mCity +date +  "的天气情况为"+ mWeather + ",气温范围" + mTempRange+",风向以及风力情况为"+mWind+"。");
    }

}
