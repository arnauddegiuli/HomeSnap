package com.adgsoftware.mydomo.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.engine.controller.heating.Heating;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.ui.components.AbstractComponent;
import com.adgsoftware.mydomo.ui.components.AutomationComponent;
import com.adgsoftware.mydomo.ui.components.HeatingComponent;
import com.adgsoftware.mydomo.ui.components.LightComponent;
import com.adgsoftware.mydomo.ui.components.OutletComponent;
import com.adgsoftware.mydomo.ui.components.RoomComponent;
import com.adgsoftware.mydomo.ui.service.MyDomoService;
import com.adgsoftware.mydomo.ui.service.MyDomoServiceImpl;

/**
 * Common class of MyDomo screens.
 */
public abstract class AbstractActivity extends Activity {
	
	protected static MyDomoService myDomoService;
	private ServiceConnection sc;

	private House house = null;
	
	public House getHouse() {
		try {
			if (myDomoService != null) {
				house = myDomoService.retrieve();
			} else {
				house = new House();
			}

		} catch (Exception e) {
			house = new House(); // TODO manage better
		}
		return house;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (myDomoService == null) {
			bindService();
		}
	}
	
	protected void bindService() {
		sc = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				myDomoService=null;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				myDomoService=(MyDomoService) service;
				createLayout();
			}
		};
		
		getApplicationContext().bindService(new Intent(this, MyDomoServiceImpl.class),
					sc, BIND_AUTO_CREATE);
	}
	
	protected void createLayout() {
	}

	/**
	 * Create a room.
	 * @param room
	 * @param applicationContext
	 * @return
	 */
	protected RoomComponent createRoom(Group room, Context applicationContext) {
		final RoomComponent roomComponent = new RoomComponent(applicationContext);
		roomComponent.setTitle(room.getTitle());
		
		return roomComponent;
	}

	/**
	 * Create a light controller and set its title.
	 * @param light
	 * @param context
	 * @return
	 */
	protected AbstractComponent createLight(final Light light, Context context) {

		LightComponent lightComponent = new LightComponent(context, light);
		lightComponent.setTitle(light.getTitle());
		return lightComponent;

	}

	/**
	 * Create an automation controller and set its title.
	 * @param automation
	 * @param context
	 * @return
	 */
	protected AutomationComponent createAutomation(final Automation automation, Context context) {

		AutomationComponent automationComponent = new AutomationComponent(context, automation);
		automationComponent.setTitle(automation.getTitle());
		return automationComponent;

	}

	/**
	 * Create a heating controller and set its title.
	 * @param heating
	 * @param context
	 * @return
	 */
	protected HeatingComponent createHeating(final Heating heating, Context context) {

		HeatingComponent heatingComponent = new HeatingComponent(context, heating);
		heatingComponent.setTitle(heating.getTitle());
		return heatingComponent;
		
	}

	/**
	 * Create an outlet controller and set its title.
	 * @param outlet
	 * @param context
	 * @return
	 */
	protected OutletComponent createOutlet(final Outlet outlet, Context context) {

		OutletComponent outletComponent = new OutletComponent(context, outlet);
		outletComponent.setTitle(outlet.getTitle());
		return outletComponent;

	}
	
	protected Group getGroup(House house, String selectedGroupId) {
		for (Group group : house.getGroups()) {
			if (group.getId().equals(selectedGroupId)) {
				return group;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Controller<Status> getController(House house, String where) {
		for (Group group : house.getGroups()) {
			for (Controller<? extends Status> controller : group.getControllerList()) {
				if (controller.getWhere().equals(where)) {
					return (Controller<Status>) controller;
				}
			}
		}
		return null;
	}
	
	public Label getLabel(House house, String id) {
		for (Label label : house.getLabels()) {
			if (label.getId().equals(id)) {
				return label;
			}
		}
		return null;
	}
	
	public String computeGroupFromAddress(String where) {
		// TODO manage it smarter
		return where.substring(0, 1);
	}

	protected void showSettingsScreen() {
		startActivity(new Intent(AbstractActivity.this, SettingsActivity.class));
	}
	
	protected void saveHouse(House house) {
		try {
			myDomoService.save(house);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void showLabelsScreen() {
		startActivity(new Intent(AbstractActivity.this, SelectLabelsActivity.class));
	}

	protected void showEditControllerScreen(String where) {
		Intent intent = new Intent(AbstractActivity.this,
				EditControllerActivity.class);
		if (where != null) {
			Log.i("Edit controller", where);
			intent.putExtra("selectedControllerId", where);
		}
		startActivity(intent);
	}

}
