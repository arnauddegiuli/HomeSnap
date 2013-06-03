package com.adgsoftware.mydomo.ui.activities;


import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.ui.AbstractActivity;

/**
 * Controller edition screen.
 * Used when adding or editing a controller.
 */
public class EditControllerActivity extends AbstractActivity {
	
	/* Selected controller holder */
	private Controller<? extends Status> newController = null;
	
	EditText txtControllerTitle = null;
	
	EditText txtControllerWhere = null;
	
	LinearLayout linearAddLabel = null;
	
	TextView txtLabelsCount = null;
	
	String initialWhere = null;

	private static enum Controllers {
		Light(com.adgsoftware.mydomo.engine.controller.light.Light.class),
		Gateway(com.adgsoftware.mydomo.engine.controller.gateway.Gateway.class),
		Automation(com.adgsoftware.mydomo.engine.controller.automation.Automation.class),
		Heating(com.adgsoftware.mydomo.engine.controller.heating.HeatingZone.class),
		Outlet(com.adgsoftware.mydomo.engine.controller.outlet.Outlet.class);

		private Class<? extends Controller<? extends Status>> clazz;

		private Controllers(Class<? extends Controller<? extends Status>> clazz) {
			this.clazz = clazz;
		}

		public Controller<? extends Status> getController()
				throws InstantiationException, IllegalAccessException {
			return myDomoService.createController(clazz, null);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Set selected controller
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// Edition mode
			String selectedControllerId = String.valueOf(extras.get("selectedControllerId"));
			newController = getController(getHouse(), selectedControllerId);
			// Initial where ID in case it is changed afterwards
			initialWhere = newController.getWhere();
			
			// Set known data
			txtControllerWhere.setText(newController.getWhere());
			txtControllerTitle.setText(newController.getTitle());
		} else {
			// Add mode
			newController = myDomoService.createController(Light.class, "0"); // Default value of the list
		}
		
		txtLabelsCount.setText(newController.getLabels().size() + " label(s)");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getText(R.string.title_edit_controller));
		setContentView(R.layout.edit_controller);
	
		txtControllerWhere = (EditText) findViewById(R.id.txtControllerWhere);
		txtControllerTitle = (EditText) findViewById(R.id.txtControllerTitle);
		txtLabelsCount = (TextView) findViewById(R.id.txtLabelsCount);
		
		linearAddLabel = (LinearLayout) findViewById(R.id.linearAddLabel);
		linearAddLabel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditControllerActivity.this,
						SelectLabelsActivity.class);
				startActivity(intent);
			}
		});
		
		// Load the controller types
		Spinner sControllerType = (Spinner) findViewById(R.id.spinnerControllerType);
		ArrayAdapter<?> adapter = new ArrayAdapter<Controllers>(this,
				android.R.layout.simple_list_item_1, Controllers.values());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sControllerType.setAdapter(adapter);
		sControllerType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Controllers controllerEnum = (Controllers) parent.getItemAtPosition(pos);
				Log.i("Item selected", controllerEnum.toString());
				try {
					newController = controllerEnum.getController();
				} catch (Exception e) {
					Log.e("Error when selecting controller", null, e);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do nothing
			}

		});
		
		Button btnSave = (Button) this.findViewById(R.id.btnEditControllerSave);
		btnSave.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				try {

					newController.setWhere(txtControllerWhere.getText().toString());
					newController.setTitle(txtControllerTitle.getText().toString());
	
					// Add controller to the proper group
					String selectedGroupId = computeGroupFromAddress(newController.getWhere());
					Controller<Status> oldController = getController(getHouse(), newController.getWhere());
					Group group = getGroup(getHouse(), selectedGroupId);
					if (oldController != null) {
						Log.i("EditControllerActivity", "Editing controller " + oldController.getWhere());
						group.set(group.indexOf(oldController), (Controller<Status>) newController);
						if (initialWhere == null) {
							// Already a controller with the same ip exist!
							//TODO add a message here!
						} else if (!initialWhere.equals(oldController.getWhere())) {
							// Delete the previous one
							group.remove(group.indexOf(getController(getHouse(), initialWhere)));
						}
					} else {
						Log.i("EditControllerActivity", "Adding controller " + newController.getWhere());
						group.add(newController);
					}
					
					Log.i("Saving controller", newController.toString());
					myDomoService.save(getHouse());
					finish();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
		});
		
		Button btnCancel = (Button) this.findViewById(R.id.btnEditControllerCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
