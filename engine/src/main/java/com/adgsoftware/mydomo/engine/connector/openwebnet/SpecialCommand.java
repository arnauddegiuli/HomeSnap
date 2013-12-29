package com.adgsoftware.mydomo.engine.connector.openwebnet;

import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.CommandParser;

public class SpecialCommand {

	private CommandParser parser;

	protected SpecialCommand(CommandParser parser) {
		this.parser = parser;
	}
	
	public String getActuator() {
		return parser.getActuator();
	}

	public String getZone() {
		return parser.getZone();
	}
}
