package com.adgsoftware.mydomo.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.ui.AbstractActivity;
import com.adgsoftware.mydomo.ui.adapters.ControllerAdapter;

/**
 * Label edition screen. Used when editing a label to add controllers to it.
 */
public class EditLabelActivity extends AbstractActivity {

	List<Controller<? extends Status>> labelControllers = new ArrayList<Controller<? extends Status>>();
	List<Controller<? extends Status>> controllersWithoutLabel = new ArrayList<Controller<? extends Status>>();
	private static final int MENU_EDIT_CONTROLLER = 0;
	private static final int MENU_DELETE_CONTROLLER = 1;

	Label selectedLabel;
	PopupWindow popUp;
	Builder alertDialog;

	private String selectedLabelId;

	@Override
	protected void onResume() {
		super.onResume();
		refreshControllers();
		refreshList();

		if (selectedLabel.isEmpty()) {
			alertDialog.show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getText(R.string.title_edit_label));
		setContentView(R.layout.edit_label);
		popUp = new PopupWindow(this);

		// controllersWithoutLabelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Set selected label
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			selectedLabelId = String.valueOf(extras.get("selectedLabelId"));
		} else {
			Log.e("Edit label", "You must call the activity with a label ID");
		}

		// Bind list view actions
		ListView listView = (ListView) findViewById(R.id.listControllersForLabel);
		registerForContextMenu(listView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_label_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_add_controller:
			showControllersPopup();
			return true;
		case R.id.menu_settings:
			showSettingsScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showControllersPopup() {
		this.alertDialog.show();
	}

	/**
	 * Load the label and its nested controllers, then bind to the list view.
	 */
	private void refreshList() {
		Log.d("Edit label", "Refresh list");
		ListView listView = (ListView) findViewById(R.id.listControllersForLabel);
		listView.setAdapter(createLabelControllersAdapter());

		// Create the adapter according to the list of controllers without label
		SimpleAdapter controllersWithoutLabelAdapter = createControllersWithNoLabelAdapter();
		
		if (controllersWithoutLabel.isEmpty()) {
			alertDialog = new AlertDialog.Builder(this).setMessage(getResources().getText(R.string.message_no_controller_without_label)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					showSettingsScreen();
				}
			});	
		} else {
			alertDialog = new AlertDialog.Builder(this).setTitle(
					getResources().getText(R.string.title_controllers_without_label)).setAdapter(
				controllersWithoutLabelAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int position) {
						Log.d("Edit label dialog", String.valueOf(position));
						Controller<? extends Status> selectedController = controllersWithoutLabel.get(position);
						selectedLabel.add(selectedController);
						saveHouse(getHouse());
						refreshControllers();
						refreshList();
					}
				});
		}
	}
	
	private ControllerAdapter createLabelControllersAdapter() {
		// fill in the grid_item layout
		ControllerAdapter adapter = new ControllerAdapter(this, labelControllers);
		return adapter;
	}

	private SimpleAdapter createControllersWithNoLabelAdapter() {
		// create the grid item mapping
		String[] from = new String[] { "text1", "text2" };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

		// prepare the list of all records
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		int i = 0;
		for (Controller<? extends Status> controller : controllersWithoutLabel) {
			HashMap<String, String> map = new HashMap<String, String>();
			i++;
			String title = controller.getTitle() == null ? "Controller " + i
					: controller.getTitle();
			map.put("text1", title);
			Log.d("Edit label", "Add orphan controller " + title);
			fillMaps.add(map);
		}

		// fill in the grid_item layout
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				android.R.layout.simple_dropdown_item_1line, from, to);
		return adapter;

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Controller");
		menu.add(0, MENU_EDIT_CONTROLLER, 0, "Edit controller");
		menu.add(0, MENU_DELETE_CONTROLLER, 1, "Delete controller");
	}

	public boolean onContextItemSelected(MenuItem menuItem) {
		int pos = ((AdapterContextMenuInfo) menuItem.getMenuInfo()).position;
		Controller<? extends Status> controller = labelControllers.get(pos);
		switch (menuItem.getItemId()) {
		case MENU_EDIT_CONTROLLER:
			editController(controller);
			return true;
		case MENU_DELETE_CONTROLLER:
			deleteController(controller);
			return true;
		default:
			return super.onContextItemSelected(menuItem);
		}
	}

	private void editController(Controller<? extends Status> controller) {
		showEditControllerScreen(controller.getWhere());
	}

	private void deleteController(Controller<? extends Status> controller) {
		Log.i("Delete controller", controller.getTitle());
		selectedLabel.remove(controller);
		saveHouse(getHouse());
		refreshControllers();
		refreshList();
	}

	private void refreshControllers() {
		Log.d("Controllers", "Load/refresh controllers");

		// Reload label from back-end
		selectedLabel = getLabel(getHouse(), selectedLabelId);

		// Load controllers without label
		controllersWithoutLabel.clear();
		for (Group group : getHouse().getGroups()) {
			for (Controller<? extends Status> controller : group
					.getControllerList()) {
				if (controller.getLabels() == null
						|| controller.getLabels().isEmpty()) {
					controllersWithoutLabel.add(controller);
				}
			}
		}

		// Load controllers of this label
		labelControllers.clear();
		for (Controller<? extends Status> controller : selectedLabel) {
			labelControllers.add(controller);
		}
	}

}
