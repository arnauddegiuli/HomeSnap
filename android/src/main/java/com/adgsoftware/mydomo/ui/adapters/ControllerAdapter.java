package com.adgsoftware.mydomo.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.heating.Heating;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet;
import com.adgsoftware.mydomo.ui.components.AbstractComponent;
import com.adgsoftware.mydomo.ui.components.AutomationComponent;
import com.adgsoftware.mydomo.ui.components.GatewayComponent;
import com.adgsoftware.mydomo.ui.components.HeatingComponent;
import com.adgsoftware.mydomo.ui.components.LightComponent;
import com.adgsoftware.mydomo.ui.components.OutletComponent;

public class ControllerAdapter extends BaseAdapter {

	List<Controller<? extends Status>> controllerList = new ArrayList<Controller<? extends Status>>();
	private LayoutInflater mInflater;
	private List<View> cacheView = new ArrayList<View>();

	public ControllerAdapter(Context context,
			List<Controller<? extends Status>> controllers) {
		super();
		
		this.controllerList = controllers;
		
		mInflater = LayoutInflater.from(context);

		View v;
		
		for (Controller<? extends Status> controller : controllerList) {
			v = mInflater.inflate(R.layout.item_controller, null);

			TextView nameView = (TextView) v.findViewById(R.id.titre);
			nameView.setText(controller.getTitle());
	
			TextView sizeView = (TextView) v.findViewById(R.id.description);
			sizeView.setText("Addresse: " + String.valueOf(controller.getWhere()));
	
			LinearLayout controllerView = (LinearLayout) v.findViewById(R.id.controller);
			if (controller instanceof Light) {
				controllerView.addView(createLight((Light) controller, context));
			} else if (controller instanceof Automation) {
				controllerView.addView(createAutomation((Automation) controller, context));
			} else if (controller instanceof Outlet) {
				controllerView.addView(createOutlet((Outlet) controller, context));
			} else if (controller instanceof Heating) {
				controllerView.addView(createHeating((Heating) controller, context));
			} else if (controller instanceof Gateway) {
				controllerView.addView(createGateway((Gateway) controller, context));
			}
			
			cacheView.add(v);
		}
		
		// Add button
		ImageView btnPlus = new ImageView(context);
		btnPlus.setImageResource(android.R.drawable.btn_plus);
		btnPlus.setPadding(0, 10, 0, 5);
		
		cacheView.add(btnPlus);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return cacheView.get(position);
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
	
	/**
	 * Create a gateway controller and set its title.
	 * 
	 * @param gateway
	 * @param context
	 * @return
	 */
	protected GatewayComponent createGateway(final Gateway gateway, Context context) {

		GatewayComponent gatewayComponent = new GatewayComponent(gateway, (Activity) context);
		return gatewayComponent;

	}

	@Override
	public int getCount() {
		return cacheView.size();
	}


	@Override
	public Object getItem(int position) {
		if (position == controllerList.size()) {
			return "add";
		}
		else {
			return controllerList.get(position);
		}
	}


	@Override
	public long getItemId(int position) {
		return cacheView.get(position).getId();
	}

}
