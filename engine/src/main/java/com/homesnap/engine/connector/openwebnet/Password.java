package com.homesnap.engine.connector.openwebnet;

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

public class Password {

	public String calcPass (int password, String nonce) {
		boolean flag = true;
		int num1 = 0x0;
		int num2 = 0x0;

		for (int i = 0; i < nonce.length(); i++) {
			char c = nonce.charAt(i);
			if (c!='0') {
				if (flag) num2 = password;
				flag = false;
			}
			switch (c) {
				case '1':
					num1 = num2 & 0xFFFFFF80;
					num1 = num1 >>> 7;
					num2 = num2 << 25;
					num1 = num1 + num2;
					break;
				case '2':
					num1 = num2 & 0xFFFFFFF0;
					num1 = num1 >>> 4;
					num2 = num2 << 28;
					num1 = num1 + num2;
					break;
				case '3':
					num1 = num2 & 0xFFFFFFF8;
					num1 = num1 >>> 3;
					num2 = num2 << 29;
					num1 = num1 + num2;
					break;
				case '4':
					num1 = num2 << 1;
					num2 = num2 >>> 31;
					num1 = num1 + num2;
					break;
				case '5':
					num1 = num2 << 5;
					num2 = num2 >>> 27;
					num1 = num1 + num2;
					break;
				case '6':
					num1 = num2 << 12;
					num2 = num2 >>> 20;
					num1 = num1 + num2;
					break;
				case '7':
					num1 = num2 & 0x0000FF00;
					num1 = num1 + (( num2 & 0x000000FF ) << 24 );
					num1 = num1 + (( num2 & 0x00FF0000 ) >>> 16 );
					num2 = ( num2 & 0xFF000000 ) >>> 8;
					num1 = num1 + num2;
					break;
				case '8':
					num1 = num2 & 0x0000FFFF;
					num1 = num1 << 16;
					num1 = num1 + ( num2 >>> 24 );
					num2 = num2 & 0x00FF0000;
					num2 = num2 >>> 8;
					num1 = num1 + num2;
					break;
				case '9':
					num1 = ~num2;
					break;
				case '0':
					num1 = num2;
					break;
			}
			num2 = num1;
		}

		return String.valueOf(Long.parseLong(Integer.toBinaryString(num1 >>> 0), 2));
	}

}
