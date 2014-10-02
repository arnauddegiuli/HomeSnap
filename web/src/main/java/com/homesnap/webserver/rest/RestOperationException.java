package com.homesnap.webserver.rest;

public class RestOperationException extends Exception {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	private String uri;
	private Verb verb;
	
	public RestOperationException(String uri, Verb verb, String message) {
		super("[" + verb + "]-["+ uri + "]: " + message);
		this.uri = uri;
		this.verb = verb;
	}

	public String getUri() {
		return uri;
	}

	public Verb getVerb() {
		return verb;
	}
}
