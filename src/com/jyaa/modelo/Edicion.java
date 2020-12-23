package com.jyaa.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jyaa.utils.LocalDateDeserializer;
import com.jyaa.utils.LocalDateSerializer;
import com.jyaa.utils.LocalTimeDeserializer;
import com.jyaa.utils.LocalTimeSerializer;

@Entity
public class Edicion {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private	String titulo;
	private String descripcion;
	@ElementCollection
	Collection<String> nombreFotos;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate fechaInicio;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate fechaFin;
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	@JsonSerialize(using = LocalTimeSerializer.class)
	private LocalTime horaInicio;
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	@JsonSerialize(using = LocalTimeSerializer.class)
	private LocalTime horaFin;
	
	@ManyToMany
	@JoinTable(joinColumns = { @JoinColumn(name = "Edicion_id")})
	private List<Actividad> actividades;
	
	public Edicion() {
		
	}
	

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Actividad> getActividades(){
		return this.actividades;
	}
	
	public void setActividades(List<Actividad> actividades) {
		this.actividades=actividades;
	}


	public LocalDate getFechaInicio() {
		return fechaInicio;
	}



	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}



	public LocalDate getFechaFin() {
		return fechaFin;
	}



	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}



	public LocalTime getHoraInicio() {
		return horaInicio;
	}



	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}



	public LocalTime getHoraFin() {
		return horaFin;
	}



	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}


	public long getId() {
		return id;
	}


	public Collection<String> getNombreFotos() {
		return nombreFotos;
	}


	public void setNombreFotos(Collection<String> nombreFotos) {
		this.nombreFotos = nombreFotos;
	}



}
