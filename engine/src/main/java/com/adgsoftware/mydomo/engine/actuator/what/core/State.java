package com.adgsoftware.mydomo.engine.actuator.what.core;

public final class State { // Représente une état transmis dans une commande,
							// dedans il y a le stateName (enum) et sa valeur.

	private StateName state;

	private StateValue value;

	public State(StateName state, StateValue value) {

		this.state = state;

		this.value = value;

	}

	public StateName getName() {

		return state;

	}

	public StateValue getValue() {

		return value;

	}

}