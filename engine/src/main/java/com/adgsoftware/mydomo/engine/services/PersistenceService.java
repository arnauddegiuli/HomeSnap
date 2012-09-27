package com.adgsoftware.mydomo.engine.services;

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
	public House retrieve() throws IOException;
	
	/**
	 * Save the list of room.
	 * @param house list of room to save.
	 */
	public void save(House house) throws IOException ;
}