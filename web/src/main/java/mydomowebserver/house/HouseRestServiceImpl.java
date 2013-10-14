package mydomowebserver.house;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Icon;
import com.adgsoftware.mydomo.engine.house.Label;

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


public class HouseRestServiceImpl implements HouseRestService {

	@Override
	public House getHouse() {
		return getModel();
	}

	@Override
	public void setHouse() {
		// TODO Auto-generated method stub
		
	}

	// /house/label
	@Override
	public Label putLabel(String id, String title) {
		Label label = getLabel(id);
		if (label == null) {
			// Creation
			label = new Label();
			getModel().getLabels().add(label);
		} 
		label.setTitle(title);
		// Save
		return label;
	}

	// /house/label/controller
	@Override
	public Controller<?> putController(String label, String where, String what) {
		// TODO Auto-generated method stub
		return null;
	}

	private House getModel() {
		House house = new House();

		Label label = new Label();
		label.setId("ch1");
		label.setTitle("Chambre 1");
		label.setIcon(Icon.chamber);
		house.getLabels().add(label);

		Light li = new Light();
		li.setTitle("toto");
		li.setWhere("12");
		label.add(li);

		Light li2 = new Light();
		li2.setTitle("Light 2");
		li2.setWhere("13");
		label.add(li2);

		Light li3 = new Light();
		li3.setTitle("Light 3");
		li3.setWhere("14");
		label.add(li3);

		label = new Label();
		label.setId("ch2");
		label.setTitle("Chambre 2");
		label.setIcon(Icon.chamber);
		house.getLabels().add(label);

		Light li4 = new Light();
		li4.setTitle("Light ch2");
		li4.setWhere("15");
		label.add(li4);

		label = new Label();
		label.setId("cui");
		label.setTitle("Cuisine");
		label.setIcon(Icon.chamber);
		house.getLabels().add(label);

		Light li5 = new Light();
		li5.setTitle("Light Cuisine");
		li5.setWhere("16");
		label.add(li5);

		return house;
	}

	private Label getLabel(String id) {
		if (id != null) {
			House house = getModel();

			for (Label label : house.getLabels()) {
				if (id.equals(label.getId())) {
					return label;
				}
			}
		}		
		return null;
	}
}