package com.adgsoftware.mydomo.server;

import com.adgsoftware.mydomo.engine.parser.CommandParser;
import com.adgsoftware.mydomo.engine.parser.ParseException;

public class ParserTest {
	public static void main(String[] args) throws ParseException {
		// Standard          *WHO*WHAT*WHERE##
		String standard = "*1*1*12##";
		CommandParser p  = CommandParser.parse(standard);
		System.out.println(p.getWho()); // Should display 1
		
		
		// Status request    *#WHO*WHERE##
		String status = "*#1*12##";
		p  = CommandParser.parse(status);
		System.out.println(p.getWhere()); // Should display 12;
		
		
		// Dimension request *#WHO*WHERE*DIMENSION##tring dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		String dimensionStatus = "*#12*22*0##";
		p  = CommandParser.parse(dimensionStatus);
		System.out.println(p.getDimension()); // Should display 0
		
		
		// Dimension write   *#WHO*WHERE*#DIMENSION*VAL1*VAL2*...*VALn##
		String dimensionCommand = "*#12*1*#1*02*11*05*2012##";
		p  = CommandParser.parse(dimensionCommand);
		System.out.println(p.getDimension()); // Should display 1
		
		
		// Space case..
		String gatewayStatusCommand = "*#13**0##";
		p  = CommandParser.parse(gatewayStatusCommand);
		System.out.println(p.getWho()); // Should display 13
	}
}
