package mydomowebserver.house;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Label;

public interface HouseRestService {

	public House getHouse();
	public void setHouse();
	
	public Label putLabel(String label, String title);
	
	public Controller<?> putController(String label, String where, String what);
}
