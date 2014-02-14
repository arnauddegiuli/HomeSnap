package com.homesnap.engine.controller.state;

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


/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public final class State {
	
	/** */
	private StateName state;
	
	/** */
	private StateValue value;

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public State(StateName state, StateValue value) {
		this.state = state;
		this.value = value;
	}

	/**
	 * 
	 * @return
	 */
	public StateName getName() {
		return state;
	}

	/**
	 * 
	 * @return
	 */
	public StateValue getValue() {
		return value;
	}
}
