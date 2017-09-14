package com.example.samyuktha.mapsprac;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.location.LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    List<Datafromhere> doll;
    GoogleMap map;
    MapFragment frag;
    boolean opennow;
    Double rating;
    LocationManager lm;
    private double latitude, longitude;
    String here;
    RecyclerView rv;
    SearchView sh;
    FloatingActionButton fab1,fab2,fab3;
    boolean isOpen = false;
    FragmentManager fm;
    FragmentTransaction ft,fo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sams);



        fab1=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab2=(FloatingActionButton)findViewById(R.id.topfloatingActionButton);
        fab3=(FloatingActionButton)findViewById(R.id.leftfloatingActionButton);

        fm= getFragmentManager();
         ft =fm.beginTransaction();
        fo=fm.beginTransaction();

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BlankFragment bfmt = new BlankFragment();
                ft.remove(frag);
                ft.add(R.id.fmlay, bfmt,"replaces");
                ft.commit();


            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen)
                {
                    fab2.setVisibility(View.INVISIBLE);
                    fab3.setVisibility(View.INVISIBLE);
                    isOpen=false;
                }
                else {

                    fab2.setVisibility(View.VISIBLE);
                    fab3.setVisibility(View.VISIBLE);
                    isOpen=true;
                }
            }
        });






        frag = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        frag.getMapAsync(this);
        doll = new ArrayList<Datafromhere>();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
rv=(RecyclerView)findViewById(R.id.rec);
        LinearLayoutManager lms = new LinearLayoutManager(this);
        lms.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(lms);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, MainActivity.this);
        Location ls= lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        latitude=ls.getLatitude();
        longitude=ls.getLongitude();

        sh=(SearchView)findViewById(R.id.searchhere);
//        sh.getQuery();



      sh.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
                here=query;
              Toast.makeText(MainActivity.this, query,Toast.LENGTH_SHORT).show();
              map.clear();

              String url = getUrl(latitude, longitude,here);
              Object[] DataTransfer = new Object[2];
              DataTransfer[0] = map;
              DataTransfer[1] = url;
              Getnearby getNearbyPlacesData = new Getnearby();
              getNearbyPlacesData.execute(DataTransfer);
              return false;
          }

          @Override
          public boolean onQueryTextChange(String newText) {

              return false;
          }
      });

//        et=(EditText)findViewById(R.id.et1);
//        button1=(Button)findViewById(R.id.but1);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//            }
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Myrecycls adap=new Myrecycls(doll,map);
        rv.setAdapter(adap);
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder googlePlacesUrl = new
                StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");

        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&query="+ nearbyPlace);
        googlePlacesUrl.append("&key=" + "AIzaSyDnaTE-olVObMqhzNMJgqrp5rY7ZYpUNjU");

        return (googlePlacesUrl.toString());
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {


        this.map = googleMap;

        Toast.makeText(MainActivity.this, " "+latitude+" "+longitude,Toast.LENGTH_SHORT).show();
        map.addMarker(new
                MarkerOptions().position(new LatLng(latitude, longitude)).title("Tutorialspoint.com"));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {


                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getLayoutInflater().inflate(R.layout.infobubble, null);

                TextView name = (TextView) v.findViewById(R.id.text2);
                TextView lat = (TextView) v.findViewById(R.id.text4);
                TextView longs = (TextView) v.findViewById(R.id.text41);
                TextView  address = (TextView) v.findViewById(R.id.text6);


                LatLng ll =marker.getPosition();
                Double latso=ll.latitude;
                Double longso = ll.longitude;
                name.setText(marker.getTitle());
                lat.setText(latso.toString());
                longs.setText(longso.toString());
                address.setText(marker.getSnippet());


                return v;
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent ins = new Intent(MainActivity.this, Main2Activity.class);
                LatLng loss= marker.getPosition();
                Double l1= loss.latitude;
                Double l2= loss.longitude;
                ins.putExtra("h1",marker.getTitle());
                ins.putExtra("h2",l1.toString());
                ins.putExtra("h3",l2.toString());
                ins.putExtra("h4",marker.getSnippet());
                startActivity(ins);

            }
        });



    }


    public void addMarkercustom(Double lat1, Double lng1)
    {
        LatLng TutorialsPoint = new LatLng(lat1, lng1);
        map.addMarker(new
                MarkerOptions().position(TutorialsPoint).title("Tutorialspoint.com"));
        map.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        this.longitude= location.getLongitude();
       this.latitude= location.getLatitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public class Getnearby extends AsyncTask<Object,String ,String> {

        String googleplacesdata, url;
        GoogleMap mgooglemap;

        List<android.location.Address> la;
        MainActivity ma;
        @Override
        protected String doInBackground(Object... params) {


            try {
                mgooglemap = (GoogleMap) params[0];
                url = (String) params[1];
                Downloadurl downloadUrl = new Downloadurl();
                googleplacesdata = downloadUrl.readUrl(url);
            } catch (Exception e) {
                Log.d("GooglePlacesReadTask", e.toString());
            }
            Log.d("output", googleplacesdata);
            return googleplacesdata;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject reader = new JSONObject(s);
                JSONArray abc= reader.getJSONArray("results");
                Log.d("around1"," "+abc.length());
                for(int i=0;i<abc.length();i++)
                {
                    JSONObject objectinsidearray = abc.getJSONObject(i);

                    JSONObject geometryobject = objectinsidearray.getJSONObject("geometry");
                    JSONObject locationobj = geometryobject.getJSONObject("location");
                    Double latitudeval=locationobj.getDouble("lat");
                    Double Longitudeval = locationobj.getDouble("lng");
                    String icon = objectinsidearray.getString("icon");

                    String name = objectinsidearray.getString("name");

                    if(objectinsidearray.has("opening_hours")) {
                        JSONObject openobject = objectinsidearray.getJSONObject("opening_hours");
                         opennow=openobject.getBoolean("open_now");
                    }
                    else opennow=false;
                    if(objectinsidearray.has("rating")) {
                        rating = objectinsidearray.getDouble("rating");
                    }
                    else rating = 0.0;
                    String vicinity = objectinsidearray.getString("formatted_address");
                    Log.d("latitudes", " "+latitudeval);
                    doll.add(new Datafromhere(latitudeval,Longitudeval,icon,name,opennow,rating,vicinity));
                    Log.d("around2"," added"+i);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

Log.d("around"," "+doll.size());
            for (int i = 0; i < doll.size(); i++) {

                final Datafromhere dlm =doll.get(i);
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(dlm.getLatsdata(),dlm.getLngsdata());
                markerOptions.position(latLng);
                markerOptions.title(dlm.getName1());
                markerOptions.snippet(dlm.getVicinity1());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                map.addMarker(markerOptions);

                // move map camera
               map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(dlm.getLatsdata(),dlm.getLngsdata())));

            }

            Myrecycls adap=new Myrecycls(doll,map);
            rv.setAdapter(adap);

        }
    }
}
