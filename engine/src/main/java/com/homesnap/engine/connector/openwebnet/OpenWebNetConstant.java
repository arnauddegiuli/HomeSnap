package com.homesnap.engine.connector.openwebnet;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
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


public class OpenWebNetConstant {
	// Standard *WHO*WHAT*WHERE##
	// Status request *#WHO*WHERE##
	// Dimension request *#WHO*WHERE*DIMENSION##
	// Dimension write *#WHO*WHERE*#DIMENSION*VAL1*VAL2*...*VALn##

	public final static String ACK = "*#*1##";
	public final static String NACK = "*#*0##";

	public final static String COMMAND_SESSION = "*99*0##";
	public final static String MONITOR_SESSION = "*99*1##";

	public final static String COMMAND = "*{0}*{1}*{2}##"; // *WHO*WHAT*WHERE##
	public final static String STATUS = "*#{0}*{1}##"; // *#WHO*WHERE##
	public final static String DIMENSION_STATUS = "*#{0}*{1}*{2}##"; // *#WHO*WHERE*DIMENSION##
																		// =>
																		// response:
																		// *#WHO#*WHERE*WHATDIMENSION*VAL1*...*VALn##
	public final static String DIMENSION_COMMAND = "*#{0}*{1}*#{2}*{3}##"; // *#WHO*WHERE*#WHATDIMENSION*DIMESION1*...*Dimensionn##
	public final static String DIMENSION_SEPARATOR = "*";
}
