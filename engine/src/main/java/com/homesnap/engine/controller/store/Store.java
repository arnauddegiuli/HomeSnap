package com.homesnap.engine.controller.store;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.state.StateName;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class Store extends Controller {

	/**
	 * 
	 */
	public Store(String address) {
		super(address);
	}

	@Override
	protected void initStateTypes() {
		declareState(StateName.STATUS, StoreStatus.class);
	}
}
