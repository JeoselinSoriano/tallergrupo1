package com.example.romy.gpsfinal;

//http://www.sgoliver.net/blog/localizacion-geografica-en-android-i/


import android.app.Activity;
import android.app.DownloadManager;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;




import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;



public class MainLocalizacion extends Activity {


    // AppCompatActivity
    private Button btnActualizar;
    private Button btnDesactivar;
    private TextView lblLatitud;
    private TextView lblLongitud;
    private TextView lblHora;
    private TextView lblEstado;


//  URL Servico Web
    private static final String URL_GRABA = "http://localhost/webservice/ubicacion.php/";
    private static final String URL_CONSULTA = "http://localhost/webservice/consultar.php";
    private static final String TAG = MainLocalizacion.class.getSimpleName();


//
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

            lblHora = (TextView) findViewById(R.id.LblPosHora);
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
            lblHora.setText("Hora: " + time.hour+":"+time.minute);
            Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude() + " - " + String.valueOf(loc.getTime()))));
        }
        else
        {
            lblLatitud.setText("Latitud: (sin_datos)");
            lblLongitud.setText("Longitud: (sin_datos)");
            lblHora.setText(time.hour+":"+time.minute);
        }
    }






// Datos para el servicio web

    public void insertLocation (View v){
        String latitud = this.lblLatitud.getText().toString();
        String longitud = this.lblLongitud.getText().toString();
        String hora = this.lblHora.getText().toString();
        if(!latitud.equals("") && !longitud.equals("") && !hora.equals("")){
            insertOnServer(latitud,longitud,hora);
        }else{
            Toast.makeText(getApplicationContext(),
                    "Llena todos los campos",Toast.LENGTH_SHORT).show();
        }
    }

    private void insertOnServer(final String lat, final String lon, final String hor){
        VolleyInstancia.getInstance(this).addToRequestQueue(
                new StringRequest(DownloadManager.Request.Method.POST, URL_GRABA,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject oJson = new JSONObject(response);
                                    Log.d(TAG, oJson.toString());
                                    procesarRespuesta(oJson);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "ERROR VOLLEY: " + error.getMessage());
                            }
                        }
                ){
                    @Override
                    protected Map<String,String> getParams()throws AuthFailureError {
                        Map<String,String> parameters = new HashMap<>();
                        parameters.put("latitud",lat);
                        parameters.put("longitud",lon);
                        parameters.put("hora",hor);
                        return parameters;
                    }
                }
        );
    }

    private void procesarRespuesta(JSONObject response){
        try{
            String estado = response.getString("estado");
            String mensaje = response.getString("mensaje");
            switch (estado) {
                case "1":
                    Toast.makeText(getApplicationContext(),
                            mensaje,Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(getApplicationContext(),
                            mensaje,Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void consultar(View v){
        VolleyInstancia.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.POST, URL_CONSULTA,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                JSONArray jsonArray;
                                try {
                                    jsonArray = response.getJSONArray("horas");
                                    for(int i=0; i<jsonArray.length(); i++){
                                        try{
                                            JSONObject objeto= jsonArray.getJSONObject(i);
                                            if(!objeto.getString("hora").equals("")) {
                                                Log.d(TAG, "Latitud " + objeto.getString("latitud"));
                                                Log.d(TAG, "Longitud " + objeto.getString("longitud"));
                                                Log.d(TAG, "Hora " + objeto.getString("hora"));
                                            }
                                        }catch (JSONException e) {
                                            Log.e(TAG, "Error de parsing: "+ e.getMessage());
                                        }
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "ERROR VOLLEY: " + error.getMessage());
                            }
                        }
                ));
    }








}
