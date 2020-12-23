package com.jyaa.resources;

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

import com.jyaa.dao.ObraDAO;
import com.jyaa.dao.ParticipanteDAO;
import com.jyaa.modelo.Obra;
import com.jyaa.modelo.Participante;
import com.jyaa.security.Secured;

@Path("/obras")
public class ObraResource {

	public ObraResource() {
		// TODO Auto-generated constructor stub
	}
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@Context
	SecurityContext securityContext;
	
	@Inject
	ObraDAO odao;
	
	@Inject
	ParticipanteDAO pdao;
	
	private String msj;
	
	
	@GET
	@Secured
	@RolesAllowed({"Admin", "Participante"})
	@Produces(MediaType.APPLICATION_JSON)
	public List<Obra> getObrasFiltradas(@QueryParam("idArtistas") List<String> idArtistas,@QueryParam("idTags") List<String> idTags, @QueryParam("nombre") String nombre){
		return odao.getListFiltered(idArtistas, idTags, nombre);
	}
	
	@GET
	@Secured
	@RolesAllowed({"Admin", "Participante"})
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		Obra o= odao.get(id);
		if(o != null) {
			return Response
					.ok()
					.entity(o)
					.build();					
		}
		else {
			msj="No se encontro la obra";
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
	public Response set(Obra o) {
		if(o.getNombre()==null || o.getDescripcion()==null || o.getDuracion()==-1 || o.getArtistaParticipante().isEmpty() || o.getTags().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		odao.set(o);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Secured
	@RolesAllowed({"Admin"})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response modify(Obra o) {
		if(o.getNombre()==null || o.getDescripcion()==null || o.getDuracion()==-1 || o.getArtistaParticipante().isEmpty() || o.getTags().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Todos los campos deben estar completos").build();
		}
		Obra oAux=odao.get(o.getId());
		if(oAux != null) {
			odao.modify(o);
			return Response.ok().entity(o).build();
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
		Obra oAux = odao.get(id);
		if(oAux != null){
			odao.delete(oAux.getId());
			return Response.noContent().build();
		} else {
			msj="No se encontro la obra";
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity(msj)
					.build();
		}
	}	
	
	@DELETE
	@Secured
	@RolesAllowed({"Participante"})
	@Path("/noMeInteresa")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response noInteresObra() {
		Participante pAux=(Participante) pdao.get(1);
		pAux.getInteresesObras().size();
		List<Obra> lObras=pAux.getInteresesObras();
		lObras.removeAll(lObras);
		pAux.setInteresesObras(lObras);
		
		if(pAux != null) {
			pdao.modify(pAux);
			return Response.ok().entity(pAux).build();
		}else {
			return Response.status(Response.Status.NOT_FOUND).entity("[]").build();
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
		Obra oAux=(Obra) odao.get(id);
		
		pAux.getInteresesObras().size();
		List<Obra> lObras=pAux.getInteresesObras();
		if (lObras==null) {
			lObras=new ArrayList<>();
		}
		lObras.add(oAux);
		pAux.setInteresesObras(lObras);
		
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
		return odao.getListNoBorrable();
	}
}
