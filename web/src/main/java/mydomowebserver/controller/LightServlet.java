package mydomowebserver.controller;

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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mydomowebserver.utils.JSonTools;
import mydomowebserver.utils.URITools;

import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
 
/** A simple servlet */
// /light
public class LightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	LightRestService service = new LightRestServiceImpl();
 
	public void init(ServletConfig config) throws ServletException {
		System.err.println("Initializing the LightServlet");
	}
	
	// /adress
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO GET!");
		resp.setContentType("application/json");
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length > 1) {
			String adress = pathInfo[0];
			Light light = service.getStatus(adress);
			resp.getWriter().write(JSonTools.formatJson(light));
		} else if (pathInfo != null && pathInfo.length == 0) {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress");
		}
	}

	// /adress
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO POST!");
		resp.setContentType("application/json");
		
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length == 1) {
			
			resp.getWriter().write(JSonTools.formatJson(service.putLight(pathInfo[0], "")));
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress");
		}
	}
	
	// /adress/on or /adress/off
	// /adress?title=deviceTitle
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO PUT!");
		long start = System.currentTimeMillis();
		resp.setContentType(" application/json");
		
		String[] pathInfo = URITools.split(req.getPathInfo());
		String title = req.getParameter("title");
		if (pathInfo != null && pathInfo.length > 0) {
			String adress = pathInfo[0];
			Light result = null;
			
			if (pathInfo.length == 2) {
				// /adress/on or /adress/off
				String status = pathInfo[1];
				
				if ("on".equalsIgnoreCase(status)) {
					result = service.putCommand(adress, LightStatus.LIGHT_ON);
				} else if ("off".equalsIgnoreCase(status)) {
					result = service.putCommand(adress, LightStatus.LIGHT_OFF);
				} else {
					resp.getWriter().write("{error: invalid command}");
					return;
				}
				
			} else if (pathInfo.length == 1) {
				// /adress or /adress?title=Titre
				result = service.putLight(adress, title);
			} else {
				resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress[/on|/off][?title=deviceTitle]");
				return;
			}
			
			if (title != null && pathInfo.length != 1) {
				// /*?title=Titre 
				result = service.putLight(adress, title);
			}
			
			resp.getWriter().write(JSonTools.formatJson(result));
		}
		
		System.err.println("Time:" + (System.currentTimeMillis() - start));
	}
	
	// /adress 
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO DEL!");
		resp.setContentType("application/json");
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length > 1) {
			String adress = pathInfo[0];
			if (service.delLight(adress)) {
				resp.getWriter().write("{\"success\":\"true\"}");	
			} else {
				resp.getWriter().write("{\"sucess\":\"true\", error\":\"Impossible to delete [" + adress + "]\"}");
			}
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress");
		}
	}
}
