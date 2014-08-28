package com.homesnap.android.ui.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.homesnap.R;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.Label;
import com.homesnap.android.ui.AbstractActivity;
import com.homesnap.android.ui.adapters.ControllerAdapter;

public class LabelDetailsGridActivity extends AbstractActivity {

	private Label selectedLabel;
	private Builder alertDialog;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	 	setContentView(R.layout.main);
	    
	 // Set selected label
	 		Bundle extras = getIntent().getExtras();
	 		if (extras != null) {
	 			String selectedLabelId = String.valueOf(extras.get("selectedLabelId"));
	 			selectedLabel = getLabel(getHouse(), selectedLabelId);
	 			setTitle(String.valueOf(extras.get("selectedLabelTitle"))); // TODO remove this
	 		} else {
	 			Log.e("Edit label", "You must call the activity with a label ID");
	 		}
	 		
	 		refreshLayoutAfterServiceConnection(); // TODO
	}
	
	protected void refreshLayoutAfterServiceConnection() {
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ControllerAdapter(this, getCon()));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(LabelDetailsGridActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
	private List<Controller<? extends Status>> getCon() {
		Log.d("Controllers", "Load/refresh controllers");

		
		// Load controllers of this label
		 List<Controller<? extends Status>> labelControllers = new ArrayList<Controller<? extends Status>>();
		for (Controller<? extends Status> controller : selectedLabel) {
			labelControllers.add(controller);
		}
		return labelControllers;
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
//			showControllersPopup();
			return true;
		case R.id.menu_settings:
			showSettingsScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
