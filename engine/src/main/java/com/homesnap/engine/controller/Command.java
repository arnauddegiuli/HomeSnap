package com.homesnap.engine.controller;

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
