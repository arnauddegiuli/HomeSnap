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

import mydomowebserver.utils.JSonTools;
import mydomowebserver.utils.URITools;

import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;

public class HouseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
			
			resp.getWriter().write(JSonTools.toJson(getModel()));
		} else {
			resp.getWriter().write("No adress provided. Usage: http[s]://server:port/light/adress");
		}
	}
	
	
	
	private House getModel() {
		House house = new House();
		
		Label label = new Label();
		label.setId("l1");
		label.setTitle("Label 1");
		house.getLabels().add(label);
		
		Light li = new Light();
		li.setTitle("toto");
		li.setWhere("12");
		label.add(li);
			
		Light li2 = new Light();
		li2.setTitle("Light 2");
		li2.setWhere("13");
		label.add(li2);
		
		label = new Label();
		label.setId("l2");
		label.setTitle("Label 2");
		house.getLabels().add(label);

		
		return house;
	}
}
