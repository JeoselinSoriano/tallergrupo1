package com.example.romy.gpsfinal;

//http://www.sgoliver.net/blog/localizacion-geografica-en-android-i/


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainLocalizacion extends Activity {


    // AppCompatActivity
    private Button btnActualizar;
    private Button btnDesactivar;
    private TextView lblLatitud;
    private TextView lblLongitud;
    private TextView lblPrecision;
    private TextView lblEstado;


    private LocationManager locManager;
    private LocationListener locListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);

            btnActualizar = (Button) findViewById(R.id.BtnActualizar);
            btnDesactivar = (Button) findViewById(R.id.BtnDesactivar);
            lblLatitud = (TextView) findViewById(R.id.LblPosLatitud);
            lblLongitud = (TextView) findViewById(R.id.LblPosLongitud);

            Time time = new Time();
            time.setToNow();
            System.out.println("time: " + time.hour+":"+time.minute);

            lblPrecision = (TextView) findViewById(R.id.LblPosPrecision);
            lblEstado = (TextView) findViewById(R.id.LblEstado);

            Ubicacion ub = new Ubicacion(this);
            //ub.getStringLocation("Latitud");
            lblLatitud.setText("Latitud: " +  ub.getStringLocation("Latitud"));

            btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    comenzarLocalizacion();
                }
            });


            btnDesactivar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locManager.removeUpdates(locListener);

                }
            });
        } catch (Exception e) {
            Log.e("ERROR", "Error: " + e);
        } finally {
            Log.i("INFO", "Salimos de onCreate");
        }
    }

    private void comenzarLocalizacion() {

        try {

            //Obtenemos una referencia al LocationManager
            locManager =
                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            //Obtenemos la última posición conocida
            Location loc =
                    locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (loc != null) {
                Log.i("location", String.valueOf(loc.getLongitude()));
                Log.i("location", String.valueOf(loc.getLatitude()));
            }

            //Mostramos la última posición conocida
            mostrarPosicion(loc);

            //Nos registramos para recibir actualizaciones de la posición
            locListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    mostrarPosicion(location);
                }
                public void onProviderDisabled(String provider){
                    lblEstado.setText("Provider OFF");
                }
                public void onProviderEnabled(String provider){
                    lblEstado.setText("Provider ON ");
                }
                public void onStatusChanged(String provider, int status, Bundle extras){
                    Log.i("", "Provider Status: " + status);
                    lblEstado.setText("Provider Status: " + status);
                }





            };

            locManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 30000, 0, locListener);

        }
        catch(Exception e) {
            Log.e("ERROR", "Error: " + e);
        }
        finally {
            Log.i("INFO", "Salimos de comenzarLocalizacion");
        }

    }





    private void mostrarPosicion(Location loc) {

        Time time = new Time();
        time.setToNow();
        System.out.println("time: " + time.hour+":"+time.minute);

        if(loc != null)
        {
            lblLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            lblLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            lblPrecision.setText("Hora: " + time.hour+":"+time.minute);
            Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
        }
        else
        {
            lblLatitud.setText("Latitud: (sin_datos)");
            lblLongitud.setText("Longitud: (sin_datos)");
            lblPrecision.setText(time.hour+":"+time.minute);
        }
    }
}
