package com.adgsoftware.mydomo.android.ui.components;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

public class RoomComponent extends AbstractComponent {
	
	List<AbstractComponent> subComponents;

	public RoomComponent(Context context) {
		super(context);
		this.addView(title);
		this.subComponents = new ArrayList<AbstractComponent>();
		
		this.setBackgroundResource(android.R.drawable.divider_horizontal_bright);
		
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO améliorer la transition quand on clique sur le composant
				for (AbstractComponent component : subComponents) {
//					component.setVisibility(component.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
					if (component.getPreviousLayoutParams() == null) {
						component.setPreviousLayoutParams(component.getLayoutParams());
						component.setLayoutParams(new android.widget.LinearLayout.LayoutParams(0, 0));
					} else {
						component.setLayoutParams(component.getPreviousLayoutParams());
						component.setPreviousLayoutParams(null);
					}
				}
//				final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animated);
//				roomComponent.startAnimation(animation);
			}
		});
	}

	public List<AbstractComponent> getSubComponents() {
		return subComponents;
	}

	public void setSubComponents(List<AbstractComponent> subComponents) {
		this.subComponents = subComponents;
	}
	
	public void setTitle(String text) {
		this.title.setPadding(5, 0, 5, 0);
		this.title.setText(text);
		this.title.setTextSize(30);
		this.title.getLayoutParams().height = LayoutParams.MATCH_PARENT;
		this.title.setGravity(Gravity.CENTER_VERTICAL);
	}

}
