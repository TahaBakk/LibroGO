package com.example.x3727349s.librogo;

/**
 * Created by x3727349s on 07/03/17.
 */

public class Pojo {

    private static double longitude;
    private static double latitude;
    private static String rutaFoto;


    public double getLongitude() {return longitude;}
    public double getLatitude() {return latitude;}
    public String getRutaFoto() {return rutaFoto;}

    public static void setLongitude(double lon) {longitude = lon;}
    public static void setLatitude(double lat) {latitude = lat;}
    public static void setRutaFoto(String path) {rutaFoto = path;}

    @Override
    public String toString() {
        return "Pojo{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", rutaFoto='" + rutaFoto + '\'' +
                '}';
    }
}
