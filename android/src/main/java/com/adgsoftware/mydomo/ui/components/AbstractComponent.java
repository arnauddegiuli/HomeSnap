package com.adgsoftware.mydomo.ui.components;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Abstract component encapsulates the elements toggle and its global behaviour:
 * <ul>
 * <li>title: label of the component</li>
 * <li>shown/hidden state</li>
 * </ul>
 * Component itself is included in children classes (light, outlet, heating, automation)
 *
 */
public abstract class AbstractComponent extends LinearLayout {

//	public static final int LABEL_WIDTH = 180;
	
	protected TextView title;
	private android.view.ViewGroup.LayoutParams previousLayoutParams;
	private boolean visible;

	public AbstractComponent(Context context) {
		super(context);
		this.setPadding(0, 10, 0, 10);
		this.title = new TextView(context);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public android.view.ViewGroup.LayoutParams getPreviousLayoutParams() {
		return previousLayoutParams;
	}

	public void setPreviousLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
		this.previousLayoutParams = layoutParams;
	}

	public void setTitle(String text) {
		title.setPadding(5, 0, 5, 0);
		title.setText(text);
//		android.view.ViewGroup.LayoutParams layoutParams = title.getLayoutParams();
//		if (layoutParams != null) {
//			layoutParams.height = LayoutParams.MATCH_PARENT;
//		}
//		title.setGravity(Gravity.CENTER_VERTICAL);
//		title.setTextSize(20);
//		title.setWidth(LABEL_WIDTH);
	}

	public String getTitle() {
		return String.valueOf(title.getText());
	}

}
