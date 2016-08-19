package com.example.romy.gpsfinal;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

//  https://github.com/elhackaton/Equipo4/blob/master/p/src/com/example/p/MainActivity.java

/**
 * Created by romy on 18/08/16.
 */
public class Ubicacion implements LocationListener {
    private Context ctx;
    private String Latitud;
    private String Longitud;
    LocationManager locationManager;
    String proveedor;
    private boolean networkOn;

    public Ubicacion (Context ctx){

        this.ctx = ctx;
        locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
        proveedor = LocationManager.NETWORK_PROVIDER;
        networkOn = locationManager.isProviderEnabled(proveedor);
        locationManager.requestLocationUpdates(proveedor,1000,1,this);

        System.out.println("CONTEX1");
        //locationManager.requestLocationUpdates(proveedor,1000,1,this);
        getLocation();

    }
    void getLocation(){

        System.out.println("PREGUNTo");
        if(networkOn){

            System.out.println("SI HAY RED");
            System.out.println("getLocation");
            Location lc = locationManager.getLastKnownLocation(proveedor);
            if(lc != null){
                StringBuilder builder = new StringBuilder();
                builder.append("Latitud: " ).append(lc.getLatitude());
                builder.append("Longitud").append((lc.getLongitude()));
                Latitud = String.valueOf(lc.getLatitude());
                Longitud = String.valueOf(lc.getLongitude());
                Toast.makeText(ctx,builder.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getStringLocation(String x){
        if(x=="Latitud")
            return Latitud;
        if(x=="Longitud")
            return Longitud;
        else
            return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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