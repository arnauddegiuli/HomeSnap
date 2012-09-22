package com.adgsoftware.mydomo.engine.controller;

import java.io.Serializable;

import com.adgsoftware.mydomo.engine.connector.CommandResult;

/** ControllerChangeListener. */
public interface ControllerDimensionChangeListener extends Serializable {
    
    void onDimensionChange(ControllerDimension<? extends Status> controller, DimensionStatus newStatus);
    
    void onDimensionChangeError(ControllerDimension<? extends Status> controller, DimensionStatus newStatus, CommandResult result);
}
