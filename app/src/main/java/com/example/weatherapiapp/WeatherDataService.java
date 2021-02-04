package com.example.weatherapiapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService
{

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

    String cityName;
    Context context;

    String cityID;



//    public List<WeatherReportModel> getCityForecastByName(String cityName)
//    {
//
//    }

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener
    {
        void onError(String message);
        void onResponse(String cityID);
    }

    public void  getCityID(String cityName, VolleyResponseListener volleyResponseListener) {
        String url = WeatherDataService.QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>()
        {


            @Override
            public void onResponse(JSONArray response)
            {
                cityID = "";
                try {
                    JSONObject cityinfo = response.getJSONObject(0);
                    cityID = cityinfo.getString("woeid");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


               // Toast.makeText(context, "CityID=" + cityID, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something Wrong");
            }
        });

        MySingleton.getInstance(context).addToRequestQueue(request);


    }

    public interface ForecastByIDResponse
    {
        void onError(String message);
        void onResponse(List<WeatheReportModel> weatheReportModels);
    }

    public void getCityForecastByID(String cityID,ForecastByIDResponse forecastByIDResponse)
    {
        List<WeatheReportModel> weatheReportModels = new ArrayList<>();

        String url=QUERY_FOR_CITY_WEATHER_BY_ID+cityID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                try
                {
                    JSONArray consolidated_weather_list=response.getJSONArray("consolidated_weather");



                    for (int i=0;i<consolidated_weather_list.length();i++)
                    {

                        WeatheReportModel one_day=new WeatheReportModel();
                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                        one_day.setId(first_day_from_api.getInt("id"));
                        one_day.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        one_day.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        one_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        one_day.setCreated(first_day_from_api.getString("created"));
                        one_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        one_day.setMin_temp(first_day_from_api.getLong("min_temp"));
                        one_day.setMax_temp(first_day_from_api.getInt("max_temp"));
                        one_day.setThe_temp(first_day_from_api.getLong("the_temp"));
                        one_day.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        one_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        one_day.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        one_day.setHumidity(first_day_from_api.getInt("humidity"));
                        one_day.setVisibility(first_day_from_api.getLong("visibility"));
                        one_day.setPredictability(first_day_from_api.getInt("predictability"));
                        weatheReportModels.add(one_day);
                    }
                    forecastByIDResponse.onResponse(weatheReportModels);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }

        });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }


    public interface GetCityForecastByNameCallBack
    {
        void onError(String message);
        void onResponse(List<WeatheReportModel> weatheReportModels);
    }

    public void getCityForecastByName(String cityName,GetCityForecastByNameCallBack getCityForecastByNameCallBack)
    {
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message)
            {

            }

            @Override
            public void onResponse(String cityID)
            {
                getCityForecastByID(cityID, new ForecastByIDResponse() {
                    @Override
                    public void onError(String message)
                    {


                    }

                    @Override
                    public void onResponse(List<WeatheReportModel> weatheReportModels)
                    {
//we have the weather report
                        getCityForecastByNameCallBack.onResponse(weatheReportModels);

                    }
                });

            }
        });

    }

}

