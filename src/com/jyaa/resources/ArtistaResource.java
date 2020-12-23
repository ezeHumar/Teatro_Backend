package com.jyaa.resources;

import java.time.LocalDate;
import java.util.ArrayList;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.jyaa.dao.ArtistaDAO;
import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.modelo.Artista;
import com.jyaa.modelo.Participante;
import com.jyaa.security.Secured;

@Path("/artistas")
public class ArtistaResource {

	public ArtistaResource() {
		// TODO Auto-generated constructor stub
	}
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Context
	SecurityContext securityContext;
	
	@Inject
	ParticipanteDAO pdao;
	
	@Inject
	ArtistaDAO adao;
	
	private String msj;
	
	@GET
	@Secured
	@RolesAllowed({"Admin", "Participante"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<Artista> getArtistasFiltered(@QueryParam("nombre")String nombre,@QueryParam("apellido") String apellido,@QueryParam("idTags") List<String> idTags,@QueryParam("idObras") List<String> idObras){
		return adao.getListFiltered(nombre, apellido, idTags, idObras);
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Artista a= adao.get(id);
		if(a != null) {
			return Response
					.ok()
					.entity(a)
					.build();					
		}
		else {
			msj="No se encontro el artista";
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
	public Response set(Artista a) {
		if(a.getApellido()==null || a.getNombre()==null || a.getFechaNacimiento()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(a.getFechaNacimiento().isAfter(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de nacimento no puede ser una fecha futura").build();
		}
		adao.set(a);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Artista a) {
		if(a.getApellido()==null || a.getNombre()==null || a.getFechaNacimiento()==null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		if(a.getFechaNacimiento().isAfter(LocalDate.now())){
			return Response.status(Response.Status.BAD_REQUEST).entity("La fecha de nacimento no puede ser una fecha futura").build();
		}
		Artista aAux=adao.get(a.getId());
		if(aAux != null) {
			adao.modify(a);
			return Response.ok().entity(a).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@DELETE
	@Secured
	@RolesAllowed({"Admin"})
	@Path("{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id) {
		Artista aAux = adao.get(id);
		if(aAux != null){
			adao.delete(aAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro el artista";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Participante"})
	@Path("{id}/meInteresa")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response interesObra(@PathParam("id") long id) {
		
		Participante pAux=(Participante) pdao.getByEmail(securityContext.getUserPrincipal().getName());
		
		Artista aAux=(Artista) adao.get(id);
		if(aAux==null) {
			return Response.status(Response.Status.NOT_FOUND).entity("El artista no existe").build();
		}
		pAux.getInteresesArtistas().size();
		List<Artista> lArtistas=pAux.getInteresesArtistas();
		if (lArtistas==null) {
			lArtistas=new ArrayList<>();
		}
		if(lArtistas.contains(aAux)) {
			return Response.status(Response.Status.CONFLICT).entity("El artista ya se encuentra en la lista de intereses").build();
		}
		lArtistas.add(aAux);
		pAux.setInteresesArtistas(lArtistas);
		
		if(pAux != null) {
			pdao.modify(pAux);
			return Response.ok().entity(pAux).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
		}
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin"})
	@Path("/noborrables")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Long> getTagsNoBorrables(){
		return adao.getListNoBorrable();
	}

}
