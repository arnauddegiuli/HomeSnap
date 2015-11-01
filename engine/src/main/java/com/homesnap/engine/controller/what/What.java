package com.homesnap.engine.controller.what;

public class What {
	private String name;
	private State<?> value;

	public What(String name, State<?> value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public State<?> getValue() {
		return value;
	}
}
