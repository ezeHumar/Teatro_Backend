package com.jyaa.resources;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.dao.UsuarioDAO;
import com.jyaa.modelo.Participante;
import com.jyaa.modelo.Usuario;
import com.jyaa.security.Secured;


@Path("/usuarios")
public class UsuariosResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Inject
	UsuarioDAO udao;
	
	@Inject
	ParticipanteDAO pdao;
	
	private String msj;
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> getUsuariosFiltered(@QueryParam("nombre")String nombre,@QueryParam("apellido") String apellido, @QueryParam("email") String email){
		return udao.getListFiltered(nombre, apellido, email);
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("buscarNombre")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> getUsuariosPorNombre(@QueryParam("nombre") String nombre){
		return udao.getListPorNombre(nombre);
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("buscarApellido")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> getUsuariosPorApellido(@QueryParam("apellido") String apellido){
		return udao.getListPorApellido(apellido);
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("buscarEmail")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> getUsuariosPorEmail(@QueryParam("email") String email){
		return udao.getListPorEmail(email);
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Usuario u= udao.get(id);
		if(u != null) {
			return Response
					.ok()
					.entity(u)
					.build();					
		}
		else {
			msj="No se encontro el usuario";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}
	
	@POST
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response set(Usuario u) {
		
		if(u.getApellido()==null || u.getNombre()==null || u.getEmail()==null || u.getFechaNacimiento()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(u.getFechaNacimiento().isAfter(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de nacimento no puede ser una fecha futura").build();
		}
		if(udao.existe(u)) {
			return Response.status(Response.Status.CONFLICT).entity("Ya existe un usuario con la misma direccion de email").build();
		}else {
			udao.set(u);
		}
		
		return Response.status(Response.Status.CREATED).entity("[]").build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Usuario u) {
		if(u.getApellido()==null || u.getNombre()==null || u.getEmail()==null || u.getFechaNacimiento()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(u.getFechaNacimiento().isAfter(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de nacimento no puede ser una fecha futura").build();
		}
		if(udao.existe(u) && !u.getEmail().equals(udao.get(u.getId()).getEmail())) {
			return Response.status(Response.Status.CONFLICT).entity("Ya existe un usuario con la misma direccion de email").build();
		}else {
			Usuario uAux=udao.get(u.getId());
			if(uAux != null) {
				udao.modify(u);
				return Response.ok().entity(u).build();
			}else {
				return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
			}
		}
		
	}
	
	@DELETE
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id) {
		Usuario uAux = udao.get(id);
		if(uAux != null){
			udao.delete(uAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro el usuario";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}	
	
	@POST
	@Path("participantes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response set(Participante p) {
		pdao.set(p);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Path("participantes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Participante p) {
		Participante pAux=pdao.get(p.getId());
		if(pAux != null) {
			udao.modify(p);
			return Response.ok().entity(p).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@DELETE
	@Secured
	@RolesAllowed({"Admin"})
	@Path("participantes/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteParticipante(@PathParam("id") Integer id) {
		Participante pAux = pdao.get(id);
		if(pAux != null){
			pdao.delete(pAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro el usuario";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}	
	
	
	
}
