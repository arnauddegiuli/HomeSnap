
import com.homesnap.webserver.rest.MissingParameterRestOperation;
import com.homesnap.webserver.rest.MyDomoRestAPI;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;


public class ListenerExampleParser implements MyDomoRestAPI {

	
	@Override
	public void onHouse() {
		System.out.println("Demande la maison");
	}

	@Override
	public void onLabelList() {
		System.out.println("Demande la liste des labels");
	}

	@Override
	public void onLabel(String labelId) {
		System.out.println("Demande le label [id:" + labelId + "]");
	}

	@Override
	public void onControllerByLabel(String labelId, String where) {
		System.out.println("Demande le controller [labelid:" + labelId + " - where:" + where + "]");
	}

	@Override
	public void onGroupList() {
		System.out.println("Demande la liste des groups");
	}

	@Override
	public void onGroup(String groupId) {
		System.out.println("Demande le group [id:" + groupId + "]");
	}

	@Override
	public void onControllerByGroup(String groupId, String where) {
		System.out.println("Demande le controller [groupId:" + groupId + " - where:" + where + "]");
	}

	@Override
	public void onController(String where) {
		System.out.println("Demande le controller [where:" + where + "]");
	}

	@Override
	public void onStatus(String name, String[] value)
			throws UnsupportedRestOperation, RestOperationException,
			MissingParameterRestOperation {
		for (int i = 0; i < value.length; i++) {
			System.out.println("Status [name:" + name + "] - [value_"+ i + ":" + value[i] + "]");	
		}
		
	}
}
