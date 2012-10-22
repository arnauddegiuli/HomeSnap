package com.adgsoftware.mydomo.ui.components;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;
import com.adgsoftware.mydomo.engine.controller.gateway.Version;

/**
 * Gateway component encapsulates a <em>gateway</em>.
 * <ul>
 * <li>title is the label prior to the button</li>
 * </ul>
 * @author adegiuli
 *
 */
public class GatewayComponent extends AbstractComponent {
	
private TextView gatewayTxt;
	
	public GatewayComponent(final Gateway gateway, final Activity activity) {
		
		super(activity.getApplicationContext());
		Context context = activity.getApplicationContext();
		gatewayTxt = new TextView(context);
		this.addView(title);
		this.addView(gatewayTxt);
		this.setOrientation(LinearLayout.HORIZONTAL);
		gatewayTxt.setId(1);
		gatewayTxt.setText(gateway.getDeviceType());

		final Button b = new Button(context);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Date date = gateway.getDateTime();
				Version version = gateway.getDistributionVersion();
				Version firmwareVersion = gateway.getFirmwareVersion();
				byte[] ipAddress = gateway.getIpAddress();
				Version kernelVersion = gateway.getKernelVersion();
				byte[] netMask = gateway.getNetMask();
				Date uptime = gateway.getUpTime();
				
				// 1. Instantiate an AlertDialog.Builder with its constructor
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("Distribution Version:" +  version.getBuild() + "." + version.getRelease() + "." + version.getVersion()
						+ "\nFirmWare Version:" +  firmwareVersion.getBuild() + "." + firmwareVersion.getRelease() + "." + firmwareVersion.getVersion()
						+ "\nIP address:" + (ipAddress[0] < 0 ? ipAddress[0] & 0xff : ipAddress[0]) + "." + (ipAddress[1] < 0 ? ipAddress[1] & 0xff : ipAddress[1]) + "." + (ipAddress[2] < 0 ? ipAddress[2] & 0xff : ipAddress[2]) + "." + (ipAddress[3] < 0 ? ipAddress[3] & 0xff : ipAddress[3])
						+ "\nNetMask:" + (netMask[0] < 0 ? netMask[0] & 0xff : netMask[0]) + "." + (netMask[1] < 0 ? netMask[1] & 0xff : netMask[1]) + "." + (netMask[2] < 0 ? netMask[2] & 0xff : netMask[2]) + "." + (netMask[3] < 0 ? netMask[3] & 0xff : netMask[3])
						+ "\nKernel Version:" +  kernelVersion.getBuild() + "." + kernelVersion.getRelease() + "." + kernelVersion.getVersion()
						)
				       .setTitle("Gateway information");


				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User clicked OK button
				           }
				       });
				
				// 3. Get the AlertDialog from create()
				android.app.AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		this.addView(b);

	}
		
	public void setTitle(String text) {
		super.setTitle(text);
		this.title.setGravity(Gravity.TOP);
	}

}
