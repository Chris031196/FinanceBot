package core.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

	private String url, username, password;
	private Connection con;

	public Connector(String url, String username, String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public boolean connect(){
		try {
			System.out.println("Checking driver...");
			//looking for the driver
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException thrown while checking driver!");
			return false;
		}

		if(isConnected()) {
			//if already connected, return true
			return true;
		} else {
			//otherwise try to connect
			try {
				System.out.println("Connecting to " +url +"\nusing Username " +username +"\nUsing Passwor " +password);
				con = DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				System.err.println("SQLException thrown while trying to connect!");
				return false;
			}
			return true;
		}
	}
	
	public void disconnect(){
		if(isConnected()){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected(){
		try {
			if(con != null) {
				boolean isValid = con.isValid(5);

				if(!isValid) {
					con = null;
				}

				return isValid;
			} else {
				return false;
			}
		} catch(SQLException e) {
			System.err.println("SQLException thrown while checking Connection!");
			con = null;
			return false;
		}
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Connection getCon() {
		return con;
	}

}
