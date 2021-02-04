package com.example.weatherapiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity
{


    Button btn_cityID,btn_getweatherbyID,btn_getweatherbyname;
    EditText et_dataInput;
    ListView lv_weatherReports;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //assign values to each control on the layout

        btn_cityID=findViewById(R.id.btn_cityID);
        btn_getweatherbyID=findViewById(R.id.btn_getweatherbyID);
        btn_getweatherbyname=findViewById(R.id.btn_getweatherbyname);
        et_dataInput=findViewById(R.id.et_dataInput);
        lv_weatherReports=findViewById(R.id.lv_weatherReports);

        final WeatherDataService weatherDataService=new WeatherDataService(MainActivity.this);

        //click listeners for each button

        btn_cityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Instantiate the RequestQueue.

                //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


                weatherDataService.getCityID(et_dataInput.getText().toString(), new WeatherDataService.VolleyResponseListener()

                {
                    @Override
                    public void onError(String message)
                    {
                        Toast.makeText(MainActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityID)
                    {
                        Toast.makeText(MainActivity.this, "Returned an ID of"+ cityID, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });




        btn_getweatherbyID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(MainActivity.this, "This is b, Toast.LENGTH_SHORT).show();

              weatherDataService.getCityForecastByID(et_dataInput.getText().toString(), new WeatherDataService.ForecastByIDResponse() {
                  @Override
                  public void onError(String message) 
                  {
                      Toast.makeText(MainActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
                  }

                  @Override
                  public void onResponse(List<WeatheReportModel> weatheReportModels)
                  {
                      //put the entire list
                      //Toast.makeText(MainActivity.this, weatheReportModels.toString(), Toast.LENGTH_SHORT).show();

                      ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,weatheReportModels);
lv_weatherReports.setAdapter(arrayAdapter);

                      }
              });

            }
        });


        btn_getweatherbyname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                //Toast.makeText(MainActivity.this, "This is b, Toast.LENGTH_SHORT).show();

                weatherDataService.getCityForecastByName(et_dataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallBack() {
                    @Override
                    public void onError(String message)
                    {
                        Toast.makeText(MainActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatheReportModel> weatheReportModels)
                    {
                        //put the entire list
                        //Toast.makeText(MainActivity.this, weatheReportModels.toString(), Toast.LENGTH_SHORT).show();

                        ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,weatheReportModels);
                        lv_weatherReports.setAdapter(arrayAdapter);

                    }
                });





            }
        });








    }







}

