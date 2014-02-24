package com.homesnap.engine.controller.what;

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
 * A tpye of Value. This interface can be implemented by two ways:
 * <ul>
 * <li>An enumeration of limited values. Example: ON/OFF or STOP/UP/DOWN, etc..</li>
 * <li>A classe reprensenting a large or infinites possible values Example: a temperature, a time, etc..</li>
 * </ul>
 * @see States
 * 
 * @version 1.0
 * @since 1.0
 */
public interface StateValue {

	/**
	 * Return a string representation of the value from this type.
	 * @return a value.
	 */
	public String getValue();
}
