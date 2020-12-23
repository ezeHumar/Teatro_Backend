package com.jyaa.modelo;

import java.security.Principal;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario extends Persona implements Principal{
	
	@Column(unique = true)
	private String email;
	private String contrasenia;
	private boolean esAdmin;
	private boolean esOperador;
	
	@JsonIgnore
	private String name;
	
	public Usuario() {
		super();
	}
	

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContrasenia() {
		return contrasenia;
	}
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public boolean isEsAdmin() {
		return esAdmin;
	}

	public void setEsAdmin(boolean esAdmin) {
		this.esAdmin = esAdmin;
	}

	public boolean isEsOperador() {
		return esOperador;
	}

	public void setEsOperador(boolean esOperador) {
		this.esOperador = esOperador;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	
}
