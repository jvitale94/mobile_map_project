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

        String lat_lon = lat+"#"+lon;

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("message", lat_lon);
        startActivity(intent);
    }
}
