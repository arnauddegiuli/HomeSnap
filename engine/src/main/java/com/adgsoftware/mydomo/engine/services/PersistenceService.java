package com.adgsoftware.mydomo.engine.services;

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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.adgsoftware.mydomo.engine.house.House;

/**
 * PersistenceService save and restore room list. 
 */
public interface PersistenceService {

	/**
	 * Retrieve from persistence the list of room.
	 * @return the list of room
	 */
	public House retrieve(FileInputStream file) throws IOException;
	
	/**
	 * Save the list of room.
	 * @param house list of room to save.
	 */
	public void save(House house, FileOutputStream file) throws IOException ;
}