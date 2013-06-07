//package org.eclipse.jetty.example.webapp;

/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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