package com.example.samyuktha.mapsprac;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    ImageView  img;
    TextView title,lats,longs,addresss;
    Button b;

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

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ino= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps"));
                startActivity(ino);
            }
        });
    }
}
