package com.upc.utility;

/**
 * Created by JHuamancama on 08/04/2016.
 */
public class services {
    //Local
    //public static String rutaWS = "http://10.0.2.2:8080";
    //Nube
    public static String rutaWS = "http://104.208.233.67:8080";

    public static String WValidarUsuario = rutaWS + "/WValidarUsuario/";
    public static String WInterrupcionLista = rutaWS + "/WInterrupcionLista/";
    public static String WOrdenAtencionLista = rutaWS + "/WOrdenAtencionLista/";
    public static String WOrdenAtencionInterrupcion = rutaWS + "/WOrdenAtencionInterrupcion/";
    public static String WCuadrillaLista = rutaWS + "/WCuadrillaLista/";

    public static String WAsignarCuadrilla = rutaWS + "/WAsignarCuadrilla/";
    public static String WAtenderOrden= rutaWS + "/WAtenderOrden/";
    public static String WEnviarOrden= rutaWS + "/WEnviarOrden/";
}
