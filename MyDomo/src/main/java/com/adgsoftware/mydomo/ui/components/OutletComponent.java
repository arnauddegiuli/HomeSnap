package com.adgsoftware.mydomo.ui.components;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet.AutomationStatus;

public class OutletComponent extends AbstractComponent {

	private ToggleButton outletToggle;

	public OutletComponent(Context context, final Outlet outlet) {
		super(context);
		title = new TextView(context);
		outletToggle = new ToggleButton(context);
		this.addView(outletToggle);
		
		outletToggle.setId(Integer.valueOf(outlet.getWho()));
		outletToggle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AutomationStatus.OUTLET_ON.equals(outlet.getWhat())) {
					outlet.setWhat(com.adgsoftware.mydomo.engine.controller.outlet.Outlet.AutomationStatus.OUTLET_OFF);
				} else {
					outlet.setWhat(com.adgsoftware.mydomo.engine.controller.outlet.Outlet.AutomationStatus.OUTLET_ON);
				}
			}
		});
		
		outlet.addControllerChangeListener(new ControllerChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus) {
				outletToggle.setChecked(AutomationStatus.OUTLET_ON.equals(newStatus));
				
			}

			@Override
			public void onWhatChangeError(
					Controller<? extends Status> controller, Status oldStatus,
					Status newStatus, CommandResult result) {
				outletToggle.setChecked(AutomationStatus.OUTLET_ON.equals(oldStatus));
			}
			
		});

	}

	public ToggleButton getComponent() {
		return outletToggle;
	}
	
	public void setTitle(String text) {
		super.setTitle(text);
		this.title.setGravity(Gravity.TOP);
		outletToggle.setTextOff("OFF");
		outletToggle.setTextOff("ON");
	}

}
