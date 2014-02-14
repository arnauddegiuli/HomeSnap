package com.adgsoftware.mydomo.engine.controller;

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

import com.adgsoftware.mydomo.engine.controller.what.State;
import com.adgsoftware.mydomo.engine.controller.where.Where;
import com.adgsoftware.mydomo.engine.controller.who.Who;

public class Command {

	public enum Type {
		STATUS, ACTION;
	}

	private Who who;
	private State what;
	private Where where;
	private Type type;
	
	public Command(Who who, State what, Where where, Type type) {
		this.what = what;
		this.who = who;
		this.where = where;
		this.type = type;
	}

	public Who getWho() {
		return who;
	}

	public State getWhat() {
		return what;
	}

	public Where getWhere() {
		return where;
	}

	public boolean isStatusCommand() {
		return Type.STATUS.equals(type);
	}

	public boolean isActionCommand() {
		return Type.ACTION.equals(type);
	}
}
