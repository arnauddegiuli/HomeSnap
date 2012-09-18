package com.adgsoftware.mydomo.ui.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.house.Label;
import com.adgsoftware.mydomo.ui.AbstractActivity;
import com.adgsoftware.mydomo.ui.adapters.LabelListAdapter;

/**
 * Main screen. Display a list of labels which group the controllers together.
 */
public class MainActivity extends AbstractActivity {

	private static final int RENAME = 0;
	private static final int DELETE = 1;
	
	// Label edition/creation var
	private PopupWindow popUp;
	private LinearLayout layout;
	private TextView lblLabelName;
	private EditText txtLabelName;
	private Button btnOk;
	private Button btnCancel;
	private Label labelToEdit;
	private boolean listClickable = true;
	private boolean labelCreation = false;
	
	// Dialog box to select action (rename/delete)
	private Builder alertDialog;
	private int itemPositionForAlertAction;

	@Override
	protected void onResume() {
		super.onResume();
		bindService();
		refreshList();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getResources().getText(R.string.title_labels));
		
		setContentView(R.layout.list_labels);

		ListView listLabels = (ListView) findViewById(R.id.listLabels);
		listLabels.setTextFilterEnabled(true);

		registerForContextMenu(listLabels);

		popUp = new PopupWindow(this);
		layout = new LinearLayout(this);
		lblLabelName = new TextView(this);
		txtLabelName = new EditText(this);
		txtLabelName.setText("");
		txtLabelName.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_NORMAL);
		txtLabelName.setWidth(300);
		btnOk = new Button(this);
		btnOk.setText("OK");
		btnCancel = new Button(this);
		btnCancel.setText("Cancel");
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.VERTICAL);
		lblLabelName.setText("Label Name");
		layout.addView(lblLabelName, params);
		layout.addView(txtLabelName, params);
		
		LinearLayout btnLayout = new LinearLayout(this);
		btnLayout.setOrientation(LinearLayout.HORIZONTAL);
		btnLayout.addView(btnOk, params);
		btnLayout.addView(btnCancel, params);
		
		layout.addView(btnLayout);
		popUp.setContentView(layout);
		

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popUp.dismiss();
				refreshList();
				listClickable = true;
				labelCreation = false;
			}
		});

		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO control value!
				labelToEdit.setTitle(txtLabelName.getText().toString());
				if (labelCreation) {
					getHouse().getLabels().add(labelToEdit);
					labelCreation = false;
				}
				popUp.dismiss();
				refreshList();
				try {
					myDomoService.save(getHouse());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listClickable = true;
			}
		});

		
		
		alertDialog = new AlertDialog.Builder(this).setTitle(
				"Select action").setAdapter(
						createControllersWithNoLabelAdapter(),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int position) {
						switch (position) {
						case RENAME:
							showAddLabelPopup(500, getHouse().getLabels().get(itemPositionForAlertAction)); // TODO set proper with property
							break;
						case DELETE:
							getHouse().getLabels().get(itemPositionForAlertAction).clear(); // TODO add a method when remove a label => detach all controllers
							getHouse().getLabels().remove(itemPositionForAlertAction);
							saveHouse(getHouse());
							break;
						default:
							break;
						}
						refreshList();
					}
				});

		
		
		listLabels.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == parent.getChildCount()-1) {
					// Add new Label
					showAddLabelPopup(parent.getWidth(), null);
				
					return false;
				} else {
					Label labelEnum = (Label) parent.getItemAtPosition(position);
					Log.i("Item Long selected", labelEnum.toString());
					try {
						itemPositionForAlertAction = position;
						alertDialog.show();
						return true;
						// Open the label and display controllers
					} catch (Exception e) {
						Log.e("Error when selecting Label", null, e);
						return false;
					}
				}
			}
			
		});
		
		listLabels.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == parent.getChildCount()-1) {
					// Add new Label
					showAddLabelPopup(parent.getWidth(), null);
				} else {
					Label labelEnum = (Label) parent.getItemAtPosition(position);
					Log.i("Item selected", labelEnum.toString());
					try {
						// Select Label
						Intent intent = new Intent(MainActivity.this,
								EditLabelActivity.class);
						intent.putExtra("selectedLabelId", labelEnum.getId());
						intent.putExtra("selectedLabelTitle", labelEnum.getTitle());
						startActivity(intent);
						// Open the label and display controllers
					} catch (Exception e) {
						Log.e("Error when selecting Label", null, e);
					}
				}
			}

		});
	}
	
	
	private SimpleAdapter createControllersWithNoLabelAdapter() {
		// create the grid item mapping
		String[] from = new String[] { "text1", "text2" };
		int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

		// prepare the list of all records
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> map = new HashMap<String, String>();
		String title = "Rename";
		map.put("text1", title);
		fillMaps.add(map);

		map = new HashMap<String, String>();
		title = "Delete";
		map.put("text1", title);
		fillMaps.add(map);
		
		// fill in the grid_item layout
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				android.R.layout.simple_dropdown_item_1line, from, to);
		return adapter;

	}
	
	
	private void showAddLabelPopup(int parentWidth, Label labelToModifiy) {
		
		if (labelToModifiy == null) {
			labelToEdit = new Label();
			this.labelCreation = true;
			this.txtLabelName.setText("");
		} else {
			labelToEdit = labelToModifiy;
			this.txtLabelName.setText(labelToModifiy.getTitle());
		}
		
		// Create a new Label
		if (listClickable) {
			popUp.setFocusable(true);
			popUp.showAtLocation(layout, Gravity.TOP, 10, 10);
			popUp.update(10, 50, parentWidth, 300);
			listClickable = false;
		} else {
			popUp.dismiss();
			listClickable = true;
		}
	}

	@Override
	protected void createLayout() {
		refreshList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.labels_menu, menu);
		return true;
	}

	/**
	 * Load the labels, then bind to the list view.
	 */
	private void refreshList() {
		Log.d("Label", "Refresh list");
		ListView listLabels = (ListView) findViewById(R.id.listLabels);
 
        //On attribut à notre listView l'adapter que l'on vient de créer
        listLabels.setAdapter(
        	new LabelListAdapter(this.getBaseContext(), getHouse().getLabels())
        );

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_add_label:
			listClickable = true;
			showAddLabelPopup(500, null); // TODO set proper parent width
			return true;
		case R.id.menu_settings:
			showSettingsScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
