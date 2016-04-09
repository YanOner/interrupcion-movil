package com.upc.model;

/**
 * Created by gmalex on 04/12/2015.
 */
public class InformeBean {
    private String CodigoInforme;
    private String Descripcion;
    private String Detalle;
    private String codigoOrden;
    private String codigoInterrupcion;
    private String codigoCuadrilla;

    public String getCodigoOrden() {
        return codigoOrden;
    }

    public void setCodigoOrden(String codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public String getCodigoInterrupcion() {
        return codigoInterrupcion;
    }

    public void setCodigoInterrupcion(String codigoInterrupcion) {
        this.codigoInterrupcion = codigoInterrupcion;
    }

    public String getCodigoCuadrilla() {
        return codigoCuadrilla;
    }

    public void setCodigoCuadrilla(String codigoCuadrilla) {
        this.codigoCuadrilla = codigoCuadrilla;
    }

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
        return "InformeBean{" +
                "CodigoInforme='" + CodigoInforme + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Detalle='" + Detalle + '\'' +
                ", codigoOrden='" + codigoOrden + '\'' +
                ", codigoInterrupcion='" + codigoInterrupcion + '\'' +
                ", codigoCuadrilla='" + codigoCuadrilla + '\'' +
                '}';
    }
}
