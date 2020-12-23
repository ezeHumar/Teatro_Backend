package com.jyaa.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.jyaa.modelo.general.Direccion;

@Entity
public class Espacio {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String nombre;
	private String descripcion;
	@OneToOne(cascade=CascadeType.ALL)
	private Direccion ubicacion;
	private int capacidad;
	private boolean abierto;
	
	public Espacio() {
		
	}
	
	public Espacio(String nombre, String descripcion, Direccion ubicacion, int capacidad, boolean abierto, List<Tag> tags) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.ubicacion = ubicacion;
		this.capacidad = capacidad;
		this.abierto = abierto;
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
	public Direccion getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(Direccion ubicacion) {
		this.ubicacion = ubicacion;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public boolean isAbierto() {
		return abierto;
	}
	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}


	public long getId() {
		return id;
	}
}
