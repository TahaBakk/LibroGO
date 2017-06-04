package com.example.x3727349s.librogo;

/**
 * Created by x3727349s on 07/03/17.
 */

public class Pojo {

    private  double longitude;
    private  double latitude;
    private  String rutaFoto;

    public Pojo(double longitude, double latitude, String rutaFoto) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.rutaFoto = rutaFoto;
    }

    public double getLongitude() {return longitude;}
    public double getLatitude() {return latitude;}
    public String getRutaFoto() {return rutaFoto;}

    public void setLongitude(double lon) {longitude = lon;}
    public void setLatitude(double lat) {latitude = lat;}
    public void setRutaFoto(String path) {rutaFoto = path;}

    @Override
    public String toString() {
        return "Pojo{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", rutaFoto='" + rutaFoto + '\'' +
                '}';
    }
}
