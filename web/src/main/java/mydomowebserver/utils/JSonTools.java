package mydomowebserver.utils;

/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
import com.adgsoftware.mydomo.engine.house.Group;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;

public class JSonTools {

	public final static String toJson(House house) {
		
		StringBuilder sb = new StringBuilder("[{id:'house', title:'House', labels:[");

		for (Label label : house.getLabels()) {
			sb.append(toJsStringLabel(label));
			sb.append(",");
		}

		if (house.getLabels().size() > 0) {
			sb.setLength(sb.length() - 1);
		}

		sb.append("],groups:[");
		
		for (Group group : house.getGroups()) {
			sb.append(toJsonGroup(group));
			sb.append(",");
		}

		if (house.getGroups().size() > 0) {
			sb.setLength(sb.length() - 1);
		}

		sb.append("]}]");
		return sb.toString();
	}
	
	public final static String toJsonGroup(Group group) {
		StringBuilder sb = new StringBuilder()
		.append("{\"id\":\"").append(group.getId()).append("\"")
		.append(", \"title\":\"").append(group.getTitle()).append("\"")
//		.append(", \"icon\":\"").append(group.getIcon() != null ? group.getIcon().getClassName() : group.getIconPath()).append("\"") TODO add icon management on group
		.append(",\"controllers\":[");

		for (Controller<? extends Status> controller : group.getControllerList()) {
			sb.append(toJson(controller));
			sb.append(",");
		}
		if (group.getControllerList().size() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.append("]}").toString();
	}

	public final static String toJsStringLabel(Label label) {
		
		StringBuilder sb = new StringBuilder()
			.append("{\"id\":\"").append(label.getId()).append("\"")
			.append(", \"title\":\"").append(label.getTitle()).append("\"")
			.append(", \"icon\":\"").append(label.getIcon() != null ? label.getIcon().getClassName() : label.getIconPath()).append("\"")
			.append(",\"controllers\":[");
		
		for (Controller<? extends Status> controller : label.getControllerList()) {
			sb.append(toJson(controller));
			sb.append(",");
		}
		if (label.getControllerList().size() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.append("]}").toString();
	}
	
	public final static String toJson(Controller<?> controller) {
		StringBuilder sb = new StringBuilder()
		.append("{\"where\":\"").append(controller.getWhere()).append("\"")
		.append(", \"who\":\"").append(controller.getWho()).append("\"")
		.append(", \"title\":\"").append(formatString(controller.getTitle())).append("\"");
		
		return sb.append("}").toString();
	}
	
	public final static String formatString(String string) {
		return string.replaceAll("[\"]", "\\\\\"");
	}
		
	public final static String formatJson(Light light) {
		String strStatus = (light.getWhat() == null ? "null" : LightStatus.LIGHT_ON == light.getWhat() ? "on" : "off");
		return "{\"adress\":\""+light.getWhere()+"\",\"status\":\""+ strStatus +"\"}";
	}
}
