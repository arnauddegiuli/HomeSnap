package com.homesnap.engine.serialization;

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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class SerializerTestUnit {
	
	private ByteArrayOutputStream memoryStream;
	
	protected void serialize(Object o) {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(outputStream);
			try {
				oos.writeObject(o);
				oos.flush();
			} finally {
				try {
					oos.close();
				} finally {
					memoryStream = outputStream;
				}
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected Object deserialize() {
		
		Object o = null;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(memoryStream.toByteArray());
		try {
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			try {
				o = ois.readObject();
			} finally {
				try {
					ois.close();
				} finally {
					memoryStream = null;
				}
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		return o;
	}
}
