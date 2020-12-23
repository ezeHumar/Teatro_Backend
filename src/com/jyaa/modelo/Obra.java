package com.jyaa.modelo;

import java.util.Collection;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity(name = "Obra")
public class Obra {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String nombre;
	private String descripcion;
	private int entradasEntregadas;
	private int duracion;
	
	@ElementCollection
	Collection<String> nombreFotos;
	
	@ManyToMany
	@JoinTable(joinColumns = { @JoinColumn(name = "Obra_id")})
	private List<Artista> artistaParticipante;
	
	@ManyToMany
	private List<Tag> tags;

	
	private float valoracion;
	
	public Obra() {
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public List<Artista> getArtistaParticipante() {
		return artistaParticipante;
	}

	public void setArtistaParticipante(List<Artista> artistasParticipantes) {
		this.artistaParticipante = artistasParticipantes;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public float getValoracion() {
		return valoracion;
	}

	public void setValoracion(float valoracion) {
		this.valoracion = valoracion;
	}

	public long getId() {
		return id;
	}

	public int getEntradasEntregadas() {
		return entradasEntregadas;
	}

	public void setEntradasEntregadas(int entradasEntregadas) {
		this.entradasEntregadas = entradasEntregadas;
	}

	public Collection<String> getNombreFotos() {
		return nombreFotos;
	}

	public void setNombreFotos(Collection<String> nombreFotos) {
		this.nombreFotos = nombreFotos;
	}
	
}
