package com.adgsoftware.mydomo.engine.controller.what;


public interface StateName {

	public StateName STATUS = new StateName() {
		@Override
		public String getName() {
			return "status";
		}
	};
	
	/**
	 * Return a string representation of the name of state.
	 * @return a name.
	 */
	public String getName();
}
