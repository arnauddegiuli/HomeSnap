package com.adgsoftware.mydomo.engine.connector;

import java.util.List;

import com.adgsoftware.mydomo.engine.controller.DimensionValue;

/**
 * Each time a monitor session ( {@link Monitor} ) get a message and isn't able to 
 * found a controller, {@link UnknownControllerListener#foundUnknownController(String, String, String, String, List)}
 * is called.
 * @author adegiuli
 */
public interface UnknownControllerListener {

	public void foundUnknownController(String who, String where, String what, String dimensionCode, List<DimensionValue> dimensionList);
}
