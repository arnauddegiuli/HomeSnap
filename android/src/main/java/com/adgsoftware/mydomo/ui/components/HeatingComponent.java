package com.adgsoftware.mydomo.ui.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.heating.Heating;
import com.adgsoftware.mydomo.engine.controller.heating.Heating.HeatingStatus;

public class HeatingComponent extends AbstractComponent {
	
	private SeekBar seekBar;

	public HeatingComponent(Context context, final Heating heating) {
		super(context);
		seekBar = new SeekBar(context);
		this.addView(title);
		this.addView(seekBar);
		
		LayoutParams params = new LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		seekBar.setPadding(5, 5, 5, 5);
		seekBar.setMax(100);
		seekBar.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
		seekBar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		
		final OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				heating.setWhat(HeatingStatus.HEATING_CONDITIONING_MANUAL_ADJUSTMENT_MODE); 
				// TODO how to pass the progress?
			}
		};
		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
		
		heating.addControllerChangeListener(new ControllerChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus) {
				//TODO				
			}

			@Override
			public void onWhatChangeError(
					Controller<? extends Status> controller, Status oldStatus,
					Status newStatus, CommandResult result) {
				//TODO
			}
			
		});

	}

	public SeekBar getComponent() {
		return seekBar;
	}

}
