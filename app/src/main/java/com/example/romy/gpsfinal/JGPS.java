package com.example.romy.gpsfinal;

/**
 * Created by romy on 19/08/16.
 */
public class JGPS {
    private int id;
    private String latitud, longitud, hora;

    public JGPS(){

    }

    public JGPS(String latitud, String longitud, String hora){
        this.latitud = latitud;
        this.longitud = longitud;
        this.hora = hora;
    }

    public JGPS(int id, String latitud, String longitud, String hora){
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.hora = hora;
    }

    public void setId(int id){

        this.id = id;
    }

    public void setLongitud(String longitud){

        this.longitud = longitud;
    }

    public void setLatitud(String latitud){

        this.latitud = latitud;
    }

    public void setHora(String hora){

        this.hora = hora;
    }

    public int getId(){
        return id;
    }

    public String getLongitud(){

        return longitud;
    }

    public String getLatitud(){

        return latitud;
    }

    public String getHora(){

        return hora;
    }
}