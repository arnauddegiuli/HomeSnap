package com.homesnap.engine.services;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;


public interface ScanListener {

	public void foundController(Who who, Where where, Controller controller);
	public void progess(int percent);
}
