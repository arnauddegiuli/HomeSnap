package com.adgsoftware.mydomo.android.ui.components;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.automation.Automation;

public class AutomationComponent extends AbstractComponent {
	
	RadioButton radioButtonUp = null;
	RadioButton radioButtonStop = null;
	RadioButton radioButtonDown = null;
	
	public AutomationComponent(Context context, final Automation automation) {
		super(context);
		this.addView(title);
		
		radioButtonUp = new RadioButton(context);
		radioButtonUp.setText("Up");
		radioButtonUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				automation.setWhat(Automation.AutomationStatus.AUTOMATION_UP);
			}
		});

		radioButtonStop = new RadioButton(context);
		radioButtonStop.setText("Idle");
		radioButtonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				automation.setWhat(Automation.AutomationStatus.AUTOMATION_STOP);
			}
		});

		radioButtonDown = new RadioButton(context);
		radioButtonDown.setText("Down");
		radioButtonDown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				automation.setWhat(Automation.AutomationStatus.AUTOMATION_DOWN);
			}
		});
		
		automation.addControllerChangeListener(new ControllerChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus) {
				switch (automation.getWhat()) {
				case AUTOMATION_UP:
					radioButtonUp.setChecked(true);
					break;
				case AUTOMATION_STOP:
					radioButtonStop.setChecked(true);
					break;
				case AUTOMATION_DOWN:
					radioButtonDown.setChecked(true);
					break;
				default:
					break;
				}
			}

			@Override
			public void onWhatChangeError(
					Controller<? extends Status> controller, Status oldStatus,
					Status newStatus, CommandResult result) {
				
			}
			
		});
		
		RadioGroup radioGroup = new RadioGroup(context);
		radioGroup.addView(radioButtonUp);
		radioGroup.addView(radioButtonStop);
		radioGroup.addView(radioButtonDown);
		
		this.addView(radioGroup);

	}

	@Override
	public void setTitle(String text) {
		super.setTitle(text);
		this.title.setGravity(Gravity.TOP);
		this.title.setPadding(5, 13, 0, 0);
	}

}
