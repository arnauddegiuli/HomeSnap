package com.adgsoftware.mydomo.webserver.rest;

public class UnsupportedRestOperation extends Exception {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	
	private String uri;
	private Verb verb;
	
	public UnsupportedRestOperation(String uri, Verb verb) {
		this.uri = uri;
		this.verb=verb;
	}

	public String getUri() {
		return uri;
	}

	public Verb getVerb() {
		return verb;
	}

	@Override
	public String getMessage() {
		return "Rest operation unsupported: [" + verb.name() + "]-[" + uri + "]";
	}
}
