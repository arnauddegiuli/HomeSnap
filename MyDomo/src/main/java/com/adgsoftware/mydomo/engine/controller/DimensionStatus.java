package com.adgsoftware.mydomo.engine.controller;

import java.util.List;

public interface DimensionStatus {
	public List<DimensionValue> getValueList();
	public void setValueList(List<DimensionValue> dimensionList);
	public String getCode();
}
