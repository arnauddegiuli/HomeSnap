//package org.eclipse.jetty.example.webapp;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.SecurityContext;
//
//@Path("lights/{where}")
//public class LightRestService {
//
//    @Context
//    private SecurityContext security;
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String status(@PathParam("where") String where) {
//        return "ok";	
//    }
//    
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("on")
//    public String on(String where){
//    	return where;
//    }
//
//    
//    public String on(String where, long timing){
//    	return where;
//    }
//
//    
//    public String on(String where, int intensity) {
//    	return null;
//    }
//    
//    public String on(String where, int intensity, long timing) {
//    	return null;
//    }
//    
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    public String off(String where){
//    	return where;
//    }
//    
//    public String off(String where, long timing) {
//    	return null;
//    }
//}