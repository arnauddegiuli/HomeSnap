package com.adgsoftware.mydomo.ui.components;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.ControllerDimension;
import com.adgsoftware.mydomo.engine.controller.ControllerDimensionChangeListener;
import com.adgsoftware.mydomo.engine.controller.DimensionStatus;
import com.adgsoftware.mydomo.engine.controller.DimensionStatusCallback;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.heating.HeatingZone;
import com.adgsoftware.mydomo.engine.controller.heating.dimension.MeasureTemperature;

public class HeatingComponent extends AbstractComponent {
	
	private SeekBar seekBar;
	private ImageButton heat;
	private TextView txtTemp;

	public HeatingComponent(Context context, final HeatingZone heating) {
		super(context);
		seekBar = new SeekBar(context);
		heat = new ImageButton(context);
		txtTemp = new TextView(context);
		txtTemp.setText("...");
		
		
		this.addView(title);
//		this.addView(seekBar);
		this.addView(heat);
		this.addView(txtTemp);
		
		LayoutParams params = new LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
//		seekBar.setPadding(5, 5, 5, 5);
//		seekBar.setMax(100);
//		seekBar.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//		seekBar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
		
		heating.getMeasureTemperature(new DimensionStatusCallback<MeasureTemperature>() {
			
			@Override
			public void value(MeasureTemperature arg0) {
				if (arg0 != null) {
					double temperature = arg0.getMeasuredTemperature(); 
					setTemperaturImage(temperature);
					txtTemp.setText("" + temperature);
				}
			}
		});
		
		
		
//		final OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {}
//
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////				heating.setWhat(CentralHeatingDimension.HeatingZoneDimension.HEATING_CONDITIONING_MANUAL_ADJUSTMENT_MODE); 
//				// TODO how to pass the progress?
//			}
//		};
//		seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
		
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
		
		heating.addControllerDimensionChangeListener(new ControllerDimensionChangeListener() {
			
			@Override
			public void onDimensionChangeError(
					ControllerDimension<? extends Status> arg0, DimensionStatus arg1,
					CommandResult arg2) {
						// TODO manage error		
			}
			
			@Override
			public void onDimensionChange(ControllerDimension<? extends Status> arg0,
					DimensionStatus arg1) {
				setTemperaturImage(((MeasureTemperature) arg1).getMeasuredTemperature());
				
			}
		});

	}

	public ImageButton getComponent() {
		return heat; // TODO a quoi ca sert?
	}

	private void setTemperaturImage(double temp) {
		// TODO manage the system (heat? cold?...  on/off ....)
		heat.setImageResource(com.adgsoftware.mydomo.R.drawable.ic_heat);
	}	
}
