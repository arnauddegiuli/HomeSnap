package com.homesnap.engine.connector;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

public class Command {

	public enum Type {
		STATUS, ACTION;
	}

	private Who who;
	private What what;
	private Where where;
	private Type type;
	private Controller source;
	
	public Command(Who who, What what, Where where, Type type, Controller source) {
		this.what = what;
		this.who = who;
		this.where = where;
		this.type = type;
		this.source = source;
	}

	public Who getWho() {
		return who;
	}

	public What getWhat() {
		return what;
	}

	public Where getWhere() {
		return where;
	}
	
	public Controller getSource() {
		return source;
	}

	public boolean isStatusCommand() {
		return Type.STATUS.equals(type);
	}

	public boolean isActionCommand() {
		return Type.ACTION.equals(type);
	}
	
	public String toString() {
		return toJSon();
	}
	
	public String toJSon() {
		return "{".concat(what.getName()).concat("=").concat(String.valueOf(what.getValue())).concat(",")
				.concat("where=").concat(where.getTo()).concat(",")
				.concat("who=").concat(who.name()).concat("}");
	}
}
