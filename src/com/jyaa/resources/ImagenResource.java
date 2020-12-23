package com.jyaa.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/upload")
public class ImagenResource {
	
	private static final String UPLOAD_FOLDER = "jyaa2020_grupo13_images/";
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	
	public ImagenResource() {
		// TODO Auto-generated constructor stub
	}
	
	@POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
		System.out.println("Subiendo");
        // check if all form parameters are provided
        if (uploadedInputStream == null || fileDetail == null)
            return Response.status(400).entity("Invalid form data").build();
        // create our destination folder, if it not exists
        try {
            createFolderIfNotExists(UPLOAD_FOLDER);
        } catch (SecurityException se) {
            return Response.status(500)
                    .entity("Can not create destination folder on server")
                    .build();
        }
        String nombreUnicoArchivo = getUniqueFileName(fileDetail.getFileName());
        String uploadedFileLocation = UPLOAD_FOLDER + nombreUnicoArchivo;
        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);
        } catch (IOException e) {
            return Response.status(500).entity("Can not save file").build();
        }
        return Response.status(200)
                .entity(nombreUnicoArchivo).build();
    }
    /**
     * Utility method to save InputStream data to target location/file
     * 
     * @param inStream
     *            - InputStream to be saved
     * @param target
     *            - full path to destination file
     */
    private void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }
    /**
     * Creates a folder to desired location if it not already exists
     * 
     * @param dirName
     *            - full path to the folder
     * @throws SecurityException
     *             - in case you don't have permission to create the folder
     */
    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
    
    private String getUniqueFileName(String fileName) {
        int num = 0;
        final String ext = getFileExtension(fileName);
        final String name = getFileName(fileName);
        File file = new File(UPLOAD_FOLDER + fileName);
        while (file.exists()) {
            num++;
            file = new File(UPLOAD_FOLDER + java.util.UUID.randomUUID() + ext);
        }
        return file.getName();
   }
    
    public static String getFileExtension(final String path) {
        if (path != null && path.lastIndexOf('.') != -1) {
            return path.substring(path.lastIndexOf('.'));
        }
        return null;
   }


   public static String getFileName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
   }
    
    @GET
    @Produces("image/jpg")
    @Path("{nombreArchivo}")
    public Response getFile(@PathParam("nombreArchivo") String nombreArchivo) {
      File file = new File(UPLOAD_FOLDER + nombreArchivo);
      return Response.ok(file, "image/jpg")
          .header("Content-Disposition", "Inline; filename=\"" + file.getName() + "\"" ) //optional
          .build();
    }
    
    @DELETE
    @Path("{nombreArchivo}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("nombreArchivo") String nombreArchivo) {
    	System.out.println("Borrando");
		File archivo = new File(UPLOAD_FOLDER + nombreArchivo);
		
		if (archivo.delete()) { 
			System.out.println("Borrado");
			return Response.ok()
					.entity("Archivo borrado: " + archivo.getName())
					.build();
		} else {
			System.out.println("No Borrado");
			return Response
					.status(Response.Status.NOT_FOUND)
					.entity("No se encontro el archivo.")
					.build();
		} 
    	
    }
}
