package mydomowebserver;

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

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mydomowebserver.utils.URITools;

import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
 
/** A simple servlet */
public class LightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	LightRestService service = new LightRestServiceImpl();
 
	public void init(ServletConfig config) throws ServletException {
		System.err.println("Initializing the LightServlet");
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO GET!");
		// /adress or /adress
		resp.setContentType("application/json");
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length == 1) {
			String adress = pathInfo[0];
			LightStatus status = service.status(adress);
			String strStatus = (status == null ? "null" : LightStatus.LIGHT_ON == status ? "on" : "off");
			resp.getWriter().write("{\"adress\":\""+adress+"\",\"status\":\""+ strStatus +"\"}");
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress");
		}
	}
	

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO POST!");
		// /adress or /adress
		resp.setContentType("application/json");
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length == 1) {
			resp.getWriter().write(formatJson(service.createLight(pathInfo[0])));
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress");
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO PUT!");
		long start = System.currentTimeMillis();
		resp.setContentType(" application/json");
		// /adress/on or /adress/off
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length == 2) {
			String adress = pathInfo[0];
			String status = pathInfo[1];

			LightStatus result = null;
			if ("on".equalsIgnoreCase(status)) {
				result = service.command(LightStatus.LIGHT_ON, adress);
			} else if ("off".equalsIgnoreCase(status)) {
				result = service.command(LightStatus.LIGHT_OFF, adress);
			} else {
				resp.getWriter().write("{error: invalid command}");
				return;
			}
			String strStatus = (result == null ? "null" : LightStatus.LIGHT_ON == result ? "on" : "off");
			resp.getWriter().write("{\"adress\":\""+adress+"\", \"status\":\""+ strStatus +"\"}");
		} else if (pathInfo != null && pathInfo.length == 1) {
			// /adress or /adress
			String json ="";
			Light light = parseJson(json);
			service.saveLight(light);
		} else {
		
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress[/on|/off]");
		}
		
		System.err.println("Time:" + (System.currentTimeMillis() - start));
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		// /adress or /adress
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length == 1) {
			String adress = pathInfo[0];
			if (service.deleteLight(adress)) {
				resp.getWriter().write("{\"success\":\"true\"}");	
			} else {
				resp.getWriter().write("{\"sucess\":\"true\", error\":\"Impossible to delete\"}");
			}
			
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress");
		}
	}
	
	private Light parseJson(String json) {
		return new Light(); // TODO not create ...
	}
	
	private String formatJson(Light light) {
		String strStatus = (light.getWhat() == null ? "null" : LightStatus.LIGHT_ON == light.getWhat() ? "on" : "off");
		return "{\"adress\":\""+light.getWhere()+"\",\"status\":\""+ strStatus +"\"}";
	}
}
