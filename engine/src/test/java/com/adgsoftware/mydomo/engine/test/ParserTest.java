package com.adgsoftware.mydomo.engine.test;

/*
 * #%L
 * MyDomoEngine
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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.adgsoftware.mydomo.engine.connector.openwebnet.Command;
import com.adgsoftware.mydomo.engine.connector.openwebnet.WhereType;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.CommandParser;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;
import com.adgsoftware.mydomo.engine.controller.DimensionValue;

public class ParserTest {
	
	@Test
	public void whoTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("1", p.getWho());
		
		// Space case..
		String gatewayStatusCommand = "*#13**0##";
		p  = CommandParser.parse(gatewayStatusCommand);
		Assert.assertEquals("13", p.getWho());

	}

	@Test
	public void whereGeneralTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*0##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("0", p.getWhere());
		Assert.assertEquals(WhereType.GENERAL, p.getWhereType());
	}

	@Test
	public void whereAmbianceTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*1##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("1", p.getWhere());
		Assert.assertEquals( WhereType.ENVIRONMENT, p.getWhereType());
		Assert.assertEquals("1", p.getEnvironment());
	}

	@Test
	public void whereGroupTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*#1##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("#1", p.getWhere());
		Assert.assertEquals("1", p.getGroup());
		Assert.assertEquals(WhereType.GROUP, p.getWhereType());
	}	

	@Test
	public void wherePointToPointTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("12", p.getWhere());
		Assert.assertEquals("12", p.getPoint());
		Assert.assertEquals(WhereType.POINTTOPOINT, p.getWhereType());
	}

	@Test
	public void whereGeneralOnBusTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*0#4#2##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("0#4#2", p.getWhere());
		Assert.assertEquals(WhereType.GENERALONLOCALBUS, p.getWhereType());
		Assert.assertEquals("2", p.getBus());
	}

	@Test
	public void whereAmbianceOnBusTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*1#4#2##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("1#4#2", p.getWhere());
		Assert.assertEquals("1", p.getEnvironment());
		Assert.assertEquals(WhereType.ENVIRONMENTONLOCALBUS, p.getWhereType());
		Assert.assertEquals("2", p.getBus());
	}

	@Test
	public void whereGroupOnBusTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*#1#4#2##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals("#1#4#2", p.getWhere());
		Assert.assertEquals("1", p.getGroup());
		Assert.assertEquals("2", p.getBus());
		Assert.assertEquals(WhereType.GROUPONLOCALBUS, p.getWhereType());
	}

	@Test
	public void wherePointToPointOnBusTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12#4#2##";
		CommandParser p  = CommandParser.parse(standard);
		Assert.assertEquals(p.getWhere(), "12#4#2");
		Assert.assertEquals("12", p.getPoint());
		Assert.assertEquals(WhereType.POINTTOPOINT, p.getWhereType());
		Assert.assertEquals("2", p.getBus());
	}



	@Test
	public void statusTest() throws ParseException {
		// Status request    *#WHO*WHERE##
		String status = "*#1*12##";
		CommandParser p  = CommandParser.parse(status);
		Assert.assertEquals(p.getWhere(), "12");
	}
	
	@Test
	public void dimensionRequestTest() throws ParseException {
		// Dimension request *#WHO*WHERE*DIMENSION##tring dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		String dimensionStatus = "*#12*22*0##";
		CommandParser p  = CommandParser.parse(dimensionStatus);
		Assert.assertEquals(p.getDimension(), "0");
	}
	
	@Test
	public void dimensionWriteTest() throws ParseException {
		// Dimension write   *#WHO*WHERE*#DIMENSION*VAL1*VAL2*...*VALn##
		String dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		CommandParser p  = CommandParser.parse(dimensionCommand);
		Assert.assertEquals(p.getDimension(), "1");
		Assert.assertEquals(p.getDimensionList().size(), 4);
		Assert.assertEquals(p.getDimensionList().get(0), "02");
		Assert.assertEquals(p.getDimensionList().get(3), "2012");
	}
	
	@Test
	public void dimensionReadListTest() throws ParseException {
		// Dimension read *#WHO*WHERE*DIMENSION*VAL1*VAL2*...*VALn##
		List<DimensionValue> l = Command.getCommandAnalyser("*#4*6*0*0226##").getDimensionListFromCommand();
		Assert.assertEquals(l.size(), 1);
		Assert.assertEquals(l.get(0).getValue(), "0226");
	}
	
}
