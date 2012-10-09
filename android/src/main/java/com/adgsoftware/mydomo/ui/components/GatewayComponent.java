package com.adgsoftware.mydomo.ui.components;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adgsoftware.mydomo.engine.controller.gateway.Gateway;

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
	
	public GatewayComponent(Context context, final Gateway gateway) {
		super(context);
		gatewayTxt = new TextView(context);
		this.addView(title);
		this.addView(gatewayTxt);
		this.setOrientation(LinearLayout.HORIZONTAL);
		gatewayTxt.setId(Integer.valueOf(gateway.getWhere()));
		gatewayTxt.setText(gateway.getDeviceType());


	}
		
	public void setTitle(String text) {
		super.setTitle(text);
		this.title.setGravity(Gravity.TOP);
	}
	
}
