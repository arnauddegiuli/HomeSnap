package com.homesnap.webserver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.House;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.MissingParameterRestOperation;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.parser.ParseException;


public class RestGetAPITest extends AbstractRestApi {



	@Test
	public void onHouse() {
		// Test to get a house with some group and label
		JSONObject jo = requestJSONObject("/house");

		// Test labels
		JSONArray labels = jo.getJSONArray(House.JSON_LABELS);
		testLabelCh1(labels.getJSONObject(0));
		testLabelCh2(labels.getJSONObject(1));

		// Test Groups
		JSONArray groups = jo.getJSONArray(House.JSON_GROUPS);
		testGroup1(groups.getJSONObject(0));
		System.out.print(jo.toString());
	}

	@Test
	public void onLabelList() {
		// Test to get labels list
		JSONArray labels = requestJSONArray("/house/labels");

		// Test labels
		testLabelCh1(labels.getJSONObject(0));
		testLabelCh2(labels.getJSONObject(1));
	}

	@Test
	public void onLabel() {
		// Test to get a specific label
		JSONObject label = requestJSONObject("/house/labels/ch1");
		// Test label Ch1
		testLabelCh1(label);

		label = requestJSONObject("/house/labels/label?id=ch1");
		// Test label Ch1
		testLabelCh1(label);
	}

	@Test
	public void controllerFromLabel() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
		// Test to get a controller of type light!
		JSONObject jo = requestJSONObject("/house/labels/ch1/controller?id=61");
		testController61(jo);
		
		jo = requestJSONObject("/house/labels/ch1/controller/61");
		testController61(jo);
	}

	@Test
	public void onGroupList() {
		// Test to get groups list
		JSONArray groups = requestJSONArray("/house/groups");

		// Test groups
		testGroup1(groups.getJSONObject(0));
	}

	@Test
	public void onGroup() {
		// Test to get a specific group
		JSONObject group = requestJSONObject("/house/groups/1");
		// Test group 1
		testGroup1(group);

		group = requestJSONObject("/house/groups/group?id=1");
		// Test group 1
		testGroup1(group);

	}

	@Test
	public void onControllerByGroup() {
		// Test to get a controller of type light!
		JSONObject jo = requestJSONObject("/house/groups/1/controller?id=11");
		testController11(jo);

		jo = requestJSONObject("/house/groups/1/controller/11");
		testController11(jo);
	}

	@Test
	public void onController() {
		// TODO Auto-generated method stub
		
	}

	@Test
	public void onStatus() {
		// TODO Auto-generated method stub
		
	}

	private void testController11(JSONObject jo) {
		Assert.assertEquals("Dressing", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("11", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	private void testController12(JSONObject jo) {
		Assert.assertEquals("Suite Parentale", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("12", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	/**
	 * Test that the json object represent the Tom chamber from the house.xml
	 * data sample.
	 * @param jo
	 */
	private void testController61(JSONObject jo) {
		Assert.assertEquals("Chambre Tom", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("61", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	private void testController63(JSONObject jo) {
		Assert.assertEquals("Chambre Marius", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("63", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	private void testLabelCh1(JSONObject label) {
		Assert.assertEquals("ch1", label.get(Label.JSON_ID));
		Assert.assertEquals("Chambre Tom", label.get(Label.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", label.get(Label.JSON_DESCRIPTION));
// TODO		Assert.assertEquals("Un icon", label.get(Label.JSON_ICON));
		JSONArray controllers = label.getJSONArray(Label.JSON_CONTROLLERS);
		testController61(controllers.getJSONObject(0));
		
	}
	
	private void testLabelCh2(JSONObject label) {
		Assert.assertEquals("ch2", label.get(Label.JSON_ID));
		Assert.assertEquals("Chambre Marius", label.get(Label.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", label.get(Label.JSON_DESCRIPTION));
// TODO		Assert.assertEquals("Un icon", label.get(Label.JSON_ICON));
		JSONArray controllers = label.getJSONArray(Label.JSON_CONTROLLERS);
		testController63(controllers.getJSONObject(0));
		
	}

	private void testGroup1(JSONObject group) {
		Assert.assertEquals("1", group.get(Group.JSON_ID));
		Assert.assertEquals("Group 1", group.get(Group.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", group.get(Group.JSON_DESCRIPTION));
		JSONArray controllers = group.getJSONArray(Group.JSON_CONTROLLERS);
		testController11(controllers.getJSONObject(0));
		testController12(controllers.getJSONObject(1));
		
	}
}
