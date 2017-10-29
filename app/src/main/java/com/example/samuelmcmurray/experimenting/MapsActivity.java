package com.example.samuelmcmurray.experimenting;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<String> restaurants;
    private String choice;
    private int count;
    private TextView tv;
    private TextView tv1;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_map)));


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(35.7796, -78.6382);
        //TextView newText = (TextView) findViewById(R.id.blurb);
       //newText.setText("Click The play buttong to begin. For directions click the map marker");
        Marker m = mMap.addMarker(new MarkerOptions().position(sydney).title("Click the buttons to explore"));
        //"The Green Button allows you to choose a filter.\nThe Blue Button lets you search for a restaurant directly\nThe Red Button picks a new Restaurant for you. "
        m.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,13.0f));
        tv = (TextView)findViewById(R.id.title);
        tv1 = (TextView)findViewById(R.id.blurb);
        setFood("food");
    }

    /**
     * This method creates an ArrayList of specific food options
     * This method is called immediately upon creation, and when a new filter is selected
     * @param fileName A string of the file to open
     */
    public void setFood(String fileName){
        this.choice="";
        BufferedReader br = null;
        this.restaurants = new ArrayList<>();

        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            String line = br.readLine();
            while(line != null){
                restaurants.add(line);
                line = br.readLine();
                //this.count++;
            }
        } catch (IOException e1) {
            //nice
        } finally{
            if(br != null)
                try {
                    br.close();
                }catch(Exception e){
                    //nice
                }
        }
    }

    /**
     * This method reads the correctly formatted string from the file and gets GPS coordinates.
     * Once the coordiantes have been retrieved the map generates a new picture
     * @param view Map view
     */
    public void newPlace(View view) {

        mMap.clear();
        if(this.count == (this.restaurants.size()-1)) {
            choice ="";
        }

        int temp = getRandom();

        String details = this.restaurants.get(temp);
        Scanner ss = new Scanner(details);
        ss.useDelimiter(",");
        String marker; //until first comma
        marker = ss.next();
        double markLat = Double.parseDouble(ss.next()); //nextcomma
        double markLong =  Double.parseDouble(ss.next());
        String blurb = ss.next();


        tv.setText(marker);

        tv1.setText(blurb);

        LatLng ll = new LatLng(markLat,markLong);
        Marker mm = mMap.addMarker(new MarkerOptions().position(ll).title(marker));//.snippet(blurb));
        mm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //mm.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,14.5f));

    }

    /**
     *
     * @param view
     */
    public void settings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void filter(View view) {
        Intent intent = new Intent(this, Filter.class);
        startActivityForResult(intent,1);
    }

    /**
     *
     * @return
     */
    public int getRandom(){
        Random r;
        r = new Random();
        int temp = r.nextInt(this.restaurants.size());
        String holder = "-"+temp;
        boolean checker=true;
        while(checker){
            if(choice.contains(holder)){
                temp = r.nextInt(this.restaurants.size());
                holder = holder + "-"+temp;
            }
            else {
                this.count++;
                checker = false;
            }
        }
        return temp;
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                this.fileName = data.getStringExtra("edittextvalue");
                //tv.setText(this.fileName);
                //now I need to create new random list
                restaurants.clear();
                setFood(fileName);
            }
        }
    }
}
