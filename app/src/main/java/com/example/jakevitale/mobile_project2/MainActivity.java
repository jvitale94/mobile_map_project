package com.example.jakevitale.mobile_project2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void moveToMap(View view)
    {
        EditText lat_input = (EditText) findViewById(R.id.lat_tv);
        String lat = lat_input.getText().toString();
        EditText lon_input = (EditText) findViewById(R.id.lon_tv);
        String lon = lon_input.getText().toString();

        EditText lat_input2 = (EditText) findViewById(R.id.lat_tv2);
        String lat2 = lat_input2.getText().toString();
        EditText lon_input2 = (EditText) findViewById(R.id.lon_tv2);
        String lon2 = lon_input2.getText().toString();


        float lat_1 = Float.parseFloat(lat);
        float lat_2 = Float.parseFloat(lat2);
        float lon_1 = Float.parseFloat(lon);
        float lon_2 = Float.parseFloat(lon2);

        float [] vals = {lat_1, lat_2, lon_1, lon_2};

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("message", vals);
        //intent.putExtra("message2", lat_lon2);
        startActivity(intent);
    }
}
