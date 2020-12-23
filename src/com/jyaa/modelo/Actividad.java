package com.jyaa.modelo;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jyaa.modelo.general.Evento;
import com.jyaa.utils.LocalDateDeserializer;
import com.jyaa.utils.LocalDateSerializer;
import com.jyaa.utils.SqlTimeDeserializer;


@Entity
public class Actividad {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@OneToOne
	private Obra obra;
	@ManyToOne
	private Espacio espacio;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;
//	@JsonDeserialize(using = LocalTimeDeserializer.class)
//	@JsonSerialize(using = LocalTimeSerializer.class)
//	@JsonFormat(pattern = "HH:mm:ss")
//	private LocalTime hora;
	@JsonFormat(pattern = "HH:mm")
	@JsonDeserialize(using = SqlTimeDeserializer.class)
	private Time hora; //Termine usando Time ya que de esta forma me deja hacer los filtros de busqueda. Con LocalTime no podia hacerlo funcionar
	@Column
	@ManyToMany
	private List<Tag> tags;
	@OneToOne
	private Evento evento; //En caso de que se demore, se suspenda, o se agoten entradas
	
	@ManyToMany(mappedBy="actividades")
	private List<Edicion> ediciones;
	
	private int codigo;
	
	public Actividad() {
		Random rand = new Random();
		this.setCodigo(rand.nextInt(10000)); //se le asigna un nro aleatoreo desde 0 a 9999
	}
	
	public Actividad(long id, Obra obra, Espacio espacio, LocalDate fecha, Time hora, List<Tag> tags, Evento evento) {
		this.id=id;
		this.espacio=espacio;
		this.fecha=fecha;
		this.hora=hora;
		this.tags=tags;
		this.evento=evento;
	}
	
	public Obra getObra() {
		return obra;
	}
	public void setObra(Obra obra) {
		this.obra = obra;
	}
	public Espacio getEspacio() {
		return espacio;
	}
	public void setEspacio(Espacio espacio) {
		this.espacio = espacio;
	}

	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public long getId() {
		return id;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
}
