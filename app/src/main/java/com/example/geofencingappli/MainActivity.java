package com.example.geofencingappli;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.geofencingappli.services.AndroidGeofencing;
import com.example.geofencingappli.services.AndroidLocation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        AndroidLocation location = new AndroidLocation( this,"hw9SDkH0HZTa6w_F9SNIrw@eu" );

        AndroidGeofencing geofencing = new AndroidGeofencing( this,"hw9SDkH0HZTa6w_F9SNIrw@eu" );
    }
}
