package com.jyaa.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.jyaa.modelo.general.Notificacion;

@Entity
public class Visitante{
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@ManyToMany(cascade=CascadeType.ALL)
	List<Notificacion>  notificaciones;
	
	
	public Visitante() {
		super();
	}
	
	
	public List<Notificacion> getNotificaciones() {
		return notificaciones;
	}
	public void setNotificaciones(List<Notificacion> notificaciones) {
		this.notificaciones= notificaciones;
	}


	public long getId() {
		return id;
	}


}
