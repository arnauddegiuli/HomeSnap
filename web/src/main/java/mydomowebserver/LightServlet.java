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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
 
/** A simple servlet */
public class LightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	LightRestService service = new LightRestServiceImpl();
 
	public void init(ServletConfig config) throws ServletException {
		System.err.println("Initializing the servlet");;
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO GET!");
		// /adress/on or /adress/off
		String adress = req.getPathInfo();
		if (adress != null) {
			adress = adress.substring(1);
			LightStatus status;
			if (!req.getPathInfo().contains("on") && !req.getPathInfo().contains("off")) { // /adress
				status = service.status(adress);
			} else { // /adress/on or /adress/off
				String newStatus = req.getPathInfo();
				newStatus = "LIGHT_ON";
				adress ="12";
				status = service.command(LightStatus.valueOf(newStatus), adress);
			}
			
			resp.getWriter().write("{adress:"+adress+",status:"+ (status == null ? "null" : status.name())+"}");
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress[/on|/off]");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}
}
