package com.homesnap.engine.controller;

/*
 * #%L
 * MyDomoEngine
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


import com.homesnap.engine.controller.state.State;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class Command {
	
	public enum CommandType {
		READ, WRITE
	}
	
	/** */
	private String from;
	
	/** */
	private String to;

	/** */
	private State state;
	
	/** */
	private CommandType type;

	/**
	 * 
	 * @param state
	 */
	public Command(String from, String to, State state) {
		this.from = from;
		this.to = to;
		this.state = state;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * 
	 * @return
	 */
	public String getTo() {
		return to;
	}

	/**
	 * 
	 * @return
	 */
	public State getState() {
		return state;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isReadCommand() {
		return CommandType.READ.equals(type);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isWriteCommand() {
		return CommandType.WRITE.equals(type);
	}
}
