package com.adgsoftware.mydomo.engine.test;

import junit.framework.Assert;

import org.junit.Test;

import com.adgsoftware.mydomo.engine.parser.CommandParser;
import com.adgsoftware.mydomo.engine.parser.ParseException;

public class ParserTest {
	
	@Test
	public void whoTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12##";
		CommandParser p  = CommandParser.parse(standard);
		System.out.println(p.getWho()); // Should display 1
		Assert.assertEquals(p.getWho(), "1");
		
		// Space case..
		String gatewayStatusCommand = "*#13**0##";
		p  = CommandParser.parse(gatewayStatusCommand);
		System.out.println(p.getWho()); // Should display 13
		Assert.assertEquals(p.getWho(), "13");

	}
	
	@Test
	public void whereTest() throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12##";
		CommandParser p  = CommandParser.parse(standard);
		System.out.println(p.getWhere()); // Should display 12
		Assert.assertEquals(p.getWhere(), "12");
	}
	
	@Test
	public void statusTest() throws ParseException {
		// Status request    *#WHO*WHERE##
		String status = "*#1*12##";
		CommandParser p  = CommandParser.parse(status);
		System.out.println(p.getWhere()); // Should display 12;
		Assert.assertEquals(p.getWhere(), "12");
	}
	
	@Test
	public void dimensionRequestTest() throws ParseException {
		// Dimension request *#WHO*WHERE*DIMENSION##tring dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		String dimensionStatus = "*#12*22*0##";
		CommandParser p  = CommandParser.parse(dimensionStatus);
		System.out.println(p.getDimension()); // Should display 0
		Assert.assertEquals(p.getDimension(), "0");
	}
	
	@Test
	public void dimensionWriteTest() throws ParseException {
		// Dimension write   *#WHO*WHERE*#DIMENSION*VAL1*VAL2*...*VALn##
		String dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		CommandParser p  = CommandParser.parse(dimensionCommand);
		System.out.println(p.getDimension()); // Should display 1
		Assert.assertEquals(p.getDimension(), "1");
	}
}
