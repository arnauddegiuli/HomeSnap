package com.homesnap.webserver.servlet.recorder;

/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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

import com.homesnap.recorder.RecorderService;
import com.homesnap.recorder.RecorderServiceImpl;
import com.homesnap.webserver.rest.recorder.MissingParameterRestOperation;
import com.homesnap.webserver.rest.recorder.RestOperationException;
import com.homesnap.webserver.rest.recorder.UnsupportedRestOperation;
import com.homesnap.webserver.rest.recorder.listener.RecorderDeleteListener;
import com.homesnap.webserver.rest.recorder.listener.RecorderGetListener;
import com.homesnap.webserver.rest.recorder.listener.RecorderPostListener;
import com.homesnap.webserver.rest.recorder.listener.RecorderPutListener;
import com.homesnap.webserver.rest.recorder.parser.ParseException;
import com.homesnap.webserver.rest.recorder.parser.UriParser;


public class RecorderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	RecorderService service;
	
	public void init(ServletConfig config) throws ServletException {
		System.err.println("Initializing the RecorderServlet");
		service = new RecorderServiceImpl("192.168.1.35", 20000, 12345);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String uri = getUri(req);
		System.err.println("DELETE :[" + uri + "]");
		try {
			RecorderDeleteListener listener = new RecorderDeleteListener(service, uri, req.getParameterMap());
			UriParser.parse(uri, listener);
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
			BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
			StringBuilder sb = new StringBuilder("");
			String str = b.readLine();
			while (str != null) {
				sb.append(str);
				str = b.readLine();
			}
			RecorderPostListener listener = new RecorderPostListener(service, uri, req.getParameterMap());
			resp.setContentType("application/json");
			UriParser.parse(uri, listener);
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
			RecorderGetListener listener = new RecorderGetListener(service, uri, req.getParameterMap());
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
			BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
			StringBuilder sb = new StringBuilder("");
			String str = b.readLine();
			while (str != null) {
				sb.append(str);
				str = b.readLine();
			}
			RecorderPutListener listener = new RecorderPutListener(service, uri, req.getParameterMap(), sb.toString());
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
	
	protected String getUri(HttpServletRequest req) {
		return req.getQueryString() == null ? req.getRequestURI() : req.getRequestURI() +"?" + req.getQueryString(); // getQueryString()getPathInfo() == null ? "/house" : "/house" + req.getPathInfo();
	}
}
