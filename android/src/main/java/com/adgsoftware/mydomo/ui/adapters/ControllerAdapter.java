package com.adgsoftware.mydomo.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.engine.controller.heating.Heating;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet;
import com.adgsoftware.mydomo.ui.components.AbstractComponent;
import com.adgsoftware.mydomo.ui.components.AutomationComponent;
import com.adgsoftware.mydomo.ui.components.HeatingComponent;
import com.adgsoftware.mydomo.ui.components.LightComponent;
import com.adgsoftware.mydomo.ui.components.OutletComponent;

public class ControllerAdapter extends ArrayAdapter<Controller<? extends Status>> {

	List<Controller<? extends Status>> mControllers = new ArrayList<Controller<? extends Status>>();
	private LayoutInflater mInflater;

	public ControllerAdapter(Context context,
			List<Controller<? extends Status>> controllers) {
		super(context, R.layout.item_controller, controllers);
		this.mControllers = controllers;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = mInflater.inflate(R.layout.item_controller, null);

		Controller<? extends Status> controller = mControllers.get(position);

		TextView nameView = (TextView) rowView.findViewById(R.id.titre);
		nameView.setText(controller.getTitle());

		TextView sizeView = (TextView) rowView.findViewById(R.id.description);
		sizeView.setText("Addresse: " + String.valueOf(controller.getWhere()));

		LinearLayout controllerView = (LinearLayout) rowView
				.findViewById(R.id.controller);
		if (controller instanceof Light) {
			controllerView.addView(createLight((Light) controller, getContext()));
		} else if (controller instanceof Automation) {
			controllerView.addView(createAutomation((Automation) controller, getContext()));
		} else if (controller instanceof Outlet) {
			controllerView.addView(createOutlet((Outlet) controller, getContext()));
		} else if (controller instanceof Heating) {
			controllerView.addView(createHeating((Heating) controller, getContext()));
		} 

		return rowView;
	}

	/**
	 * Create a light controller and set its title.
	 * 
	 * @param light
	 * @param context
	 * @return
	 */
	protected AbstractComponent createLight(final Light light, Context context) {

		LightComponent lightComponent = new LightComponent(context, light);
		return lightComponent;

	}

	/**
	 * Create an automation controller and set its title.
	 * 
	 * @param automation
	 * @param context
	 * @return
	 */
	protected AutomationComponent createAutomation(final Automation automation,
			Context context) {

		AutomationComponent automationComponent = new AutomationComponent(
				context, automation);
		return automationComponent;

	}

	/**
	 * Create a heating controller and set its title.
	 * 
	 * @param heating
	 * @param context
	 * @return
	 */
	protected HeatingComponent createHeating(final Heating heating,
			Context context) {

		HeatingComponent heatingComponent = new HeatingComponent(context,
				heating);
		return heatingComponent;

	}

	/**
	 * Create an outlet controller and set its title.
	 * 
	 * @param outlet
	 * @param context
	 * @return
	 */
	protected OutletComponent createOutlet(final Outlet outlet, Context context) {

		OutletComponent outletComponent = new OutletComponent(context, outlet);
		return outletComponent;

	}

}
