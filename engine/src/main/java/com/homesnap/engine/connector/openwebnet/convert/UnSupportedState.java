package com.homesnap.engine.connector.openwebnet.convert;

public class UnSupportedState extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3943007799850403939L;

	@Override
	public String getMessage() {
		return "State unsupported.";
	}

}
