package com.homesnap.webserver.servlet.house;

/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by A. de Giuli (arnaud.degiuli(at)free.fr).
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

import com.homesnap.engine.house.House;
import com.homesnap.webserver.rest.house.MissingParameterRestOperation;
import com.homesnap.webserver.rest.house.RestOperationException;
import com.homesnap.webserver.rest.house.UnsupportedRestOperation;
import com.homesnap.webserver.rest.house.listener.MyDomoDeleteListener;
import com.homesnap.webserver.rest.house.listener.MyDomoGetListener;
import com.homesnap.webserver.rest.house.listener.MyDomoPostListener;
import com.homesnap.webserver.rest.house.listener.MyDomoPutListener;
import com.homesnap.webserver.rest.house.parser.ParseException;
import com.homesnap.webserver.rest.house.parser.UriParser;


public class HouseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	HouseServiceImpl service = new HouseServiceImpl();

	public void init(ServletConfig config) throws ServletException {
		System.err.println("Initializing the HouseServlet");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = getUri(req);
		System.err.println("DELETE :[" + uri + "]");
		try {
			House house = service.readHouse();
			MyDomoDeleteListener listener = new MyDomoDeleteListener(house, uri, req.getParameterMap());
			UriParser.parse(uri, listener);
			service.saveHouse(house);
			if (listener.getResult() == null || listener.getResult() == "") {
				resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
			} else {
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			resp.getWriter().write(listener.getResult());
		} catch (ParseException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (UnsupportedRestOperation e) {
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		} catch (RestOperationException e) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (MissingParameterRestOperation e) {
			resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
		} catch (Error e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.out.print("Error on that URI: [" + uri + "]");
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = getUri(req);
		System.err.println("POST :[" + uri + "]");
		try {
			House house = service.readHouse();
			BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
			StringBuilder sb = new StringBuilder("");
			String str = b.readLine();
			while (str != null) {
				sb.append(str);
				str = b.readLine();
			}
			MyDomoPostListener listener = new MyDomoPostListener(house, uri, req.getParameterMap(), sb.toString());
			resp.setContentType("application/json");
			UriParser.parse(uri, listener);
			service.saveHouse(house);
			resp.setHeader("Location", uri);
			resp.setStatus(HttpServletResponse.SC_CREATED);
			resp.getWriter().write(listener.getResult());
		} catch (ParseException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (UnsupportedRestOperation e) {
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		} catch (RestOperationException e) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (MissingParameterRestOperation e) {
			resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
		} catch (Error e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.out.print("Error on that URI: [" + uri + "]");
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = getUri(req);
		System.err.println("GET :[" + uri + "]");
		try {
			House house = service.readHouse();
			MyDomoGetListener listener = new MyDomoGetListener(house, uri, req.getParameterMap());
			resp.setContentType("application/json");
			UriParser.parse(uri, listener);
			resp.getWriter().write(listener.getResult());
		} catch (ParseException e) {
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (UnsupportedRestOperation e) {
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		} catch (RestOperationException e) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (MissingParameterRestOperation e) {
			resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
		} catch (Error e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.out.print("Error on that URI: [" + uri + "]");
			e.printStackTrace();
		}
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = getUri(req);
		System.err.println("PUT :[" + uri + "]");
		try {
			House house = service.readHouse();
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
			e.printStackTrace();
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} catch (UnsupportedRestOperation e) {
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		} catch (RestOperationException e) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (MissingParameterRestOperation e) {
			resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
		} catch (Error e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.out.print("Error on that URI: [" + uri + "]");
			e.printStackTrace();
		}
	}
	
	protected String getUri(HttpServletRequest req) {
		return req.getQueryString() == null ? req.getRequestURI() : req.getRequestURI() +"?" + req.getQueryString(); // getQueryString()getPathInfo() == null ? "/house" : "/house" + req.getPathInfo();
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
