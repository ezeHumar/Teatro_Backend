package com.jyaa.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.jyaa.modelo.Usuario;

public class TeatroSecurityContext implements SecurityContext {

	Usuario usuario;
	
	public TeatroSecurityContext(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String getAuthenticationScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		return usuario;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String rol) {
		if(this.usuario.isEsAdmin() && rol.equals("Admin")) {
			return true;
		}
		else {
			if(this.usuario.isEsOperador() && rol.equals("Operador")) {
				return true;
		}
			else {
				if(!this.usuario.isEsOperador() && !this.usuario.isEsAdmin() && rol.equals("Participante")) {
					return true;
				}else {
					return false;
				}
			}
		}

	}
}
