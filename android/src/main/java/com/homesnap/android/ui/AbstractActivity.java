package com.homesnap.android.ui;

import java.io.IOException;
import java.util.List;

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
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;
import com.homesnap.android.service.MyDomoService;
import com.homesnap.android.service.MyDomoServiceImpl;
import com.homesnap.android.ui.activities.EditControllerActivity;
import com.homesnap.android.ui.activities.SelectLabelsActivity;
import com.homesnap.android.ui.activities.SettingsActivity;
import com.homesnap.android.ui.adapters.ControllerAdapter;
import com.homesnap.android.ui.components.AutomationComponent;
import com.homesnap.android.ui.components.HeatingComponent;
import com.homesnap.android.ui.components.OutletComponent;
import com.homesnap.android.ui.components.RoomComponent;

/**
 * Common class of MyDomo screens.
 */
public abstract class AbstractActivity extends Activity {

	protected static MyDomoService myDomoService;

	public House getHouse() {
		try {
			if (myDomoService != null) {
				return myDomoService.retrieve();
			} else {
				return new House();
			}
		} catch (Exception e) {
			return new House(); // TODO manage better
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bindService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		bindService();
	}
	
	protected void bindService() {
		if (myDomoService == null) {
			ServiceConnection sc = new ServiceConnection() {
				
				@Override
				public void onServiceDisconnected(ComponentName name) {
					myDomoService=null;
				}
				
				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					myDomoService=(MyDomoService) service;
					refreshLayoutAfterServiceConnection();
				}
			};
			
			getApplicationContext().bindService(new Intent(this, MyDomoServiceImpl.class),
						sc, BIND_AUTO_CREATE);
		} else {
			myDomoService.connect();
		}
	}
	
	protected void refreshLayoutAfterServiceConnection() {
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
	protected HeatingComponent createHeating(final HeatingZone heating, Context context) {

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
	
	protected Group getGroup(House house, String selectedGroupId, boolean createGroup) {
		for (Group group : house.getGroups()) {
			if (group.getId().equals(selectedGroupId)) {
				return group;
			}
		}
		if (createGroup) {
			Group g = new Group();
			g.setId(selectedGroupId);
			house.getGroups().add(g);
			return g;
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
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	public String computeGroupFromAddress(String where) {
		if (where == null)
			return "";
		else if (where.length() <= 1)
			return "0";
		
		return where.substring(0, 1);
	}

	protected void showSettingsScreen() {
		startActivity(new Intent(AbstractActivity.this, SettingsActivity.class));
	}
	
	protected void saveHouse() {
		try {
			myDomoService.save();
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

	protected ControllerAdapter createControllerAdapter(List<Controller<? extends Status>> controllers) {
		// fill in the grid_item layout
		ControllerAdapter adapter = new ControllerAdapter(this, controllers);
		return adapter;
	}

}
