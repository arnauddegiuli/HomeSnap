package com.adgsoftware.mydomo.engine.controller;

public interface DimensionStatusCallback<T extends DimensionStatus> {
	
	public void value(T value);

}
