package com.example.geofencingappli.services;

import android.location.Location;
import android.util.Log;

import com.example.geofencingappli.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polestar.naosdk.api.external.NAOERRORCODE;
import com.polestar.naosdk.api.external.NAOLocationHandle;
import com.polestar.naosdk.api.external.NAOLocationListener;
import com.polestar.naosdk.api.external.NAOSensorsListener;
import com.polestar.naosdk.api.external.NAOServiceHandle;
import com.polestar.naosdk.api.external.NAOSyncListener;
import com.polestar.naosdk.api.external.TNAOFIXSTATUS;

// NAOSyncListener fournit des evenements quand la synchronisation avec le serveur est faite
// NAOGeofencingListener délivre les changements de location, on l'appelle dans OnLocationChanged, ça retourne chaque seconde la position de l'utilisateur
//NAOSensorsListener demande l'activation du bluetooth si nécessaire

public class AndroidLocation extends NAOServiceHandle implements NAOSyncListener, NAOLocationListener, NAOSensorsListener {

    NAOLocationHandle _handle;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public AndroidLocation(MainActivity mainActivity, String apiKey) {
        _handle = new NAOLocationHandle(mainActivity, AndroidService.class, apiKey,this,this );
        if (_handle !=null)
        {
            _handle.synchronizeData(this);
            _handle.start();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d( "AndroidLocation","position : Altitude : " + location.getAltitude()+"/ Longitude : "+location.getLongitude()+"/ Latitude : "+location.getLatitude());
        myRef =myRef.getRef().child( "Position" ).child( "Altitude" ); myRef.setValue( location.getAltitude());
        myRef = myRef.getRef().child( "Position").child( "Longitude" ); myRef.setValue( location.getLongitude());
        myRef = myRef.getRef().child( "Position").child( "Latitude" ); myRef.setValue( location.getLatitude());

    }

    @Override
    public void onLocationStatusChanged(TNAOFIXSTATUS tnaofixstatus) {
        Log.d( "AndroidLocation","status location"+ tnaofixstatus);
    }

    @Override
    public void onEnterSite(String s) {
        Log.d( "AndroidLocation","onEnterSite"+s );
    }

    @Override
    public void onExitSite(String s) {
        Log.d( "AndroidLocation", "onExitSite"+s );
    }

    @Override
    public void onError(NAOERRORCODE naoerrorcode, String s) {
        Log.d( "naoerrorcode",s );
    }

    @Override
    public void requiresCompassCalibration() {
        Log.d( "AndroidLocation", "requires  compass calibration");
    }

    @Override
    public void requiresWifiOn() {
        Log.d( "AndroidLocation", "requires  wifi on");
    }

    @Override
    public void requiresBLEOn() {
        Log.d( "AndroidLocation", "requires  bluetooth on");
    }

    @Override
    public void requiresLocationOn() {

    }

    // retourne infos depuis le cloud à propos de la "positionning database" et du fichier json "geofence" /
    // envoie des événements quand l'utilisateur est dans la zone ou en dehors /
    // MAJ de la posiiton chaque seconde
    @Override
    public void onSynchronizationSuccess() {
        Log.d( "AndroidLocation", "synchro succes" );
        if (_handle != null)
        {
            _handle.start();
        }
    }

    @Override
    public void onSynchronizationFailure(NAOERRORCODE naoerrorcode, String s) {
        Log.d( "naoerrorcode", s );
    }
}
