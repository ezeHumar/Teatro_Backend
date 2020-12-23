package com.jyaa.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.jyaa.modelo.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		try {
            String token = authorizationHeader.substring(7);
            Claims claims =  Jwts.parser().setSigningKey("1234").parseClaimsJws(token).getBody();
            Usuario usuario = new Usuario();
            usuario = setRol(usuario, (String) claims.get("rol"));
            usuario.setName((String) claims.get("email"));
            TeatroSecurityContext secContext = new TeatroSecurityContext(usuario);
            requestContext.setSecurityContext(secContext);
        } catch (ExpiredJwtException e) {
        	System.out.println(e.toString());
        	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Sesion expirada").build());
        }
		catch (Exception e) {
			System.out.println(e.toString());
        	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("El usuario no puede acceder a este recurso").build());
        }
    }

	private Usuario setRol(Usuario usuario, String rol) {
		switch(rol){
			case "Admin":
				usuario.setEsAdmin(true);
				break;
			case "Operador":
				usuario.setEsOperador(true);
				break;
			default:
				usuario.setEsAdmin(false);
				usuario.setEsOperador(false);
				break;
		}
		return usuario;
	}
}
