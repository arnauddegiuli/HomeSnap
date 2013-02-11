package com.adgsoftware.mydomo.ui.components;

import android.content.Context;
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
 */
public class LightComponent extends AbstractComponent {
	
	private ToggleButton lightButton;
	
	public LightComponent(Context context) {
		super(context);
		lightButton = new ToggleButton(context);
		this.addView(title);
		this.addView(lightButton);
		this.setOrientation(LinearLayout.HORIZONTAL);
	}
	
	/**
	 * Define the light
	 * @param light
	 */
	public void setLight(final Light light) {
		
		lightButton.setId(Integer.valueOf(light.getWho()));
		lightButton.setChecked(Light.LightStatus.LIGHT_ON.equals(light.getWhat()));
		
		lightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				lightButton.setText("...");
				lightButton.setChecked(false); // To avoid to the default mechanism run => button always uncheck... will be check only if action occurs.
				new SetStatusTask().execute(light);
			}
		});

		light.addControllerChangeListener(new ControllerChangeListener() {
						
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus) {
				new Thread(new ThreadWithStatus(newStatus)).start();
			}

			@Override
			public void onWhatChangeError(
					Controller<? extends Status> controller, Status oldStatus,
					Status newStatus, CommandResult result) {
				new Thread(new ThreadWithStatus(oldStatus)).start();
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
	
	// To send to commander
	private class SetStatusTask extends AsyncTask<Light, Void, Void> {
		@Override
		protected Void doInBackground(Light... light) {
			Log.d("Light", "Invoking getWhat");
			LightStatus what = light[0].getWhat();
			if (Light.LightStatus.LIGHT_ON.equals(what)) {
				Log.d("Light", "setWhat: " + Light.LightStatus.LIGHT_OFF);
				light[0].setWhat(Light.LightStatus.LIGHT_OFF);
			} else {
				light[0].setWhat(Light.LightStatus.LIGHT_ON);
				Log.d("Light", "setWhat: " + Light.LightStatus.LIGHT_ON);
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
}
