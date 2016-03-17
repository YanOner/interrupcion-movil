package com.upc.model;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by gmalex on 03/12/2015.
 */
public class CuadrillaBean {

    private String CodigoCuadrilla;
    private String Descripcion;
    private String Estado;
    private String CodigoUsuario;

    public String getCodigoCuadrilla() {
        return CodigoCuadrilla;
    }

    public void setCodigoCuadrilla(String codigoCuadrilla) {
        CodigoCuadrilla = codigoCuadrilla;
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

    public String getCodigoUsuario() {
        return CodigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        CodigoUsuario = codigoUsuario;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public String toString() {
        return "Datos:" + System.lineSeparator()+
                "CodigoCuadrilla='" + CodigoCuadrilla + '\'' +System.lineSeparator()+
                "Descripcion='" + Descripcion + '\'' +System.lineSeparator()+
                "Estado='" + Estado + '\'' +System.lineSeparator();
    }

}
