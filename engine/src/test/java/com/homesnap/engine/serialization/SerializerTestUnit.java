package com.homesnap.engine.serialization;

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
