package mydomowebserver;

import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.Path;

import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.ControllerChangeListener;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;
import com.adgsoftware.mydomo.engine.services.ControllerService;
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
// SEE THAT: https://github.com/jersey/jersey/tree/master/examples/osgi-http-service/bundle
@Path( "/light" )
public class LightRestServiceImpl implements LightRestService {

	public void log(String message) {
		System.out.println(message);
	}
	
	private ControllerService service = new ControllerServiceImpl("localhost", 1234); // TODO change it!
	private Map<String, Controller> controllerList = new Hashtable<String, Controller>();
	
	@Override
	public LightStatus command(LightStatus status, String adress) {
		
		Light l = service.createController(Light.class, adress);
		l.addControllerChangeListener(new ControllerChangeListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onWhatChangeError(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus, CommandResult result) {
				this.notify();
			}
			
			@Override
			public void onWhatChange(Controller<? extends Status> controller,
					Status oldStatus, Status newStatus) {
				this.notify();
			}
		});
		
		if (LightStatus.LIGHT_ON.equals(status)) {
			l.setWhat(LightStatus.LIGHT_ON);
		} else if (LightStatus.LIGHT_OFF.equals(status)) {
			l.setWhat(LightStatus.LIGHT_OFF);
		}
		
		try {
			this.wait(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l.getStatus(adress);
	}
	
	private Controller<Status> getController(Class<Controller> clazz, String adress) {
		Controller<Status> result = controllerList.get(adress);
		if(result == null) {
			result = service.createController(clazz, adress);
			controllerList.put(adress, result);
		}
		
		return result;
	}

	@Override
	public LightStatus status(String adress) {
		// TODO Auto-generated method stub
		Light l = service.createController(Light.class, adress);
		return l.getWhat();
	}
}
