package com.homesnap.engine.connector;

import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
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
