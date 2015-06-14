package com.homesnap.recorder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.recorder.db.ScriptRunner;

public class dbUtils {

	private String creationSqlScript = "/src/main/sql/creation-v" + Constants.VERSION + ".sql";
	private String updateSqlScript = "/src/main/sql/update-v" + Constants.VERSION + ".sql";
	private String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private String connectionURL = "jdbc:derby:recorder;create=true";
	private Log l = new Log();
	
	private Connection conn;
	
	static {
		try {
			DriverManager
				.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		} catch (SQLException e) {
			System.out.println(" . . . . Impossible to register jdbc Driver.");
			e.printStackTrace();
		}
	}
	
	public dbUtils() {
		try {
			conn = DriverManager.getConnection(connectionURL);
			
			if (!isDBExisting()) {
				System.out.println(" . . . . Creating database version ["
						+ Constants.VERSION + "]");
				InputStream r = getClass().getResourceAsStream(creationSqlScript);
				
				if (r == null) {
					throw new Exception("Creation Script missing...");
				}
				else {
					new ScriptRunner(conn, true, false).runScript(new InputStreamReader(r));
				}
			} else if (!isDBLastVersion()) {
				System.out.println(" . . . . Updating database from version ["
						+ getCurrentDBVersion() + "] to version ["
						+ Constants.VERSION + "]");
				InputStream r = getClass().getResourceAsStream(updateSqlScript);
				if (r == null) {
					throw new Exception("Update Script missing...");
				}
				else {
					new ScriptRunner(conn, true, false).runScript(new InputStreamReader(r));
				}
			}
			
			System.out.println("Current database version [" + getCurrentDBVersion() + "].");
		} catch (Throwable e) {
			System.out.println(" . . . . Error during database creation or update. Check that there is no corruption.");
			e.printStackTrace();
		}
	}

	

	public void writeTemperatureData(String probeId, String value) {
		writeData(probeId, "Temperature", value);
	}
	
	
	public List<Record> readData(String type, String probeId, Timestamp begin, Timestamp end) {
		try {
			   l.finest(Session.Command, "Read data for [type:" + type + "],[probeId:" + probeId + "],[begin:" + begin + "],[end:" + end + "].");
			   
			   List<Object> param = new ArrayList<Object>();
			   StringBuilder sb = new StringBuilder("SELECT PROBE_ID, PROBE_VALUE, PROBE_VALUE_DATE FROM PROBE_DATA");
			   
			   if (type != null) {
				   param.add(type);
				   sb.append(" WHERE PROBE_TYPE = ?");
			   }
			   
			   if (probeId != null) {
				   param.add(probeId);
				   if (param.size() == 1) {
					   sb.append(" WHERE ");
				   } else {
					   sb.append(" AND ");
				   }
				   sb.append("PROBE_ID = ?");
			   }
			   
			   if (begin != null) {
				   param.add(begin);
				   if (param.size() == 1) {
					   sb.append(" WHERE ");
				   } else {
					   sb.append(" AND ");
				   }
				   sb.append("PROBE_VALUE_DATE >= ?");
			   }
			   
			   if (end != null) {
				   param.add(end);
				   if (param.size() == 1) {
					   sb.append(" WHERE ");
				   } else {
					   sb.append(" AND ");
				   }
				   sb.append("PROBE_VALUE_DATE >= ?");
			   }
			   
			   PreparedStatement s = conn.prepareStatement(sb.toString());
			   
			   int i = 1;
			   for (Object object : param) {
				   s.setObject(i++, object);
			   }
			   
			   
			   ResultSet rs = s.executeQuery();
			   List<Record> list = new ArrayList<Record>();

			   
			   while (rs.next()) {
				   Record rcd = new Record();
				   rcd.setProbeId(rs.getString("PROBE_ID"));
				   rcd.setValue(rs.getString("PROBE_VALUE"));
				   rcd.setDate(rs.getTimestamp("PROBE_VALUE_DATE"));
				   rcd.setType(type);
				   list.add(rcd);
			   }
			   
			   s.close();
			  return list;
		}  catch (Throwable e)  {  
			e.printStackTrace();
			 System.out.println ("Impossible to read data");
		}
		return null;
	}

	private void writeData(String probeId, String type, String value) {
		try {
		    
			   System.out.println (" . . . . Write data: " + value);
			   
			   PreparedStatement s = conn.prepareStatement("INSERT INTO PROBE_DATA (PROBE_ID, PROBE_TYPE, PROBE_VALUE, PROBE_VALUE_DATE) VALUES (?, ?, ?, ?)");
			   
			   s.setString(1, probeId);
			   s.setString(2, type);
			   s.setString(3, value);
			   s.setTimestamp(4, new Timestamp(new Date().getTime()));
			   
			   s.executeUpdate();
			   s.close();
			  
		}  catch (Throwable e)  {  
			e.printStackTrace();
			System.out.println (" . . . . Impossible to write data");
		}
	}

	public void close() {
		if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
			   boolean gotSQLExc = false;
			   try {
				  conn.close();
			      DriverManager.getConnection("jdbc:derby:;shutdown=true");
			   } catch (SQLException se)  {
			      if ( se.getSQLState().equals("XJ015") ) {
			         gotSQLExc = true;
			      }
			   }
			   if (!gotSQLExc) {
			      System.out.println("Database did not shut down normally");
			   }  else  {
			      System.out.println("Database shut down normally");
			   }
			}
	}
	
	
	/***      Check for  WISH_LIST table    ****/
	public boolean isDBExisting() throws SQLException {
		try {
			Statement s = conn.createStatement();
			s.execute("update PARAMETER_DATA set DATA_ITEM = 'TEST ENTRY' where 1=3");
			s.close();
		} catch (SQLException sqle) {
			String theError = (sqle).getSQLState();
			/** If table exists will get - WARNING 02000: No row was found **/
			if (theError.equals("42X05")) // Table does not exist
			{
				return false;
			} else if (theError.equals("42X14") || theError.equals("42821")) {
				System.out
						.println("Incorrect table definition. Drop recorder and re-run program.");
				throw sqle;
			} else {
				System.out.println("Unhandled SQLException");
				throw sqle;
			}
		}
		// System.out.println("Just got the warning - table exists OK ");
		return true;
	}

	/*** END wwdInitTable 
	 * @throws SQLException **/

	private boolean isDBLastVersion() throws SQLException {
		return Constants.VERSION.equals(getCurrentDBVersion()); // TODO manage
																// when database
																// version is
																// upper than
																// software
																// version
	}
		
	private String getCurrentDBVersion() throws SQLException {
		Statement s = null;
		try {
			s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT DATA_ITEM FROM PARAMETER_DATA WHERE DATA_ENTRY_ID='version'");
			if (rs.next())
				return rs.getString(1);
			
		} finally {
			if (s != null) {
				s.close();
			}
		}
		
		return "unknow version";
		
	}
}
