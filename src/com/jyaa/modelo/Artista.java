package com.jyaa.modelo;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity(name = "Artista")
public class Artista extends Persona {
	
	private String apodo;
	
	@ElementCollection
	Collection<String> nombreFotos;
	
	@ManyToMany(mappedBy="artistaParticipante")
	private List<Obra> obras;
	
	public Artista() {
		
	}
	
	public Artista(String apodo, String linkFoto, String nombre, String apellido, LocalDate fechaNacimiento) {
		this.apodo=apodo;
	}

	public String getApodo() {
		return apodo;
	}
	public void setApodo(String apodo) {
		this.apodo = apodo;
	}
	
	public Collection<String> getNombreFotos() {
		return nombreFotos;
	}

	public void setNombreFotos(Collection<String> nombreFotos) {
		this.nombreFotos = nombreFotos;
	}


	
}
