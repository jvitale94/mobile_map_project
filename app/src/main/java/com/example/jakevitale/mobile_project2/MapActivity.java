package com.example.jakevitale.mobile_project2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.jakevitale.mobile_project2.R.id.map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private float lat1;
    private float lon1;
    private float lat2;
    private float lon2;
    public Marker one;
    public Marker two;

    public boolean marker_one = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();
        float[] vals = bundle.getFloatArray("message");
        lat1 = vals[0];
        lon1 = vals[2];
        lat2 = vals[1];
        lon2 = vals[3];

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng marker1 = new LatLng(lat1, lon1);
        LatLng marker2 = new LatLng(lat2, lon2);
        Marker m1 = mMap.addMarker(new MarkerOptions().position(marker1).title("M1: " + lat1 + ", " + lon1));
        Marker m2 = mMap.addMarker(new MarkerOptions().position(marker2).title("M2: " + lat2 + ", " + lon2));
        one = m1;
        two = m2;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker1));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                if (marker_one) {
                    one.remove();
                    Marker m = mMap.addMarker(new MarkerOptions().position(point).title("M1: " + point.latitude + ", " + point.longitude));
                    one = m;
                } else {
                    two.remove();
                    Marker m = mMap.addMarker(new MarkerOptions().position(point).title("M2: " + point.latitude + ", " + point.longitude));
                    two = m;
                }
            }
        });
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
        String t = marker.getTitle();

        if (t.substring(0, 2).equals("M1")) {
            marker_one = true;
        } else {
            marker_one = false;
        }

        return true;
    }

    public void changeOne(View view) {

        EditText lat_edit = (EditText) findViewById(R.id.edit1);
        String lat = lat_edit.getText().toString();
        EditText lon_edit = (EditText) findViewById(R.id.edit2);
        String lon = lon_edit.getText().toString();

        float lat_num = Float.parseFloat(lat);
        float lon_num = Float.parseFloat(lon);

        LatLng marker = new LatLng(lat_num, lon_num);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

        one.remove();

        Marker m1 = mMap.addMarker(new MarkerOptions().position(marker).title("M1: " + lat_num + ", " + lon_num));
        one = m1;
    }

    public void changeTwo(View view) {
        EditText lat_edit = (EditText) findViewById(R.id.edit3);
        String lat = lat_edit.getText().toString();
        EditText lon_edit = (EditText) findViewById(R.id.edit4);
        String lon = lon_edit.getText().toString();

        float lat_num = Float.parseFloat(lat);
        float lon_num = Float.parseFloat(lon);

        LatLng marker = new LatLng(lat_num, lon_num);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

        two.remove();

        Marker m1 = mMap.addMarker(new MarkerOptions().position(marker).title("M2: " + lat_num + ", " + lon_num));
        two = m1;
    }

    public void drawPath(View view) {
        mMap.clear();

        LatLng l1 = one.getPosition();
        LatLng l2 = two.getPosition();
        Marker m1 = mMap.addMarker(new MarkerOptions().position(l1).title("M1: " + l1.latitude + ", " + l2.latitude));
        Marker m2 = mMap.addMarker(new MarkerOptions().position(l2).title("M2: " + l2.latitude + ", " + l2.latitude));
        one = m1;
        two = m2;

        String url = getMapsApiDirectionsUrl(one.getPosition(), two.getPosition());
        ReadTask downloadTask = new ReadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    //Below is code to set up drawing the path
    private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;

    }

    private class ReadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // TODO Auto-generated method stub
            String data = "";
            try {
                MapHttpConnection http = new MapHttpConnection();
                data = http.readUr(url[0]);


            } catch (Exception e) {
                // TODO: handle exception
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    public class MapHttpConnection {
        public String readUr(String mapsApiDirectionsUrl) throws IOException {
            String data = "";
            InputStream istream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(mapsApiDirectionsUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                istream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(istream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();


            } catch (Exception e) {
                System.out.println("Exception while reading url " + e.toString());
            } finally {
                istream.close();
                urlConnection.disconnect();
            }
            return data;

        }
    }

    public class PathJSONParser {

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            try {
                jRoutes = jObject.getJSONArray("routes");
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat",
                                        Double.toString(((LatLng) list.get(l)).latitude));
                                hm.put("lng",
                                        Double.toString(((LatLng) list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;

        }

        private List<LatLng> decodePoly(String encoded) {
            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            // TODO Auto-generated method stub
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.BLUE);
            }

            if (polyLineOptions == null) {
                Toast.makeText(MapActivity.this, "NOT VALID MARKERS TO MAKE A PATH",
                        Toast.LENGTH_SHORT).show();
            } else {
                mMap.addPolyline(polyLineOptions);
            }

        }
    }
}
