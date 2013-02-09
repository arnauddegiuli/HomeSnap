package com.adgsoftware.mydomo.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;

/**
 * Light component encapsulates a <em>light</em> toggle button.
 * <ul>
 * <li>title is the label prior to the button</li>
 * </ul>
 * @author ssok
 *
 */
public class LightComponent extends AbstractComponent {
	
	private ToggleButton lightButton;
	
	public LightComponent(Context context, final Light light) {
		super(context);
		lightButton = new ToggleButton(context);
		this.addView(title);
		this.addView(lightButton);
		this.setOrientation(LinearLayout.HORIZONTAL);
		lightButton.setId(Integer.valueOf(light.getWho()));
		lightButton.setChecked(Light.LightStatus.LIGHT_ON.equals(light.getWhat()));
		lightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				lightButton.setText("...");
				new SetWhatTask().execute(light);
			}
		});

		light.addControllerChangeListener(new ControllerChangeListener() {
						
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus) {
				ThreadWithStatus thread = new ThreadWithStatus(newStatus);
				thread.run();
			}

			@Override
			public void onWhatChangeError(
					Controller<? extends Status> controller, Status oldStatus,
					Status newStatus, CommandResult result) {
				ThreadWithStatus thread = new ThreadWithStatus(oldStatus);
				thread.run();
			}
			
		});

	}
	
	private class ThreadWithStatus implements Runnable {
		Status newStatus;
		public ThreadWithStatus(Status newStatus) {
			this.newStatus = newStatus;
		}
		
		public void run() {
			lightButton.post(new Runnable() {
	            public void run() {
	            	Log.d("Light", "changeWhat: " + newStatus);
	    			lightButton.setChecked(Light.LightStatus.LIGHT_ON.equals(newStatus));
	    			lightButton.setText(Light.LightStatus.LIGHT_ON.equals(newStatus) ? "ON" : "OFF");
	            }
	        });
		}
	}
	
	private class SetWhatTask extends AsyncTask<Light, Void, Void> {
		@Override
		protected Void doInBackground(Light... light) {
			Log.d("Light", "Invoking getWhat");
			LightStatus what = light[0].getWhat();
			Log.d("Light", "setWhat: " + what);
			if (Light.LightStatus.LIGHT_ON.equals(what)) {
				light[0].setWhat(Light.LightStatus.LIGHT_OFF);
			} else {
				light[0].setWhat(Light.LightStatus.LIGHT_ON);
			}
			return null;
		}
	}
	
	public ToggleButton getComponent() {
		return lightButton;
	}
	
	public void setTitle(String text) {
		super.setTitle(text);
		this.title.setGravity(Gravity.TOP);
	}

//	@Override
//	public void draw(Canvas canvas) {
//		// TODO Auto-generated method stub
//
//		int h = canvas.getHeight();
//		int l = canvas.getWidth();
//		Paint p = new Paint();
//			p.setColor(Color.BLUE);
//		canvas.drawRect(0, 0, l, h, new Paint());
//		
//	}
	
}
