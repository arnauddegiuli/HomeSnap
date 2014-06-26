package com.homesnap.engine.controller.counter;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.counter.stateValue.CounterStatusValue;
import com.homesnap.engine.controller.light.stateValue.LightStatusValue;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.who.Who;

public class Counter extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;

	public Counter() {
	}

	@Override
	public Who getWho() {
		return Who.ENERGY_MANAGEMENT;
	}

	@Override
	protected void initStateTypes() {
		declareState(StateName.STATUS, LightStatusValue.class);
	}

	public CounterStatusValue getStatus() {
		return (CounterStatusValue) get(StateName.STATUS);
	}

	public void setStatus(CounterStatusValue status) {
		set(StateName.STATUS, status);
	}
}
