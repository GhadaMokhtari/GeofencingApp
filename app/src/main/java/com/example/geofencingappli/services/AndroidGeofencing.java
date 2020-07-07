package com.example.geofencingappli.services;

import android.util.Log;

import com.example.geofencingappli.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.polestar.naosdk.api.external.NAOERRORCODE;
import com.polestar.naosdk.api.external.NAOGeofenceListener;
import com.polestar.naosdk.api.external.NAOGeofencingHandle;
import com.polestar.naosdk.api.external.NAOGeofencingListener;
import com.polestar.naosdk.api.external.NAOLocationHandle;
import com.polestar.naosdk.api.external.NAOSensorsListener;
import com.polestar.naosdk.api.external.NAOServiceHandle;
import com.polestar.naosdk.api.external.NAOSyncListener;
import com.polestar.naosdk.api.external.NaoAlert;

// NAOSyncListener fournit des evenements quand la synchronisation avec le serveur est faite
// NAOGeofencingListener délivre les changements de location, on l'appelle dans OnLocationChanges, ça retourne chaque seconde la position de l'utilisateur
//NAOSensorsListener demande l'activation du bluetooth si nécessaire
public class AndroidGeofencing extends NAOServiceHandle implements NAOSyncListener, NAOGeofenceListener, NAOGeofencingListener, NAOSensorsListener {

    NAOGeofencingHandle handle;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public AndroidGeofencing(MainActivity mainActivity, String apikey){
        handle = new NAOGeofencingHandle( mainActivity, AndroidService.class, apikey,this,this );
        if (handle != null){
            handle.synchronizeData( this );
            handle.start();
        }
    }

    @Override
    public void onEnterGeofence(int i, String zone) {
        Log.d("geofencingClient","onEnterGeofence"+ zone);
        myRef = myRef.getRef().child( "Geofencing" ).child( "Type" ); myRef.setValue( "Enter zone" );
        myRef = myRef.getRef().child( "Geofencing" ).child( "Zone" ); myRef.setValue(  zone );

    }

    @Override
    public void onExitGeofence(int i, String zone) {
        Log.d("AndroidGeofencing","onExitGeofence"+ zone);
        myRef = myRef.getRef().child( "Geofencing" ).child( "Type" ); myRef.setValue( "Exit zone" );
        myRef = myRef.getRef().child( "Geofencing" ).child( "Zone" ); myRef.setValue(  zone );
    }

    @Override
    public void onFireNaoAlert(NaoAlert naoAlert) {
        Log.d("AndroidGeofencing", "Alert"+naoAlert.getContent());
        Log.d("AndroidGeofencing", "Alert"+naoAlert.getName());
    }

    @Override
    public void onError(NAOERRORCODE naoerrorcode, String s) {

        Log.d( "geofencingError",s );
    }

    @Override
    public void requiresCompassCalibration() {
        Log.d( "AndroidGeofencing", "requires compass calibration");
    }

    @Override
    public void requiresWifiOn() {

        Log.d( "AndroidGeofencing", "requires wifi on");
    }

    @Override
    public void requiresBLEOn() {
        Log.d( "AndroidGeofencing", "requires bluetooth on");
    }

    @Override
    public void requiresLocationOn() {

    }

    @Override
    public void onSynchronizationSuccess() {
        if (handle != null)
        {
            handle.start();
        }
    }

    @Override
    public void onSynchronizationFailure(NAOERRORCODE naoerrorcode, String s) {
        Log.d( "naoerrorcode", s );
    }
}
