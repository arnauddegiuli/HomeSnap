package com.adgsoftware.mydomo.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adgsoftware.mydomo.R;
import com.adgsoftware.mydomo.engine.house.Label;

public class LabelListAdapter extends BaseAdapter {

	private List<Label> labelList;
	private LayoutInflater mInflater;
	private List<View> cacheView = new ArrayList<View>();

	public LabelListAdapter(Context context,
			List<Label> labelList) {
		super();
		
		this.labelList = labelList;
		
		mInflater = LayoutInflater.from(context);
		
		ImageView imageView;
		TextView titre;
		TextView description;
		View v;
		
		for (Label label : labelList) { // To optimize view
			v = mInflater.inflate(R.layout.list_labels_item_img, null);
			imageView = (ImageView) v.findViewById(R.id.img);
			imageView.setImageResource(R.drawable.list_item_arrow);
			titre = (TextView) v.findViewById(R.id.titre);
			titre.setText(label.getTitle());
			description = (TextView) v.findViewById(R.id.description);
			description.setText("[" + label.getControllerList().size() + " controller(s)]");
			
			cacheView.add(v);
		}
		
		// Add button
		ImageView btnPlus = new ImageView(context);
		btnPlus.setImageResource(R.drawable.add);
		btnPlus.setPadding(0, 10, 0, 5);
		
		cacheView.add(btnPlus);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return cacheView.get(position);
	}

	@Override
	public int getCount() {
		return cacheView.size();
	}


	@Override
	public Object getItem(int position) {
		if (position == labelList.size()) {
			return "add";
		}
		else {
			return labelList.get(position);
		}
	}


	@Override
	public long getItemId(int position) {
		return cacheView.get(position).getId();
	}
}