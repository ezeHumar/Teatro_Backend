package com.jyaa.dao.jpa;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.ActividadDAO;
import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.modelo.Actividad;
import com.jyaa.modelo.Participante;

@Service
public class JPAActividadDAO extends JPADAO<Actividad> implements ActividadDAO{

	@Inject
	EntityManager manager;
	
	@Inject
	ParticipanteDAO pdao;
	
	public JPAActividadDAO() {
		// TODO Auto-generated constructor stub
		super(Actividad.class);
	}

	@Override
	public boolean delete(long id) {
		try {
			//Pimero se borran las asistencias a la actividad de los participantes
			Query q= manager.createQuery("select p from Participante p inner join p.asistencias a where a.id=:actividadID");
			q.setParameter ("actividadID", id);
			List<Participante> p= (List<Participante>) q.getResultList();
			
			for(Participante par: p) {
				par.getAsistencias().remove(this.get(id));
				pdao.modify(par);
			}
			//Luego se borra la actividad
			EntityTransaction etx = manager.getTransaction();
			etx.begin();
			manager.remove(this.get(id));
			etx.commit();
			return true;
		}
		catch(Exception e){
			System.out.println("No se pudo borrar la actividad");
			return false;
		}
		
	}

	@Override
	public List<Actividad> getListFiltered(List<String> idObras, List<String> idEspacios, String edicionId, String horaMin, String horaMax,String fechaMin, String fechaMax) throws SQLException {
		
		Time horaMinAux = null;
		Time horaMaxAux = null;
		LocalDate fechaMinAux = null;
		LocalDate fechaMaxAux = null;
		
		Iterator<String> obrasIterator = idObras.iterator();
		Iterator<String> espaciosIterator = idEspacios.iterator();
		
		StringBuilder query = new StringBuilder("select distinct a from Actividad a");
		
		if(obrasIterator.hasNext()) {
			query.append(" left join a.obra o ");
		}
		
		if(espaciosIterator.hasNext()){
			query.append(" left join a.espacio es ");
		}
		
		if(edicionId!=null){
			query.append(" left join a.ediciones e ");
		}
		
		if (obrasIterator.hasNext() || espaciosIterator.hasNext() || edicionId!=null || horaMin!=null || horaMax!=null || fechaMin!=null || fechaMax!=null) {
			query.append(" where ");
		}
		
		if (horaMax!=null || horaMin!=null) {

			if (horaMax!=null && horaMin!=null){				
				query.append("(a.hora>=:horaMin and a.hora<=:horaMax)");
			}else if(horaMax!=null){
				query.append("(a.hora<=:horaMax)");
			}else if(horaMin!=null) {
				query.append("(a.hora>=:horaMin)");
			}

			if (obrasIterator.hasNext() || espaciosIterator.hasNext() || edicionId!=null || fechaMin!=null || fechaMax!=null) {
				query.append(" and ");
			}	
		}
		
		if (fechaMax!=null || fechaMin!=null) {

			if (fechaMax!=null && fechaMin!=null){
				fechaMinAux = LocalDate.parse(fechaMin);
				fechaMaxAux = LocalDate.parse(fechaMax);
				query.append("(a.fecha between :fechaMin and :fechaMax)");
			}else if(fechaMax!=null){
				fechaMaxAux = LocalDate.parse(fechaMax);
				query.append("(a.fecha<=:fechaMax)");
			}else if(fechaMin!=null) {
				fechaMinAux = LocalDate.parse(fechaMin);
				query.append("(a.fecha>=:fechaMin)");
			}

			if (obrasIterator.hasNext() || espaciosIterator.hasNext() || edicionId!=null) {
				query.append(" and ");
			}	
		}
		
		if(obrasIterator.hasNext()) {
			query.append("(");
		}
		while(obrasIterator.hasNext()) {
			query.append("(a.obra.id=" + obrasIterator.next() + ")");
			if(obrasIterator.hasNext()) {
				query.append(" or ");
			}
			else if(espaciosIterator.hasNext() || edicionId!=null){
				
				query.append(") and ");
				break;
			} else {
				query.append(")");
				break;
			}
		}
		
		if(espaciosIterator.hasNext()) {
			query.append("(");
		}
		while(espaciosIterator.hasNext()) {
			query.append("(es.id=" + espaciosIterator.next() + ")");
			if(espaciosIterator.hasNext()) {
				query.append(" or ");
			}
			else if(edicionId!=null){
				query.append(") and (");
				break;
			} else {
				query.append(")");
				break;
			}
		}
		
		if(edicionId!=null) {
			query.append("e.id=" + edicionId + ")");
		}
		
		System.out.println(query.toString());
		Query q= manager.createQuery(query.toString());
		
		if(fechaMax!=null) {
			q.setParameter("fechaMax", fechaMaxAux);
		}
		
		if(fechaMin!=null) {
			q.setParameter("fechaMin", fechaMinAux);
		}
		
		if(horaMin!=null) {
			q.setParameter("horaMin", Time.valueOf(LocalTime.parse(horaMin))); //Hago esa conversion de LocalTime a Time porque Time.parse no anda bien, esta Deprecated
		}
		
		if(horaMax!=null) {
			q.setParameter("horaMax", Time.valueOf(LocalTime.parse(horaMax)));
		}
				
		return  q.getResultList();
		
		
		
	}

	@Override
	public List<Long> getListNoBorrable() {
		try {
			
			Set<Long> set = new HashSet<Long>();
			
			//Busco los id de las actividades que pertenecen a otra entidad
			List<Long> noBorrables=(List<Long>) manager.createQuery("select distinct a.id from Edicion e inner join e.actividades a").getResultList();

	        return noBorrables;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar los no borrables");
			return null;
		}
	}

	
}
