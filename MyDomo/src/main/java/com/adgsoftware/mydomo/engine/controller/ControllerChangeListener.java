package com.adgsoftware.mydomo.engine.controller;

import java.io.Serializable;

import com.adgsoftware.mydomo.engine.connector.CommandResult;

/** ControllerChangeListener. */
public interface ControllerChangeListener extends Serializable {
    /** @param controller the controller that changed. */
    void onWhatChange(Controller<? extends Status> controller, Status oldStatus, Status newStatus);
    
    void onWhatChangeError(Controller<? extends Status> controller, Status oldStatus, Status newStatus, CommandResult result);
}
