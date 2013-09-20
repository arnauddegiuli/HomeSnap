package com.adgsoftware.mydomo.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionStatusEnum;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.ui.AbstractActivity;

/**
 * Settings screen. Allows to add/edit/delete controls & rooms, scenarios, etc.
 */
public class SettingsActivity extends AbstractActivity {

	public static final String PREFS_NAME = "myDomo.settings";
	LinearLayout settingsLayout;
	TextView textView;
	Handler handler;
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
		refreshList();
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveServerSettings();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		handler = new Handler() {
	        public void handleMessage(Message msg) {
	        	textView.setText(getConnectedStatusMessage());
	        	textView.invalidate();
	        }
	    };
	    
		settingsLayout = (LinearLayout) findViewById(R.id.settingsLayout);
		
		setTitle(getResources().getText(R.string.title_settings));
		setContentView(R.layout.settings);

		/* Restore old values */
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String serverIP = settings.getString("serverIP", "192.168.0.19"); // TODO define default value?
		String serverPort = settings.getString("serverPort", "1234");
		String serverPassword = settings.getString("serverPassword", "12345");
		EditText txtServerIP = (EditText) findViewById(R.id.txtServerIP);
		EditText txtServerPort = (EditText) findViewById(R.id.txtServerPort);
		EditText txtServerPassword = (EditText) findViewById(R.id.txtServerPassword);
		txtServerIP.setText(serverIP);
		txtServerPort.setText(serverPort);
		txtServerPassword.setText(serverPassword);
		
		super.onCreate(savedInstanceState);
		
        createLayout();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	case R.id.menu_add_controller:
	    		showEditControllerScreen(null);
	    		return true;
	        case R.id.menu_refresh:
				refresh();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	protected void refresh() {
		bindService();
	}
	
	/**
	 * Save the given server settings in the shared preferences 
	 * then update the current commander and monitor. 
	 */
	private void saveServerSettings() {
		EditText txtServerIP = (EditText) findViewById(R.id.txtServerIP);
		EditText txtServerPort = (EditText) findViewById(R.id.txtServerPort);
		EditText txtServerPassword = (EditText) findViewById(R.id.txtServerPassword);
		String serverIP = txtServerIP.getText().toString();
		String serverPort = txtServerPort.getText().toString();
		String serverPassword = txtServerPassword.getText().toString();
		Log.i("Save settings", serverIP + ":" + serverPort);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		// Not change the ip if the value is the same => avoid close connection on leaving settings screen
		if (!settings.getString("serverIP", "0").equals(serverIP)) {
			editor.putString("serverIP", serverIP);
			myDomoService.setServerIp(serverIP);
		}
		
		if (!settings.getString("serverPort", "0").equals(serverPort)) {
			editor.putString("serverPort", serverPort);
			myDomoService.setServerPort(Integer.parseInt(serverPort));
		}
		
		if (!settings.getString("serverPassword", "0").equals(serverPassword)) {
			editor.putString("serverPassword", serverPassword);
			myDomoService.setServerPassword(Integer.parseInt(serverPassword));
		}
		
		editor.commit();
	}
	
	private static String COMMAND_OFFLINE = "Command[OFFLINE]";
	private static String COMMAND_ONLINE = "Command[ON]";
	private static String MONITOR_OFFLINE = "Monitor[OFFLINE]";
	private static String MONITOR_ONLINE = "Monitor[ON]";
	
	private String commandState = COMMAND_OFFLINE;
	private String monitorState = MONITOR_OFFLINE;
	
	private String getConnectedStatusMessage() {
		return commandState + " | " + monitorState;
	}
	
	
	private void createConnectedStatusViewer() {
		textView = (TextView) findViewById(R.id.connectedStatusViewer);
		
		if (myDomoService.isCommanderConnected()) {
			commandState = COMMAND_ONLINE;
		}
		if (myDomoService.isMonitorConnected()) {
			monitorState = MONITOR_ONLINE;
		}
		
		textView.setText(getConnectedStatusMessage());
		
		myDomoService.addCommanderConnectionListener(
				new ConnectionListener() {
					
					@Override
					public void onConnect(ConnectionStatusEnum connectionStatus) {
						if (ConnectionStatusEnum.Connected.equals(connectionStatus)) {
							commandState = COMMAND_ONLINE;
							handler.sendEmptyMessage(0);
						}
					}
					
					@Override
					public void onClose() {
						commandState = COMMAND_OFFLINE;
						handler.sendEmptyMessage(0);
					}
				}
		);
		
		myDomoService.addMonitorConnectionListener(
				new ConnectionListener() {
					
					@Override
					public void onConnect(ConnectionStatusEnum connectionStatus) {
						if (ConnectionStatusEnum.Connected.equals(connectionStatus)) {
							monitorState = MONITOR_ONLINE;
							handler.sendEmptyMessage(0);
						}
					}
					
					@Override
					public void onClose() {
						monitorState = MONITOR_OFFLINE;
						handler.sendEmptyMessage(0);
					}
				}
		);
		
	}

	/**
	 * Load the controllers, then bind to the list view.
	 */
	private void refreshList() {
		Log.d("Controllers", "Refresh list");
		createConnectedStatusViewer();
		
		ListView listView = (ListView) findViewById(R.id.listviewSettings);
		List<Controller<? extends Status>> controllers = new ArrayList<Controller<? extends Status>>();
		if (getHouse().getGroups() != null) {
			for (Group room : getHouse().getGroups()) {
				for (Controller<? extends Status> controller : room.getControllerList()) {
					controllers.add(controller);
				}
			}
		}
		listView.setAdapter(createControllerAdapter(controllers));
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == parent.getChildCount()+1) {
					showEditControllerScreen(null);
				}
			}
		});
	}
	
}
