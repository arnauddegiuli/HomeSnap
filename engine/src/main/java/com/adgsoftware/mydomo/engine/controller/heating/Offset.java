package com.adgsoftware.mydomo.engine.controller.heating;

public class Offset {

	public enum Mode {
		ON, OFF, PROTECTION;
	}
	
	private Mode mode;
	private int degree;
	
	public Offset(Mode mode, int degree) {
		this.mode = mode;
		this.degree = degree;
	}

	public Mode getMode() {
		return mode;
	}
	
	public int getDegree() {
		return degree;
	}
}
