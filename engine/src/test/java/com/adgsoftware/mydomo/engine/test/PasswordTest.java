package com.adgsoftware.mydomo.engine.test;

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

import junit.framework.Assert;

import org.junit.Test;

import com.adgsoftware.mydomo.engine.connector.impl.Password;

public class PasswordTest {

	private Password passwordEncoder = new Password();
	
	@Test
	public void passwordTest() {
		testCalcPass (12345, "603356072", "25280520");
		testCalcPass (12345, "410501656", "119537670");
		testCalcPass (12345, "630292165", "4269684735");
		testCalcPass (12345, "523781130", "537331200");
	}
	
	private void testCalcPass (int pass, String nonce, String expected) {
		String res = passwordEncoder.calcPass (pass, nonce);
		Assert.assertEquals(expected, res);
	}
}
