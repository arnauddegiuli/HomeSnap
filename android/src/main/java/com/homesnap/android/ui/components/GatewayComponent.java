package com.homesnap.android.ui.components;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homesnap.R;
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
	private Drawable gateway;
	private Button b = new Button(getContext());
	
	public GatewayComponent(Context context) {
		
		super(context);
		gatewayTxt = new TextView(context);
		gateway = context.getResources().getDrawable(R.drawable.gateway);
		b.setCompoundDrawablesWithIntrinsicBounds(gateway, null, null, null);
		this.addView(title);
		this.addView(gatewayTxt);
		this.setOrientation(LinearLayout.HORIZONTAL);


	}
	
	public void setGateway(final Gateway gateway) {
		gatewayTxt.setId(1);
		gatewayTxt.setText(gateway.getDeviceType());

		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

//				Date date = gateway.getDateTime();
				Version version = gateway.getDistributionVersion();
				Version firmwareVersion = gateway.getFirmwareVersion();
				byte[] ipAddress = gateway.getIpAddress();
				Version kernelVersion = gateway.getKernelVersion();
				byte[] netMask = gateway.getNetMask();
//				Date uptime = gateway.getUpTime();
				
				// 1. Instantiate an AlertDialog.Builder with its constructor
				android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder((Activity) getContext());

				// 2. Chain together various setter methods to set the dialog characteristics
				StringBuffer sb = new StringBuffer("Distribution Version:");
				if (version == null) {
					sb.append(" Not available");
				} else {
					sb.append(version.getBuild()).append(".").append(version.getRelease()).append(".").append(version.getVersion());
				}
				
				if (firmwareVersion == null) {
					sb.append("\nFirmWare Version: Not available");
				} else {
					sb.append("\nFirmWare Version:").append(firmwareVersion.getBuild()).append(".").append(firmwareVersion.getRelease()).append(".").append(firmwareVersion.getVersion());
				}
				
				if (ipAddress == null) {
					sb.append("\nIP address: Not available");
				} else {
					sb.append("\nIP address:").append((ipAddress[0] < 0 ? ipAddress[0] & 0xff : ipAddress[0])).append(".").append((ipAddress[1] < 0 ? ipAddress[1] & 0xff : ipAddress[1])).append(".").append((ipAddress[2] < 0 ? ipAddress[2] & 0xff : ipAddress[2])).append(".").append((ipAddress[3] < 0 ? ipAddress[3] & 0xff : ipAddress[3]));
				}
					
				if (netMask == null) {
					sb.append("\nNetMask: Not available");
				} else {
					sb.append("\nNetMask:").append((netMask[0] < 0 ? netMask[0] & 0xff : netMask[0])).append(".").append((netMask[1] < 0 ? netMask[1] & 0xff : netMask[1])).append(".").append((netMask[2] < 0 ? netMask[2] & 0xff : netMask[2])).append(".").append((netMask[3] < 0 ? netMask[3] & 0xff : netMask[3]));
				}
				
				if (kernelVersion == null) {
					sb.append("\nKernel Version: Not available");
				} else {
					sb.append("\nKernel Version:").append(kernelVersion.getBuild()).append(".").append(kernelVersion.getRelease()).append(".").append(kernelVersion.getVersion());
				}
				
				
				builder.setMessage(sb.toString()
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
