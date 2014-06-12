package com.homesnap.engine.connector;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import java.util.List;

import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

/**
 * Each time a monitor session ( {@link Monitor} ) get a message and isn't able to 
 * found a controller, {@link UnknownControllerListener#foundUnknownController(String, String, String, String, List)}
 * is called.
 * @author adegiuli
 */
public interface UnknownControllerListener {

	public void foundUnknownController(Who who, Where where, State what);
}
