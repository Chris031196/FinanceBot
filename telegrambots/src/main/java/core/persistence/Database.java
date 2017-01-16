package core.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
	
//	private static final String URL = "jdbc:mysql://localhost:3306/venenumbonus?autoReconnect=true&useSSL=false";
//	private static final String USERNAME = "root";
//	private static final String PASSWORD = "sammy";
	
	private static final String URL = "jdbc:mysql://localhost:3306/FinancialSystem?autoReconnect=true&useSSL=false";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "sammy";

	private static Connector con = new Connector(URL, USERNAME, PASSWORD);
	
	private Database(){}
	
	public static ResultSet executeQuery(String sql) {
		if(!con.isConnected()){
			con.connect();
		}
		ResultSet result = null;
		try {
			result = con.getCon().createStatement().executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void executeUpdate(String sql) {
		if(!con.isConnected()){
			con.connect();
		}
		try {
			con.getCon().createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
