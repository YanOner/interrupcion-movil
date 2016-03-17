package com.upc.model;

public class Usuario {

	private String codigo;
	private String nombre;
	private String password;
	private boolean valido;
	private String mensaje;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public boolean isValido() {
		return valido;
	}

	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public boolean getValido() {
		return valido;
	}


	public void setValido(boolean valido) {
		this.valido = valido;
	}


	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", password=" + password + ", valido=" + valido + ", mensaje=" + mensaje
				+ "]";
	}
	
}
