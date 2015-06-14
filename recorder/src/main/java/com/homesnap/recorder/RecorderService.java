package com.homesnap.recorder;

import java.util.List;

public interface RecorderService {
	
	public List<Record> getRecorderValueList();
	public List<Record> getTypeValueList(String type);
	public List<Record> getProbeValueList(String type, String probeId);
	public List<Record> getDayValueList(String type, String probeId, int year, int month, int day);
	public List<Record> getMonthValueList(String type, String probeId, int year, int month);
	public List<Record> getYearValueList(String type, String probeId, int year);
}
