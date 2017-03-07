package com.example.x3727349s.librogo;

/**
 * Created by x3727349s on 07/03/17.
 */

public class Pojo {

    public double longitude;
    public double latitude;
    public String rutaFoto;


    public double getLongitude() {return longitude;}
    public double getLatitude() {return latitude;}
    public String getRutaFoto() {return rutaFoto;}

    public void setLongitude(double longitude) {this.longitude = longitude;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setRutaFoto(String rutaFoto) {this.rutaFoto = rutaFoto;}

    @Override
    public String toString() {
        return "Pojo{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", rutaFoto='" + rutaFoto + '\'' +
                '}';
    }
}
