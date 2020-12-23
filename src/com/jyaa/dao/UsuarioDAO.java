package com.jyaa.dao;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;

import com.jyaa.modelo.Usuario;

@Contract
public interface UsuarioDAO extends DAO<Usuario>{
	public boolean existe(Usuario usuario);
	public Usuario validar(Usuario usuario);
	public Usuario validarAdmin(Usuario usuario); 
	public List<Usuario> getListFiltered(String nombre, String apellido, String email);
	public List<Usuario> getListPorNombre(String nombre);
	public List<Usuario> getListPorApellido(String apellido);
	public List<Usuario> getListPorEmail(String email);
}
