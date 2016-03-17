package com.upc.model;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by gmalex on 03/12/2015.
 */
public class OrdenAtencionBean {
    private String CodigoOrden;
    private String Descripcion;
    private String Estado;
    private String Comentarios;
    private String Fecha;
    private String CodigoInterrupcion;
    private String CodigoCuadrilla;
    private String CodigoUsuario;

    public String getCodigoUsuario() {
        return CodigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        CodigoUsuario = codigoUsuario;
    }

    public String getCodigoOrden() {
        return CodigoOrden;
    }

    public void setCodigoOrden(String codigoOrden) {
        CodigoOrden = codigoOrden;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String comentarios) {
        Comentarios = comentarios;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getCodigoInterrupcion() {
        return CodigoInterrupcion;
    }

    public void setCodigoInterrupcion(String codigoInterrupcion) {
        CodigoInterrupcion = codigoInterrupcion;
    }

    public String getCodigoCuadrilla() {
        return CodigoCuadrilla;
    }

    public void setCodigoCuadrilla(String codigoCuadrilla) {
        CodigoCuadrilla = codigoCuadrilla;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public String toString() {
        return "Datos:" + System.lineSeparator()+
                "CodigoOrden='" + CodigoOrden + '\'' +System.lineSeparator()+
                "Descripcion='" + Descripcion + '\'' +System.lineSeparator()+
                "Estado='" + Estado + '\'' +System.lineSeparator()+
                "Fecha='" + Fecha + '\'' +System.lineSeparator();
    }
}
