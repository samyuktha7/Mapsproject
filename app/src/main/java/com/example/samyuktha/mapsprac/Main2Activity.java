package com.example.samyuktha.mapsprac;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity implements LocationListener {

    ImageView  img;
    TextView title,lats,longs,addresss;
    double dlat,dlon,alat,alon;
    Button b;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        img=(ImageView)findViewById(R.id.imageview);
        title=(TextView)findViewById(R.id.titletextview);
        lats=(TextView)findViewById(R.id.latText);
        longs=(TextView)findViewById(R.id.longtextview);
        addresss=(TextView)findViewById(R.id.addtextview);
        b=(Button) findViewById(R.id.googlebutton);






       Intent ink= getIntent();
        title.setText(ink.getStringExtra("h1"));
        lats.setText(ink.getStringExtra("h2"));
        longs.setText(ink.getStringExtra("h3"));
        addresss.setText(ink.getStringExtra("h4"));

        Picasso.with(this).load(ink.getStringExtra("h5")).into(img);
        dlat=Double.parseDouble(lats.getText().toString());
                dlon=Double.parseDouble(longs.getText().toString());


        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //to handle the error, we wrote this.
            return;
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,10, Main2Activity.this);
        final Location la= lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            alat=la.getLatitude();
        alon=la.getLongitude();


        Toast.makeText(Main2Activity.this, " "+dlat+" "+dlon,Toast.LENGTH_SHORT).show();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ins= new Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?saddr="+alat+","+alon+"&daddr="+dlat+","+dlon));
                startActivity(ins);
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        alat=location.getLatitude();
        alon=location.getLongitude();

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
}
