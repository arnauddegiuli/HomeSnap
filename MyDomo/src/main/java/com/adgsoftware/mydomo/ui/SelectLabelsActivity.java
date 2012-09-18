package com.adgsoftware.mydomo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.house.Label;

/**
 * Labels selection screen.
 * Used when to add labels to controllers.
 */
public class SelectLabelsActivity extends AbstractActivity {

	List<Label> labels = new ArrayList<Label>();

	@Override
	protected void onResume() {
		super.onResume();
		refreshLabels();
		refreshList();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_labels);

		ListView listLabels = (ListView) findViewById(R.id.listLabels);
		registerForContextMenu(listLabels);

		listLabels.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO select this label
			}
		});
	}

	private SimpleAdapter createAdapter() {
		// create the grid item mapping
		String[] from = new String[] { "title", "subTitle", "value" };
		int[] to = new int[] { R.id.labelTitle, R.id.labelSubTitle, R.id.checkboxItem };

		// prepare the list of all records
		List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
		int i = 0;
		for (Label label : labels) {
			HashMap<String, String> map = new HashMap<String, String>();
			i++;
			String title = label.getTitle() == null ? "Label " + i : label
					.getTitle();
			map.put("title", title);
//			map.put("subTitle", label.getSubLabelList().size() + " sub-labels");
			fillMaps.add(map);
		}

		// fill in the grid_item layout
		SimpleAdapter adapter = new SimpleAdapter(this, fillMaps,
				R.layout.list_labels_item_checkbox, from, to);
		return adapter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.labels_menu, menu);
		return true;
	}

	private void refreshLabels() {
		Log.d("Labels", "Load/refresh labels");

		labels.clear();
		List<Label> labels = getHouse().getLabels();
		
		this.labels = labels;
	}

	/**
	 * Load the labels, then bind to the list view.
	 */
	private void refreshList() {
		Log.d("Controllers", "Refresh list");
		ListView listLabels = (ListView) findViewById(R.id.listLabels);
		listLabels.setAdapter(createAdapter());
	}
	
	public void selectLabel(View v) {
		CheckBox cb = (CheckBox) v;
		// on r�cup�re la position � l'aide du tag d�fini dans la classe
		// MyListAdapter
		Log.d("Select label", String.valueOf(cb.getTag()));
	}

}
