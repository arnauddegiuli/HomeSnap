package com.adgsoftware.mydomo.android.ui.activities;

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
import com.adgsoftware.mydomo.android.ui.AbstractActivity;
import com.adgsoftware.mydomo.android.ui.adapters.LabelListAdapter;
import com.adgsoftware.mydomo.android.util.ControlException;
import com.adgsoftware.mydomo.engine.house.Label;

/**
 * Main screen. Display a list of labels which group the controllers together.
 */
public class LabelsActivity extends AbstractActivity {

	private static final int RENAME = 0;
	private static final int DELETE = 1;
//	private static final int CAMERA = 2;
	
	private Label labelToModify = null;
	
	// Contextual menu to select action (rename/delete)
	private Builder contextualMenuDialog;

	@Override
	protected void onResume() {
		super.onResume();
		refreshList();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(getResources().getText(R.string.title_labels)); // TODO voir pour sortir cette propriété dans le XML
		setContentView(R.layout.list_labels);

		ListView listLabels = (ListView) findViewById(R.id.listLabels);
		listLabels.setTextFilterEnabled(true);
		registerForContextMenu(listLabels);

		// Associate contextuel menu with long click
		listLabels.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					labelToModify = (Label) parent.getItemAtPosition(position);
					contextualMenuDialog.show();
					return true;
					// Open the label and display controllers
				} catch (Exception e) {
					Log.e("Error when selecting Label", null, e);
					return false;
				}
			}
		});

		listLabels.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Label labelEnum = (Label) parent.getItemAtPosition(position);
				try {
					// Select Label
					Intent intent = new Intent(LabelsActivity.this,
							LabelDetailsGridActivity.class);
					intent.putExtra("selectedLabelId", labelEnum.getId());
					intent.putExtra("selectedLabelTitle", labelEnum.getTitle()); // TODO revoir la facon de passer les paramètre title...
					startActivity(intent);
					// Open the label and display controllers
				} catch (Exception e) {
					Log.e("Error when selecting Label", null, e);
				}
			}
		});

		// Popup creation
		initPopup();

		// Contextual menu creation
		contextualMenuDialog = new AlertDialog.Builder(this).setTitle(
			"Select action").setAdapter(
					createLabelContextualMenu(),
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int position) {
					switch (position) {
					case RENAME:
						showLabelPopup(500);
						break;
					case DELETE:
						try {
							myDomoService.removeLabel(labelToModify.getId());
						} catch (ControlException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						saveHouse();
						break;
//						case CAMERA:
//							try {
//								// Select Label
//								Intent intent = new Intent(LabelsActivity.this,
//										StreamActivity.class);
//								startActivity(intent);
//							} catch (Exception e) {
//								Log.e("Error when launch streaming", null, e);
//							}
//							break;
					default:
						break;
					}
					refreshList();
				}
			}
		);


	}

	private SimpleAdapter createLabelContextualMenu() {
		// TODO clean up this shit mess
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
		
		map = new HashMap<String, String>();
		title = "Streaming";
		map.put("text1", title);
		fillMaps.add(map);
		
		// fill in the grid_item layout
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				android.R.layout.simple_dropdown_item_1line, from, to);
		return adapter;
	}

	@Override
	protected void refreshLayoutAfterServiceConnection() {
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
		if (myDomoService != null) {
			((ListView) findViewById(R.id.listLabels))
				.setAdapter(
				new LabelListAdapter(this.getBaseContext(), myDomoService.getLabels())
			);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_add_label:
			listClickable = true;
			labelToModify = null;
			showLabelPopup(500); // TODO set proper parent width
			return true;
		case R.id.menu_settings:
			showSettingsScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	// Label popup
	
	// Label edition/creation var
	private PopupWindow popUp;
	private LinearLayout layout;
	private TextView lblLabelName;
	private EditText txtLabelName;
	private boolean listClickable = true;
	
	
	private void showLabelPopup(int parentWidth) {
		
		if (labelToModify == null) {
			this.txtLabelName.setText("");
		} else {
			this.txtLabelName.setText(labelToModify.getTitle());
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
	
	private void initPopup() {
		popUp = new PopupWindow(this);
		layout = new LinearLayout(this); // TODO voir pour sortir dans le XML
		lblLabelName = new TextView(this);
		txtLabelName = new EditText(this);
		txtLabelName.setText("");
		txtLabelName.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_NORMAL);
		txtLabelName.setWidth(300);
		Button btnOk = new Button(this);
		btnOk.setText("OK");
		Button btnCancel = new Button(this);
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
				labelToModify = null;
			}
		});

		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (labelToModify == null) {
					try {
						myDomoService.createLabel(null, txtLabelName.getText().toString(), null, null, null, null);
					} catch (ControlException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						myDomoService.modifyLabel(labelToModify.getId(), null, txtLabelName.getText().toString(), null, null, null, null);
					} catch (ControlException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				labelToModify=null;
				popUp.dismiss();
				refreshList();
				saveHouse();
				listClickable = true;
			}
		});
	}
}
