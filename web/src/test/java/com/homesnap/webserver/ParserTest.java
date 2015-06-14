package com.homesnap.webserver;


/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import org.junit.Assert;
import org.junit.Test;

import com.homesnap.webserver.rest.house.MissingParameterRestOperation;
import com.homesnap.webserver.rest.house.RestOperationException;
import com.homesnap.webserver.rest.house.UnsupportedRestOperation;
import com.homesnap.webserver.rest.house.parser.ParseException;
import com.homesnap.webserver.rest.house.parser.UriParser;


public class ParserTest {
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
	
	@Test
	public void house() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//		/house																			GET/DELETE
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house?param=param";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}
	
	@Test
	public void labelList() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//		/house																			GET/DELETE
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house/labels";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}
	
	@Test
	public void label() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//	/house/labels/labelId || /house/labels/label?id=id								GET/PUT/POST/DELETE
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house/labels/ch1";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/labels/label?id=ch1";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}

	@Test
	public void controllerFromLabel() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//	/house/labels/labelId/where || /house/labels/labelId/controller?id=id			GET/PUT/POST/DELETE
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house/labels/ch1/controller/61";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/labels/ch1/controller?id=61";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/labels/ch1/61";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}

	@Test
	public void groupList() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//		/house/groups																	GET
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house/groups";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}
	
	@Test
	public void group() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//		/house/groups/groupId || /house/groups/group?id=id								GET
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house/groups/10";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/groups/group?id=10&status=valeur2";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}
	
	@Test 
	public void controllerFromGroup() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//		/house/groups/groupId/where || /house/groups/groupId/controller?id=id			GET
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house/groups/10/controller/12";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/groups/10/controller?id=12";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/groups/10/12";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}

	@Test
	public void controller() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
//		/controllers/where || /controllers/controller?id=id
		ListenerExampleParser listener = new ListenerExampleParser();
		String uri = "/house/controllers/12";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/controllers/controller?id=12";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);

		listener = new ListenerExampleParser();
		uri = "/house/controllers/controller/12";
		UriParser.parse(uri, listener);
		Assert.assertTrue(listener.success);
	}

	
	@Test
	public void status() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
		// Done by servlet and not by parser => no test with parser
	}
}
