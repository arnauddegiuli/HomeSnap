package com.homesnap.android.ui.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.homesnap.R;
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

	private Button lightButton;
	private Drawable light_on;
	private Drawable light_off;
	private Drawable loading;

	public LightComponent(Context context) {
		super(context);
		lightButton = new Button(context);
		light_off = context.getResources().getDrawable(R.drawable.light_off);
		light_on = context.getResources().getDrawable(R.drawable.light_on);
		loading = context.getResources().getDrawable(R.drawable.load);
//		this.addView(title);
		this.addView(lightButton);
		this.setOrientation(LinearLayout.HORIZONTAL);

	}

	public LightComponent(Context context, final Light light) {
		this(context);
		setLight(light);
	}

	/**
	 * Define the light
	 * @param light
	 */
	public void setLight(final Light light) {
		lightButton.setId(Integer.valueOf(light.getWho()));
		if (LightStatus.LIGHT_ON == light.getWhat()) {
			setOn();
		} else {
			setOff();
		}
		lightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new SetStatusTask().execute(light);
				
			}
		});

		light.addControllerChangeListener(new ControllerChangeListener() {
						
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, final Status newStatus) {
				lightButton.post(new Runnable() {
					public void run() {
						Log.d("Light", "changeWhat: " + newStatus);
						if (Light.LightStatus.LIGHT_ON.equals(newStatus)) {
							setOn();
						} else {
							setOff();
						}
					}
				});
			}

			@Override
			public void onWhatChangeError(
					Controller<? extends Status> controller, final Status oldStatus,
					Status newStatus, CommandResult result) {
				lightButton.post(new Runnable() {
					public void run() {
						Log.d("Light", "changeWhat: " + oldStatus);
						if (Light.LightStatus.LIGHT_ON.equals(oldStatus)) {
							setOn();
						} else {
							setOff();
						}
					}
				});
			}
		});
	}

	private synchronized void setOff() {
		lightButton.setCompoundDrawablesWithIntrinsicBounds(light_off, null, null, null);
	}

	private synchronized void setOn() {
		lightButton.setCompoundDrawablesWithIntrinsicBounds(light_on, null, null, null);
	}

	// To send to commander
	private class SetStatusTask extends AsyncTask<Light, Void, Void> {
		@Override
		protected Void doInBackground(final Light... light) {
			((Activity) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					synchronized (light) {
						Log.d("Light", "Invoking getWhat");
						LightStatus what = light[0].getWhat();
						lightButton.setCompoundDrawablesWithIntrinsicBounds(loading, null, null, null);
						if (Light.LightStatus.LIGHT_ON.equals(what)) {
							Log.d("Light", "setWhat: " + Light.LightStatus.LIGHT_OFF);
							light[0].setWhat(Light.LightStatus.LIGHT_OFF);
						} else {
							Log.d("Light", "setWhat: " + Light.LightStatus.LIGHT_ON);
							light[0].setWhat(Light.LightStatus.LIGHT_ON);
						}
					}
				}
			});
			return null;
		}
	}

//	public void setTitle(String text) {
//		super.setTitle(text);
//		this.title.setGravity(Gravity.BOTTOM);
//	}
}
