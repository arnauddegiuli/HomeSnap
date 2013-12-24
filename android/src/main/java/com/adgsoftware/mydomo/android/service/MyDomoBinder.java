package com.adgsoftware.mydomo.android.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import com.adgsoftware.mydomo.android.ui.Constants;
import com.adgsoftware.mydomo.android.ui.activities.SettingsActivity;
import com.adgsoftware.mydomo.android.util.Control;
import com.adgsoftware.mydomo.android.util.ControlException;
import com.adgsoftware.mydomo.android.util.IdAlreadyExist;
import com.adgsoftware.mydomo.android.util.MissingValue;
import com.adgsoftware.mydomo.android.util.NotExisting;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Icon;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.PersistenceService;
import com.adgsoftware.mydomo.engine.services.impl.OpenWebNetControllerService;
import com.adgsoftware.mydomo.engine.services.impl.PersistenceServiceImpl;

public class MyDomoBinder 
extends Binder 
implements MyDomoService {

	
	private PersistenceService persist;
	private ControllerService controller;
	private House house = null;
	private Application application;
	
	public MyDomoBinder() {
		super();
	}
	
	public void onCreate(Application application) {
		SharedPreferences settings = application.getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
		String serverIP = settings.getString(Constants.PROPERTY_NAME_SERVER_IP, Constants.DEFAULT_IP);
		String serverPort = settings.getString(Constants.PROPERTY_NAME_SERVER_PORT, Constants.DEFAULT_PORT);
		String serverPassword = settings.getString(Constants.PROPERTY_NAME_SERVER_PASSWORD, Constants.DEFAULT_PASSWORD);
		this.controller = new OpenWebNetControllerService(serverIP, Integer.parseInt(serverPort), Integer.parseInt(serverPassword));
		this.persist = new PersistenceServiceImpl(controller);
		this.application = application;
	}

	public void onDestroy() {
		this.controller.onDestroy();
	}

	@Override
	public boolean isBinderAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pingBinder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IInterface queryLocalInterface(String descriptor) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IBinder asBinder() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Business service

	@Override
	public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public <T extends Controller<? extends Status>> T createController(
			Class<T> clazz, String where) {
		return controller.createController(clazz, where);
	}
	
	public void save() throws IOException {
		if (house != null) {
			persist.save(house, application.openFileOutput("house.xml", Application.MODE_PRIVATE));
		}
	}
	
	public House retrieve() throws IOException {
		if (house == null) {
			house = persist.retrieve(application.openFileInput("house.xml"));
		}
		return house;
	}

	@Override
	public boolean isMonitorConnected() {
		return controller.isMonitorConnected();
	}

	@Override
	public boolean isCommanderConnected() {
		return controller.isCommanderConnected();
	}

	public void setServerIp(String serverIp) {
		Log.i("Controller set IP", serverIp);
		controller.setIp(serverIp);
	}

	@Override
	public void setServerPort(int serverPort) {
		Log.i("Controller set port", Integer.toString(serverPort));
		controller.setPort(serverPort);
	}

	@Override
	public void addMonitorConnectionListener(ConnectionListener listener) {
		controller.addMonitorConnectionListener(listener);
	}

	@Override
	public void addCommanderConnectionListener(
			ConnectionListener listener) {
		controller.addCommanderConnectionListener(listener);
	}

	@Override
	public void setServerPassword(int password) {
		Log.i("Controller set Password", "******");
		controller.setPassword(password);
	}

	@Override
	public void connect() {
		controller.connect();
	}

	private House getHouse() {
		try {
			return retrieve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			house = new House();
			return house;
		}
	}




// Label
	@Override
	public List<Label> getLabels() {
		return getHouse().getLabels();
	}

	@Override
	public Label createLabel(String id, String title, String description,
			Icon icon, String iconPath,
			Collection<? extends Controller<? extends Status>> controllerList)
			throws ControlException {
		List<Control> controlList = new ArrayList<Control>();
		if (id == null) {
			controlList.add(new MissingValue());
		} else if (getLabel(id) != null) {
			controlList.add(new IdAlreadyExist());
		}

		if (title == null) {
			controlList.add(new MissingValue());
		}


		if (controlList.size() > 0) {
			throw new ControlException(controlList);
		}
		Label label = new Label();
		label.setTitle(title);
		// label.setDescription(description); TODO
		label.setId(id);
		label.setIcon(icon);
		label.setIconPath(iconPath);
		if (controllerList != null) label.addAll(controllerList);

		getHouse().getLabels().add(label); // TODO manage recursivity
		
		// TODO add to the group!
		return label;
	}

	/**
	 * Modify the label.
	 * 
	 * @param labelToModify
	 * @param id
	 *            new id to set on the label. If null: old id not modified.
	 * @param title
	 *            new title to modify. If null: old title not modified
	 * @param description
	 *            TODO finish documentation
	 * @param icon
	 * @param iconPath
	 * @param controllerList
	 * @return
	 * @throws ControlException
	 */
	@Override
	public Label modifyLabel(String oldId, String newid, String title,
			String description, Icon icon, String iconPath,
			Collection<? extends Controller<? extends Status>> controllerList)
			throws ControlException {
		List<Control> controlList = new ArrayList<Control>();
		if (oldId == null) {
			controlList.add(new MissingValue());
		}

		if (controlList.size() > 0) {
			throw new ControlException(controlList);
		}

		Label label = getLabel(oldId);
		if (title != null)
			label.setTitle(title);
		// if (description != null) label.setDescription(description); TODO
		if (newid != null)
			label.setId(newid);
		if (icon != null)
			label.setIcon(icon);
		if (iconPath != null)
			label.setIconPath(iconPath);
		if (controllerList != null)
			label.addAll(controllerList);
		return label;
	}

	public void removeLabel(String id) throws ControlException {
		List<Control> controlList = new ArrayList<Control>();
		if (id == null) {
			controlList.add(new MissingValue());
		} else if (getLabel(id) == null) {
			controlList.add(new NotExisting());
		}
		
		if (controlList.size() > 0) {
			throw new ControlException(controlList);
		}

		
		
		getLabel(id).clear(); // TODO add a method when remove a label => detach all controllers
		getHouse().getLabels().remove(getLabel(id));
		
	}

	public Label getLabel(String id) {
		for (Label label : getHouse().getLabels()) {
			if (id == label.getId()) {
				return label; // TODO manage with equals!!!
			}
		}
		return null;
	}

	public List<Label> searchLabel(String title) {
		List<Label> result = new ArrayList<Label>();
		for (Label label : getHouse().getLabels()) {
			if (title == label.getTitle()) {
				result.add(label); // TODO manage with equals!!!
			}
		}

		return result;
	}
	
}
