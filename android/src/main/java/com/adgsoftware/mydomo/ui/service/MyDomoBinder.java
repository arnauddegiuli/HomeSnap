package com.adgsoftware.mydomo.ui.service;

import java.io.IOException;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.services.ControllerService;
import com.adgsoftware.mydomo.engine.services.PersistenceService;
import com.adgsoftware.mydomo.engine.services.impl.ControllerServiceImpl;
import com.adgsoftware.mydomo.engine.services.impl.PersistenceServiceImpl;
import com.adgsoftware.mydomo.ui.activities.SettingsActivity;

public class MyDomoBinder 
extends Binder 
implements MyDomoService {

	private PersistenceService persist;
	private ControllerService controller;
	private House house = null;
	
	public MyDomoBinder() {
		super();
	}
	
	public void onCreate(Application application) {
		SharedPreferences settings = application.getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
		String serverIP = settings.getString("serverIP", "192.168.0.19");
		String serverPort = settings.getString("serverPort", "1234");
		String serverPassword = settings.getString("serverPassword", "12345");
		this.controller = new ControllerServiceImpl(serverIP, Integer.parseInt(serverPort), Integer.parseInt(serverPassword));
		this.persist = new PersistenceServiceImpl(application, controller);
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
	
	public void save(House house) throws IOException {
		this.house = house;  
		persist.save(house);
	}
	
	public House retrieve() throws IOException {
		if (house == null) {
			house = persist.retrieve();
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
	
}
