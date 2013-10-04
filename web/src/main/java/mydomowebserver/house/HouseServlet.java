package mydomowebserver.house;

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

import com.adgsoftware.mydomo.engine.house.Label;

import mydomowebserver.utils.JSonTools;
import mydomowebserver.utils.URITools;

public class HouseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	HouseRestService service = new HouseRestServiceImpl();
	
	public void init(ServletConfig config) throws ServletException {
		System.err.println("Initializing the HouseServlet");
	}	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO GET HOUSE!");
		// /house
		resp.setContentType("application/json");
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length == 0) {
			resp.getWriter().write(JSonTools.toJson(service.getHouse()));
		} else if (pathInfo != null && pathInfo.length == 1) {

			for (Label label : service.getHouse().getLabels()) {
				if (pathInfo[0].equals(label.getId())) {
					resp.getWriter().write(JSonTools.toJsStringLabel(label));
					return;
				}
			}
			resp.getWriter().write("error"); // TODO how throw exception
			
		} else {
			resp.getWriter().write("Usage: http[s]://server:port/house[/labelid]");
		}
	}
	
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO PUT HOUSE!");
		// /house
		resp.setContentType("application/json");
		String[] pathInfo = URITools.split(req.getPathInfo());
		if (pathInfo != null && pathInfo.length == 1) {
			String label = pathInfo[0];
			String title = req.getParameter("title");
			service.putLabel(label, title);
		} else if (pathInfo != null && pathInfo.length == 2) {
			String label = pathInfo[0];
			String adress = pathInfo[1];
			String what = req.getParameter("what");
			service.putController(label, adress, what);
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/house/label?title=Titre\n" +
					               "                           http[s]://server:port/house/label/adresse?what=light");
		}
	}
	
	
}
