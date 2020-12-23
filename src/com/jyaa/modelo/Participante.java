package com.jyaa.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.jyaa.modelo.general.Notificacion;
import com.jyaa.modelo.general.Valoracion;

@Entity
public class Participante extends Usuario {
	
	@ManyToMany
	List<Obra> interesesObras;
	//@JsonIgnore
	@ManyToMany
	List<Artista> interesesArtistas;
	//@JsonIgnore
	@ManyToMany
	List<Tag>  interesesTags;
	//@JsonIgnore
	@ManyToMany
	List<Actividad> interesesActividades;
	//@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
	List<Valoracion> valoraciones;
	//@JsonIgnore
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name="asistenciasP")
	List<Actividad> asistencias;
	//@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	List<Notificacion> notificaciones;
	

	public Participante() {
		super();
		this.setEsOperador(false);
		this.setEsAdmin(false);
	}

	
	public List<Obra> getInteresesObras() {
		return interesesObras;
	}
	public void setInteresesObras(List<Obra> interesesObras) {
		this.interesesObras = interesesObras;
	}
	
	public List<Artista> getInteresesArtistas() {
		return interesesArtistas;
	}
	public void setInteresesArtistas(List<Artista> interesesArtistas) {
		this.interesesArtistas = interesesArtistas;
	}
	
	public List<Tag> getInteresesTags() {
		return interesesTags;
	}
	public void setInteresesTags(List<Tag> interesesTags) {
		this.interesesTags = interesesTags;
	}
	
	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}
	public void setValoraciones(List<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}
	
	public List<Actividad> getAsistencias() {
		return asistencias;
	}
	public void setAsistencias(List<Actividad> asistencias) {
		this.asistencias = asistencias;
	}
	
	public List<Notificacion> getNotificaciones() {
		return notificaciones;
	}
	public void setTags(List<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}

	public void setNotificaciones(List<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}

	public List<Actividad> getInteresesActividades() {
		return interesesActividades;
	}

	public void setInteresesActividades(List<Actividad> interesesActividades) {
		this.interesesActividades = interesesActividades;
	}
	

}
