package com.adgsoftware.mydomo.webserver.servlet.house;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.webserver.rest.MissingParameterRestOperation;
import com.adgsoftware.mydomo.webserver.rest.RestOperationException;
import com.adgsoftware.mydomo.webserver.rest.UnsupportedRestOperation;
import com.adgsoftware.mydomo.webserver.rest.listener.MyDomoGetListener;
import com.adgsoftware.mydomo.webserver.rest.listener.MyDomoPutListener;
import com.adgsoftware.mydomo.webserver.servlet.house.parser.ParseException;
import com.adgsoftware.mydomo.webserver.servlet.house.parser.UriParser;
import com.adgsoftware.mydomo.webserver.utils.JSonTools;


public class HouseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HouseServiceImpl service = new HouseServiceImpl();

	public void init(ServletConfig config) throws ServletException {
		System.err.println("Initializing the HouseServlet");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO GET HOUSE!");

		try {
			House house = service.readHouse();
			String uri = getUri(req);
			MyDomoGetListener listener = new MyDomoGetListener(house, uri, req.getParameterMap());
			resp.setContentType("application/json");
			UriParser.parse(uri, listener);
			resp.getWriter().write(listener.getResult());
		} catch (ParseException e) {
			e.printStackTrace();
			resp.getWriter().write(MyDomoGetListener.REST_API);
		} catch (UnsupportedRestOperation e) {
			resp.getWriter().write(JSonTools.toJson(e));
		} catch (RestOperationException e) {
			resp.getWriter().write(JSonTools.toJson(e));
		} catch (MissingParameterRestOperation e) {
			resp.getWriter().write(JSonTools.toJson(e));
		}
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("DO PUT HOUSE!");

		try {
			House house = service.readHouse();
			String uri = getUri(req);
			
			BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
			StringBuilder sb = new StringBuilder("");
			String str = b.readLine();
			while (str != null) {
				sb.append(str);
				str = b.readLine();
			}
			MyDomoPutListener listener = new MyDomoPutListener(house, uri, req.getParameterMap(), sb.toString());
			resp.setContentType("application/json");
			UriParser.parse(uri, listener);
			service.saveHouse(house);
			resp.getWriter().write(listener.getResult());
		} catch (ParseException e) {
			resp.getWriter().write(MyDomoGetListener.REST_API);
		} catch (UnsupportedRestOperation e) {
			resp.getWriter().write(JSonTools.toJson(e));
		} catch (RestOperationException e) {
			resp.getWriter().write(JSonTools.toJson(e));
		} catch (MissingParameterRestOperation e) {
			resp.getWriter().write(JSonTools.toJson(e));
		}
	}
	
	protected String getUri(HttpServletRequest req) {
		return req.getPathInfo() == null ? "/house" : "/house" + req.getPathInfo();
	}
}



/*
CONTROLLER
[/house/labels/labelId/where || /house/groups/groupId/where || /controllers/where]

Light:
/[on|off] || [?what=on||?what=off]

Automation:
/[up|down|stop] || [?what=up||?what=down||?what=stop]

heating:
/[heating_on||heating_off||...status]/dimension?value || [?what=heating_on||heating_off|| ...] => seule la premiere permet de régler les dimensions!

HOUSE SERVLER

/house																			GET/DELETE
/house/labels																	GET/DELETE
/house/labels/labelId || /house/labels/label?id=id								GET/PUT/POST/DELETE
/house/labels/labelId/where || /house/labels/labelId/controller?id=id			GET/PUT/POST/DELETE
/house/groups																	GET
/house/groups/groupId || /house/groups/group?id=id								GET
/house/groups/groupId/where || /house/groups/groupId/controller?id=id			GET


GET
/house
/house/labels
/house/labels/labelId || /house/labels/label?id=id
/house/labels/labelId/where || /house/labels/labelId/controller?id=id

/house/groups
/house/groups/groupId
/house/groups/groupId/where

POST
/house/labels/labelId (title=Titre)
/house/labels/labelId/where (who=1)


PUT
/house/labels/labelId?title=Titre
/house/labels/labelId/where?who=1

DELETE
/house
/house/labels
/house/labels/labelId || /house/labels/label?id=id
/house/labels/labelId/where || /house/labels/labelId/controller?id=id
*/