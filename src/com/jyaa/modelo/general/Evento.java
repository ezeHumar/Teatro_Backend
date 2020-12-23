package com.jyaa.modelo.general;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Evento {
	

	public Evento(boolean entradasAgotadas, boolean suspendido, int demora) {
		super();
		this.entradasAgotadas = entradasAgotadas;
		this.suspendido = suspendido;
		this.demora = demora;
	}

	//Esta clase se utiliza para indicar eventualidades en las actividades, como entradas agotadas, suspension o demora;
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private boolean entradasAgotadas;
	private boolean suspendido;
	private int demora; //en minutos

	public Evento() {
		// TODO Auto-generated constructor stub
	}

	public boolean isEntradasAgotadas() {
		return entradasAgotadas;
	}

	public void setEntradasAgotadas(boolean entradasAgotadas) {
		this.entradasAgotadas = entradasAgotadas;
	}

	public boolean isSuspendido() {
		return suspendido;
	}

	public void setSuspendido(boolean suspendido) {
		this.suspendido = suspendido;
	}

	public int getDemora() {
		return demora;
	}

	public void setDemora(int demora) {
		this.demora = demora;
	}


}
