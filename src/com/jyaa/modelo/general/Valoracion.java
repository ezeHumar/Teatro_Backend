package com.jyaa.modelo.general;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.jyaa.modelo.Obra;

@Entity
public class Valoracion {

	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@ManyToOne//(cascade=CascadeType.ALL)
	private Obra obra;
	int puntuacion;
	String comentario;
	
	public Valoracion() {
		// TODO Auto-generated constructor stub
	}
	public Obra getObra() {
		return obra;
	}
	public void setObra(Obra obra) {
		this.obra = obra;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}


}
