package com.jyaa.dao.jpa;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.ObraDAO;
import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.modelo.Obra;
import com.jyaa.modelo.Participante;
import com.jyaa.modelo.general.Valoracion;

@Service
public class JPAObraDAO extends JPADAO<Obra> implements ObraDAO{
	
	@Inject
	EntityManager manager;
	
	@Inject
	ParticipanteDAO pdao;

	public JPAObraDAO() {
		// TODO Auto-generated constructor stub
		super(Obra.class);
	}
	
	@Override
	public boolean delete(long id) {
		
		try {
			//Pimero se borran las valoraciones a la obra de los participantes
			Query q= manager.createQuery("select p from Participante p inner join p.valoraciones v inner join v.obra o where o.id=:obraID");
			q.setParameter ("obraID", id);
			List<Participante> p= (List<Participante>) q.getResultList();
			
			Valoracion vBorrar=null;
			for(Participante par: p) {
				int i=0;//posicion en la que se encuentra en la lista de valoraciones
				
				List<Valoracion> v = par.getValoraciones();
				Iterator<Valoracion> vIterator = v.iterator();
				Valoracion vAux;
				while(vIterator.hasNext()) {
					vAux=vIterator.next();
					if(vAux.getObra().equals(this.get(id))){
						vBorrar=vAux;//Me quedo con la validacion que debo borrar. No la puedo borrar directamente aca porque da error
						
					}
					i++;
				}
				if(vBorrar!=null) {
					v.remove(vBorrar);
					par.setValoraciones(v);
					pdao.modify(par);
				}
			}
			
			//Luego se borra la obra
			EntityTransaction etx = manager.getTransaction();
			etx.begin();
			manager.remove(this.get(id));
			etx.commit();
			return true;
		}
		catch(Exception e){
			System.out.println("No se pudo borrar la obra");
			return false;
		}
	}

	@Override
	public List<Obra> getListFiltered(List<String> idArtistas, List<String> idTags, String nombre) {
		try {
			
			StringBuilder query = new StringBuilder("Select distinct o from Obra o left join o.artistaParticipante a left join o.tags t");
			
			Iterator<String> artistasIterator=idArtistas.iterator();
			Iterator<String> tagsIterator=idTags.iterator();
			
			if(artistasIterator.hasNext() || tagsIterator.hasNext() || nombre!=null) {
				query.append(" where (");
			}
			
			if(nombre!=null) {
				query.append("o.nombre like CONCAT('%',:nombre,'%') )");
				if(artistasIterator.hasNext() || tagsIterator.hasNext()){
					query.append("and (");
				}
			}
			
			while(artistasIterator.hasNext()) {
				query.append("a.id=" + artistasIterator.next());
				if(artistasIterator.hasNext()) {
					query.append(" or ");
				}
				else if(tagsIterator.hasNext()){
					query.append(") and (");
					break;
				} else {
					query.append(")");
					break;
				}
			}
			
			while(tagsIterator.hasNext()) {
				query.append("t.id="+ tagsIterator.next());
				if(tagsIterator.hasNext()) {
					query.append(" or ");
				}else {
					query.append(")");
				}
			}
			
			Query q= manager.createQuery(query.toString());
			if (nombre!=null) {
				q.setParameter("nombre", nombre);
			}
			
			List<Obra> obras=(List<Obra>) q.getResultList();			
			return obras;
		}
		catch(Exception e){
			System.out.println(e.toString());
			System.out.println("No se pudo recuperar la obra");
			return null;
		}
	}
	
	@Override
	public List<Valoracion> getValoraciones(long id) {
		
		try {
			Query q= manager.createQuery("Select v from Participante p inner join p.valoraciones v where v.obra.id=:id");
			q.setParameter("id", id);
			List<Valoracion> valoraciones=(List<Valoracion>) q.getResultList();			
			return valoraciones;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar la obra");
			return null;
		}
	}

	@Override
	public List<Long> getListNoBorrable() {
		try {
			//Busco los id de los tags que pertenecen a otra entidad
			List<Long> noBorrables=(List<Long>) manager.createQuery("select distinct o.id from Actividad a inner join a.obra o").getResultList();
	        return noBorrables;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar los no borrables");
			return null;
		}
	}
}
