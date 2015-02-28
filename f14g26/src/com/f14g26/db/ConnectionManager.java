package com.f14g26.db;

import java.sql.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.f14g26.model.DbBean;

@ManagedBean(name = "connectionManager")
@SessionScoped
public class ConnectionManager {

	private static ConnectionManager instance = null;
	private String DBUSERNAME;

	public static void setInstance(ConnectionManager instance) {
		ConnectionManager.instance = instance;
	}

	private String DBPASSWORD;
	private String CONN_STRING;
	private Connection conn = null;

	private ConnectionManager() {
	}

	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	private boolean openConnection() {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) facesContext
					.getExternalContext().getSession(false);
			DbBean db = (DbBean) session.getAttribute("dbBean");
			if (db.getServer().equalsIgnoreCase("131.193.209.54:3306")
					|| db.getServer().equalsIgnoreCase("131.193.209.57:3306")) {
				DBUSERNAME = "f14g26";
				DBPASSWORD = "f14g26TCcdQ";
			} else if (db.getServer().equalsIgnoreCase("localhost")) {
				DBUSERNAME = "root";
				DBPASSWORD = "password";
			}
			CONN_STRING = "jdbc:mysql://" + db.getServer() + "/f14g26";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(CONN_STRING, DBUSERNAME,
					DBPASSWORD);
		} catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e) {
			return false;
		} catch (SQLException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
		return true;
	}

	public Connection getConnection() {
		if (conn == null) {
			if (openConnection()) {
				return conn;
			} else {
				return null;
			}
		}
		return conn;
	}

	public void close() {
		try {
			conn.close();
		} catch (NullPointerException e) {
		} catch (SQLException e) {
		}
		conn = null;
	}

}