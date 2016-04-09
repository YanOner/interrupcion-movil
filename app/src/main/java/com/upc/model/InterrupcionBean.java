package com.upc.model;

import android.annotation.TargetApi;
import android.os.Build;

public class InterrupcionBean {

	private String CodigoInterrupcion;
	private String Descripcion;
	private String Estado;
	private String Fecha;
	private String CodigoUsuario;
	private String Longitud;
	private String Latitud;

	public String getLongitud() {
		return Longitud;
	}

	public void setLongitud(String longitud) {
		Longitud = longitud;
	}

	public String getLatitud() {
		return Latitud;
	}

	public void setLatitud(String latitud) {
		Latitud = latitud;
	}

	public String getCodigoInterrupcion() {
		return CodigoInterrupcion;
	}

	public void setCodigoInterrupcion(String codigoInterrupcion) {
		CodigoInterrupcion = codigoInterrupcion;
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

	public String getFecha() {
		return Fecha;
	}

	public void setFecha(String fecha) {
		Fecha = fecha;
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
		return "Datos:" +System.lineSeparator()+
				"CodigoInterrupcion='" + CodigoInterrupcion + '\'' +System.lineSeparator()+
				"Descripcion='" + Descripcion + '\'' +System.lineSeparator()+
				"Estado='" + Estado + '\'' +System.lineSeparator()+
				"Fecha='" + Fecha + '\'' +System.lineSeparator();
	}
}
