package com.homesnap.engine.connector;

import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

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


/**
 * Interface which provides information from return from gateway after send a
 * command.
 */
public interface CommandResult {
	/**
	 * Return the status of the command (ok, nok or error)
	 * @return the status of the command.
	 */
	public CommandResultStatus getStatus();

	/**
	 * Return the "formatted" (from the raw result) what of the command.
	 * @return the what of the command.
	 */
	public State getWhat(StateName name);

	/**
	 * Return the type "formatted" (from the raw result) of the target device
	 * @return the type
	 */
	public Who getWho();

	/**
	 * Return the target "formatted" (from the raw result) of the command.
	 * @return the target of the command.
	 */
	public Where getWhere();

}