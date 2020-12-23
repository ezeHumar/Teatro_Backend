package com.jyaa.resources;

import java.util.Date;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.jyaa.dao.UsuarioDAO;
import com.jyaa.modelo.Usuario;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/login")
public class LoginResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Inject
	UsuarioDAO udao;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response  validar(Usuario usuarioAux) {
		Usuario usuario = udao.validar(usuarioAux);
		Header header = Jwts.header();
	    header.setType("JWT");
		if (usuario != null) {
			String rol=this.setRol(usuario);
			long tiempo = System.currentTimeMillis();
			String jwt = Jwts.builder()
					.signWith(SignatureAlgorithm.HS256, "1234")
					.setSubject(usuario.getEmail())
					.claim("rol", rol)
					.claim("email", usuario.getEmail())
					.setIssuedAt(new Date(tiempo))
					.setExpiration(new Date(tiempo+9000000)) //Se vence el token en 15mins
					.compact();
			
			JsonObject json = Json.createObjectBuilder()
								.add("JWT", jwt).build();
			
			return Response.status(Response.Status.CREATED).entity(json.toString()).build();
			
		}
		
		return Response.status(Response.Status.ACCEPTED.UNAUTHORIZED).entity("Revise usuario y contraseña").build();
		
	}
	
	@POST
	@Path("admin")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response  validarAdmin(Usuario usuarioAux) {
		Usuario usuario = udao.validarAdmin(usuarioAux);
		Header header = Jwts.header();
	    header.setType("JWT");
		if (usuario != null) {
			String rol=this.setRol(usuario);
			long tiempo = System.currentTimeMillis();
			String jwt = Jwts.builder()
					.signWith(SignatureAlgorithm.HS256, "1234")
					.setSubject(usuario.getEmail())
					.claim("rol", rol)
					.claim("email", usuario.getEmail())
					.setIssuedAt(new Date(tiempo))
					.setExpiration(new Date(tiempo+9000000)) //Se vence el token en 15mins
					.compact();
			
			JsonObject json = Json.createObjectBuilder()
								.add("JWT", jwt).build();
			
			return Response.status(Response.Status.CREATED).entity(json.toString()).build();
			
		}
		
		return Response.status(Response.Status.ACCEPTED.UNAUTHORIZED).entity("Revise usuario y contraseña").build();
		
	}
	
	private String setRol(Usuario usuario){
		if(usuario.isEsAdmin()) {
			return "Admin";
		}else if(usuario.isEsOperador())  {
			return "Operador";
		}
		return "Participante";
		
	}
}
