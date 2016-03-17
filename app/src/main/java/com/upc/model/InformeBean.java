package com.upc.model;

/**
 * Created by gmalex on 04/12/2015.
 */
public class InformeBean {
    private String CodigoInforme;
    private String Descripcion;
    private String Detalle;

    public String getCodigoInforme() {
        return CodigoInforme;
    }

    public void setCodigoInforme(String codigoInforme) {
        CodigoInforme = codigoInforme;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Datos:" +System.lineSeparator()+
                "CodigoInforme='" + CodigoInforme + '\'' +System.lineSeparator()+
                "Descripcion='" + Descripcion + '\'' +System.lineSeparator()+
                "Detalle='" + Detalle + '\'' +System.lineSeparator();
    }
}
