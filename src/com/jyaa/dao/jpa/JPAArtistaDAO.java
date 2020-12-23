package com.jyaa.dao.jpa;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.ArtistaDAO;
import com.jyaa.modelo.Artista;

@Service
public class JPAArtistaDAO extends JPADAO<Artista> implements ArtistaDAO{

	public JPAArtistaDAO() {
		// TODO Auto-generated constructor stub
		super(Artista.class);
	}
	
	@Inject
	EntityManager manager;

	@Override
	public List<Artista> getListFiltered(String nombre, String apellido, List<String> idTags, List<String> idObras) {
		StringBuilder query = new StringBuilder("Select distinct a from Artista a left join a.obras o left join o.tags t");
		
		Iterator<String> obrasIterator=idObras.iterator();
		Iterator<String> tagsIterator=idTags.iterator();
		
		if(obrasIterator.hasNext() || tagsIterator.hasNext() || nombre!=null || apellido!=null) {
			query.append(" where (");
		}
		
		if(nombre!=null) {
			query.append("a.nombre like CONCAT('%',:nombre,'%') ) ");
			if(apellido!=null || obrasIterator.hasNext() ||  tagsIterator.hasNext()){
				query.append("and (");
			}
		}
		
		if(apellido!=null) {
			query.append("a.apellido like CONCAT('%',:apellido,'%') )");
			if(obrasIterator.hasNext() ||  tagsIterator.hasNext()){
				query.append("and (");
			}
		}
		
		while(obrasIterator.hasNext()) {
			query.append("o.id=" + obrasIterator.next());
			if(obrasIterator.hasNext()) {
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
		
		System.out.println(query.toString());
		
		Query q= manager.createQuery(query.toString());
		
		if (nombre!=null) {
			q.setParameter("nombre", nombre);
		}
		if (apellido!=null) {
			q.setParameter("apellido", apellido);
		}
		
		
		List<Artista> artistas=(List<Artista>) q.getResultList();			
		return artistas;
	}

	@Override
	public List<Long> getListNoBorrable() {
		try {
			//Busco los id de los tags que pertenecen a otra entidad
			List<Long> noBorrables=(List<Long>) manager.createQuery("select distinct a.id from Obra o inner join o.artistaParticipante a").getResultList();
	
	        return noBorrables;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar los no borrables");
			return null;
		}
	}

}
