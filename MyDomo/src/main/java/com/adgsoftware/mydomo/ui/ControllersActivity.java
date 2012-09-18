package com.adgsoftware.mydomo.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.ui.adapters.ControllerAdapter;

public class ControllersActivity extends AbstractActivity {

	List<Controller<? extends Status>> controllers = new ArrayList<Controller<? extends Status>>();
	private static final int MENU_EDIT_CONTROLLER = 0;
	private static final int MENU_DELETE_CONTROLLER = 1;
	private static final int MENU_EDIT_LABELS = 2;
	
	Label selectedLabel;
	PopupWindow popUp;

	LinearLayout layout;
	boolean listClickable = true;
	
	@Override
	protected void onResume() {
		super.onResume();
		refreshControllers();
		refreshList();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getText(R.string.title_controllers));
		popUp = new PopupWindow(this);
		layout = new LinearLayout(this);
		layout.addView(findViewById(com.adgsoftware.mydomo.R.layout.list_controllers));

		setContentView(R.layout.list_controllers);
		
		ListView listView = (ListView) findViewById(R.id.listview);
		registerForContextMenu(listView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				@SuppressWarnings("unchecked")
				Controller<Status> controller = (Controller<Status>) parent.getItemAtPosition(position);
				Intent intent = new Intent(ControllersActivity.this,
						EditControllerActivity.class);
				intent.putExtra("selectedControllerId", controller.getWhere());
				startActivity(intent);
			}
		});
		
		Bundle extras = getIntent().getExtras();
		String selectedLabelId = String.valueOf(extras.get("selectedLabelId"));
		selectedLabel = getLabel(getHouse(), selectedLabelId);
		
	}

	private ControllerAdapter createAdapter() {
		// fill in the grid_item layout
		ControllerAdapter adapter = new ControllerAdapter(this, controllers);
		return adapter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.controllers_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_add_controller:
			showControllersPopup();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showControllersPopup() {
		// Create a new Label
		if (listClickable) {
			popUp.showAtLocation(layout, Gravity.TOP, 10, 10);
			popUp.update(10, 50, layout.getWidth(), 300);
			popUp.setFocusable(true);
			listClickable = false;
		} else {
			popUp.dismiss();
			listClickable = true;
		}
	}

	private void refreshControllers() {
		Log.d("Controllers", "Load/refresh controllers");

		controllers.clear();

		for (Controller<? extends Status> controller : selectedLabel) {
			controllers.add(controller);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Controller");
		menu.add(0, MENU_EDIT_CONTROLLER, 0, "Edit");
		menu.add(0, MENU_DELETE_CONTROLLER, 1, "Delete");
	}

	public boolean onContextItemSelected(MenuItem menuItem) {
		int pos = ((AdapterContextMenuInfo) menuItem.getMenuInfo()).position;
		Controller<? extends Status> controller = controllers.get(pos);
		switch (menuItem.getItemId()) {
		case MENU_EDIT_CONTROLLER:
			editController(controller);
			return true;
		case MENU_DELETE_CONTROLLER:
			deleteController(controller);
			return true;
		case MENU_EDIT_LABELS:
			editLabels(controller);
			return true;
		default:
			return super.onContextItemSelected(menuItem);
		}
	}

	private void editController(Controller<? extends Status> controller) {
		Log.i("Edit controller", controller.getWhere());
		Intent intent = new Intent(ControllersActivity.this,
				EditControllerActivity.class);
		intent.putExtra("selectedControllerId", controller.getWhere());
		startActivity(intent);
	}

	private void deleteController(Controller<? extends Status> controller) {
		Log.i("Delete controller", controller.getTitle());
		getGroup(getHouse(), computeGroupFromAddress(controller.getWhere())).remove(getController(getHouse(), controller.getWhere()));
		try {
			myDomoService.save(getHouse());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshControllers();
		refreshList();
	}

	private void editLabels(Controller<? extends Status> controller) {
		Log.i("Edit labels of controller", controller.getTitle());
	}

	/**
	 * Load the rooms and their nested controllers, then bind to the list view.
	 */
	private void refreshList() {
		Log.d("Controllers", "Refresh list");
		ListView listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(createAdapter());
	}

//	public void addLightController(Group room, String where) {
//		Light light = myDomoService.createController(Light.class, where);
//		room.add(light);
//		createLight(light, getApplicationContext());
//	}
//
//	public void addHeating(Group room, String where) {
//		Heating heating = myDomoService.createController(Heating.class, where);
//		room.add(heating);
//		createHeating(heating, getApplicationContext());
//	}
//
//	public void addAutomation(Group room, String where) {
//		Automation automation = myDomoService.createController(
//				Automation.class, where);
//		room.add(automation);
//		createAutomation(automation, getApplicationContext());
//	}
}
