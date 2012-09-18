package com.adgsoftware.mydomo.ui;

import java.io.IOException;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.connector.ConnectionStatusEnum;
import com.adgsoftware.mydomo.engine.connector.OpenWebConnectionListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;
import com.adgsoftware.mydomo.engine.controller.heating.Heating;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.outlet.Outlet;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.ui.components.AbstractComponent;
import com.adgsoftware.mydomo.ui.components.AutomationComponent;
import com.adgsoftware.mydomo.ui.components.HeatingComponent;
import com.adgsoftware.mydomo.ui.components.OutletComponent;

/**
 * Settings screen. Allows to add/edit/delete controls & rooms, scenarios, etc.
 */
public class SettingsActivity extends AbstractActivity {

	public static final String PREFS_NAME = "myDomo.settings";
	
	private House house = new House();
	protected LinearLayout layout; // layout containing controllers
	
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		/* Restore old values */
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String serverIP = settings.getString("serverIP", "192.168.0.19"); // TODO define default value?
		String serverPort = settings.getString("serverPort", "1234");
		EditText txtServerIP = (EditText) findViewById(R.id.txtServerIP);
		EditText txtServerPort = (EditText) findViewById(R.id.txtServerPort);
		txtServerIP.setText(serverIP);
		txtServerPort.setText(serverPort);
		
		
		super.onCreate(savedInstanceState);
		
        layout = (LinearLayout) this.findViewById(R.id.rowControllerList);
        createLayout();
		
//		/* Bind the save button */
//		Button btnSave = (Button) this.findViewById(R.id.btnSettingsControllers);
//		btnSave.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showControllersScreen();
//			}
//		});
		
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
		layout.removeAllViews();
		bindService();
	}
	
	/**
	 * Save the given server settings in the shared preferences 
	 * then update the current commander and monitor. 
	 * @param serverIP		Server IP (ex. 192.168.0.26)
	 * @param serverPort	Server port (ex. 1234)
	 */
	private void saveServerSettings() {
		EditText txtServerIP = (EditText) findViewById(R.id.txtServerIP);
		EditText txtServerPort = (EditText) findViewById(R.id.txtServerPort);
		String serverIP = txtServerIP.getText().toString();
		String serverPort = txtServerPort.getText().toString();
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
		
		editor.commit();
//		finish();
	}
	
	/**
	 * Create the main layout with all the nested components.
	 */
	protected void createLayout() {
    	layout.removeAllViews();
//    	setContentView(layout);
		if (myDomoService != null) {
			try {
				house = myDomoService.retrieve();
			} catch (IOException e) {
				house = new House(); // TODO manage better
			}
        }
		
		addComponentsToLayout();
		
	}

	private void addComponentsToLayout() {
		if (myDomoService != null) {
			createConnectedStatusViewer();
			if (house.getGroups() != null) {
				for (Group room : house.getGroups()) {
					for (@SuppressWarnings("rawtypes") Controller controller : room.getControllerList()) {
						if (controller instanceof Light) {
							final AbstractComponent light = createLight((Light) controller, getApplicationContext());
							layout.addView(light);
						}
						if (controller instanceof Automation) {
							final AutomationComponent automation = createAutomation((Automation) controller, getApplicationContext());
							layout.addView(automation);
						}
						if (controller instanceof Heating) {
							final HeatingComponent heating = createHeating((Heating) controller, getApplicationContext());
							LayoutParams params = new LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
							heating.setLayoutParams(params);
							layout.addView(heating);
						}
						if (controller instanceof Outlet) {
							final OutletComponent outlet = createOutlet((Outlet) controller, getApplicationContext());
							layout.addView(outlet);
						}
					}
				}
			}
		}
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
	
	TextView textView;
	
	
	private Handler handler = new Handler() {
        public void  handleMessage(Message msg) {
        	textView.setText(getConnectedStatusMessage());
        	textView.invalidate();
        }
    };
	
	private void createConnectedStatusViewer() {
		textView = new TextView(getApplicationContext());
		
		if (myDomoService.isCommanderConnected()) {
			commandState = COMMAND_ONLINE;
		}
		if (myDomoService.isMonitorConnected()) {
			monitorState = MONITOR_ONLINE;
		}
		
		textView.setText(getConnectedStatusMessage());
		layout.addView(textView);
		
		myDomoService.addCommanderConnectionListener(
				new OpenWebConnectionListener() {
					
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
				new OpenWebConnectionListener() {
					
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

}
