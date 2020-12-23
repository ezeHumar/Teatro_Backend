package com.jyaa.dao.jpa;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jvnet.hk2.annotations.Service;

import com.jyaa.dao.UsuarioDAO;
import com.jyaa.modelo.Usuario;

@Service
public class JPAUsuarioDAO extends JPADAO<Usuario> implements UsuarioDAO{

	public JPAUsuarioDAO() {
		// TODO Auto-generated constructor stub
		super(Usuario.class);
	}
	
	@Inject
	EntityManager manager;
	
	public Usuario validar(Usuario u) {
		try {
			Query q= manager.createQuery("from Usuario u where email=:email and contrasenia=:contrasenia");
			q.setParameter("email", u.getEmail());
			q.setParameter("contrasenia", u.getContrasenia());
			Usuario usuario=(Usuario) q.getSingleResult();
			return usuario;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar el usuario");
			return null;
		}
	}
	
	public Usuario validarAdmin(Usuario u) {
		try {
			Query q= manager.createQuery("from Usuario u where email=:email and contrasenia=:contrasenia and esAdmin=true");
			q.setParameter("email", u.getEmail());
			q.setParameter("contrasenia", u.getContrasenia());
			Usuario usuario=(Usuario) q.getSingleResult();
			return usuario;
		}
		catch(Exception e){
			System.out.println("No se pudo recuperar el usuario");
			return null;
		}
	}
	
	public List<Usuario> getListPorNombre(String nombre){
		try {
			Query q= manager.createQuery("from Usuario where nombre like CONCAT('%',:nombre,'%') and (esAdmin=true or esOperador=true)");
			q.setParameter("nombre", nombre);
			List<Usuario> Usuarios=(List<Usuario>) q.getResultList();
			return Usuarios;
		}
		catch(Exception e){
			System.out.println("No se pudo listar los usuarios");
			return null;
		}
	}
	
	public List<Usuario> getListPorApellido(String apellido){
		try {
			Query q= manager.createQuery("from Usuario where apellido like CONCAT('%',:apellido,'%') and (esAdmin=true or esOperador=true)");
			q.setParameter("apellido", apellido);
			List<Usuario> Usuarios=(List<Usuario>) q.getResultList();
			return Usuarios;
		}
		catch(Exception e){
			System.out.println("No se pudo listar los usuarios");
			return null;
		}
	}
	
	public List<Usuario> getListPorEmail(String email){
		try {
			Query q= manager.createQuery("from Usuario where email like CONCAT('%',:email,'%') and (esAdmin=true or esOperador=true)");
			q.setParameter("email", email);
			List<Usuario> Usuarios=(List<Usuario>) q.getResultList();
			return Usuarios;
		}
		catch(Exception e){
			System.out.println("No se pudo listar los usuarios");
			return null;
		}
	}

	@Override
	public List<Usuario> getListFiltered(String nombre, String apellido, String email) {
		StringBuilder query = new StringBuilder("from Usuario u where DTYPE=Usuario");
		
		if(nombre!=null || apellido!=null || email!=null) {
			query.append(" and (");
		}
		
		if(nombre!=null) {
			query.append("u.nombre like CONCAT('%',:nombre,'%') ) ");
			if(apellido!=null || email!=null){
				query.append("and (");
			}
		}
		
		if(apellido!=null) {
			query.append("u.apellido like CONCAT('%',:apellido,'%') )");
			if( email!=null){
				query.append("and (");
			}
		}
		
		if(email!=null) {
			query.append("u.email like CONCAT('%',:email,'%') )");
		}
		
		System.out.println(query.toString());
		
		Query q= manager.createQuery(query.toString());
		
		if (nombre!=null) {
			q.setParameter("nombre", nombre);
		}
		if (apellido!=null) {
			q.setParameter("apellido", apellido);
		}
		if (email!=null) {
			q.setParameter("email", email);
		}
		
		
		List<Usuario> usuarios=(List<Usuario>) q.getResultList();			
		return usuarios;
	}

	@Override
	public boolean existe(Usuario u) {
		try {
			Query q= manager.createQuery("from Usuario u where email=:email");
			q.setParameter("email", u.getEmail());
			Usuario usuario=(Usuario) q.getSingleResult();
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

}
