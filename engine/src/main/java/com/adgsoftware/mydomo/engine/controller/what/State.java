package com.adgsoftware.mydomo.engine.controller.what;

public final class State { // Représente une état transmis dans une commande,
							// dedans il y a le stateName (enum) et sa valeur.

	private StateName name;
	private StateValue value;

	public State(StateName state, StateValue value) {
		this.name = state;
		this.value = value;
	}

	public StateName getName() {
		return name;
	}

	public StateValue getValue() {
		return value;
	}
}