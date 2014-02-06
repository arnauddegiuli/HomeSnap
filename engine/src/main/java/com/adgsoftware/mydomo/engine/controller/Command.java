package com.adgsoftware.mydomo.engine.controller;

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
