package com.homesnap.webserver.rest;




public interface MyDomoRestAPI {

	public void onHouse() throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onLabelList() throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onLabel(String labelId) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onControllerByLabel(String labelId, String where) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onGroupList() throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onGroup(String groupId) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onControllerByGroup(String groupId, String where) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onController(String where) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
//	public void onStatus(String name, String[] value) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
}
