package com.homesnap.android.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homesnap.R;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet;
import com.homesnap.android.ui.components.AbstractComponent;
import com.homesnap.android.ui.components.AutomationComponent;
import com.homesnap.android.ui.components.GatewayComponent;
import com.homesnap.android.ui.components.HeatingComponent;
import com.homesnap.android.ui.components.LightComponent;
import com.homesnap.android.ui.components.OutletComponent;

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
			} else if (controller instanceof HeatingZone) {
				controllerView.addView(createHeating((HeatingZone) controller, context));
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
	protected AbstractComponent createLight(Light light, Context context) {
		return new LightComponent(context, light);

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
		return new AutomationComponent(
				context, automation);
	}

	/**
	 * Create a heating controller and set its title.
	 * 
	 * @param heating
	 * @param context
	 * @return
	 */
	protected HeatingComponent createHeating(final HeatingZone heating,
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
	protected GatewayComponent createGateway(Gateway gateway, Context context) {

		GatewayComponent gatewayComponent = new GatewayComponent(context);
		gatewayComponent.setGateway(gateway);
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
