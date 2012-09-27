package com.adgsoftware.mydomo.server;

import com.adgsoftware.mydomo.engine.parser.CommandParser;
import com.adgsoftware.mydomo.engine.parser.ParseException;

public class ParserTest {
	public static void main(String[] args) throws ParseException {
		String dimensionCommand = "*#13*22*#1*02*11*05*2012##";
		String dimensionStatus = "*#13*22*0##";
		CommandParser p  = CommandParser.parse(dimensionCommand);
		System.out.println(p.getDimension());
		
		p  = CommandParser.parse(dimensionStatus);
		System.out.println(p.getDimension());
	}
}
