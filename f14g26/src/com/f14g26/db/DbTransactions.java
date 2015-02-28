package com.f14g26.db;

import java.sql.*;

import com.f14g26.model.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.f14g26.model.UserBean;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

@ManagedBean(name = "dbTransactions")
@SessionScoped
public class DbTransactions {

	private static Connection conn = null;
	private String connectionStatus = null;

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	// Getters and setters for conn
	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		DbTransactions.conn = conn;
	}

	public static Connection getConnection() throws NullPointerException,
			SQLException {
		conn = ConnectionManager.getInstance().getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	public String insertUser() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		UserBean ub = (UserBean) session.getAttribute("userBean");
		ub.setRegisterMessage("");
		// Populate session id and client ip of UserBean
		ub.setClientip(request.getRemoteAddr());
		ub.setSessionid(request.getSession().getId());

		String userinsert = "INSERT INTO all_users(user_name, user_password, first_name, last_name, email) VALUES(?,?,?,?,?)";
		String creationstatus = "REGISTRATIONFAILED";
		try {
			PreparedStatement pstmt = getConnection().prepareStatement(
					userinsert);
			pstmt.setString(1, ub.getUsername());
			pstmt.setString(2, ub.getPassword());
			pstmt.setString(3, ub.getFirstname());
			pstmt.setString(4, ub.getLastname());
			pstmt.setString(5, ub.getEmail());
			conn.setAutoCommit(false);
			int status = pstmt.executeUpdate();

			if (status == 1) {
				creationstatus = "REGISTERED";
				ub.setRegisterMessage("Successfully registered. Please login to continue");
				writeLog(ub.getUsername(), ub.getSessionid(), ub.getClientip(),
						"Register");
				conn.commit();
				ConnectionManager.getInstance().close();
				// Reset the bean values
				ub.setUsername("");
				ub.setPassword("");
				ub.setFirstname("");
				ub.setLastname("");
				ub.setEmail("");
				ub.setDisplayRegisterMessage(true);
				ub.setDisplayLoginMessage(false);
				return creationstatus;

			} else {
				ub.setUsername("");
				ub.setPassword("");
				ub.setEmail("");
				ub.setRegisterMessage("User registration failed");
				ub.setDisplayRegisterMessage(false);
				ub.setDisplayLoginMessage(false);
				conn.rollback();
				return creationstatus;
			}
		} catch (MySQLIntegrityConstraintViolationException e) {
			ub.setUsername("");
			ub.setPassword("");
			ub.setEmail("");
			ub.setRegisterMessage("User / Email already exists");
			creationstatus = "REGISTRATIONFAILED";
			ub.setDisplayRegisterMessage(false);
			ub.setDisplayLoginMessage(false);
			ConnectionManager.getInstance().close();
			// e.printStackTrace();
			return creationstatus;
		} catch (MySQLSyntaxErrorException e) {
			ub.setUsername("");
			ub.setPassword("");
			ub.setEmail("");
			ub.setRegisterMessage("Unable to register. Please contact Admin");
			ub.setDisplayRegisterMessage(false);
			ub.setDisplayLoginMessage(false);
			creationstatus = "REGISTRATIONFAILED";
			ConnectionManager.getInstance().close();
			// e.printStackTrace();
			return creationstatus;
		} catch (NullPointerException e) { // Null pointer exception occurs when
											// the DB connection is not created
			ub.setUsername("");
			ub.setPassword("");
			ub.setEmail("");
			ub.setRegisterMessage("Unable to connect to the database. User registration failed");
			ub.setDisplayRegisterMessage(false);
			ub.setDisplayLoginMessage(false);
			creationstatus = "REGISTRATIONFAILED";
			// e.printStackTrace();
			ConnectionManager.getInstance().close();
			return creationstatus;
		} catch (SQLException e) {
			ub.setUsername("");
			ub.setPassword("");
			ub.setEmail("");
			ub.setRegisterMessage("User registration failed");
			ub.setDisplayRegisterMessage(false);
			ub.setDisplayLoginMessage(false);
			creationstatus = "REGISTRATIONFAILED";
			ConnectionManager.getInstance().close();
			// e.printStackTrace();
			return creationstatus;
		} catch (Exception e) {
			ub.setUsername("");
			ub.setPassword("");
			ub.setEmail("");
			ub.setRegisterMessage("User registration failed");
			ub.setDisplayRegisterMessage(false);
			ub.setDisplayLoginMessage(false);
			creationstatus = "REGISTRATIONFAILED";
			ConnectionManager.getInstance().close();
			// e.printStackTrace();
			return creationstatus;
		}
	}

	public String checkUser() {
		String checkusersql = "SELECT COUNT(*) FROM all_users WHERE user_name = ? AND user_password = ? AND active = \"Y\"";
		String getFirstName = "SELECT first_name FROM all_users WHERE user_name = ?";

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		UserBean ub = (UserBean) session.getAttribute("userBean");

		String status = "USERDOESNTEXISTS";
		try {
			ub.setDisplayLoginMessage(false);
			ub.setLoginMessage("");
			// Populate session id and client ip of UserBean
			ub.setClientip(request.getRemoteAddr());
			ub.setSessionid(request.getSession().getId());
			ub.setDisplayRegisterMessage(false);
			if (ub.getUsername().equals("f14g26")
					&& ub.getPassword().equals("F14G26")) {
				status = "ADMINUSER";
				// Setting a session attribute to handle the logout
				// functionality
				session.setAttribute("logout", "no");
				return status;

			} else {
				PreparedStatement pstmt = getConnection().prepareStatement(
						checkusersql);
				pstmt.setString(1, ub.getUsername());
				pstmt.setString(2, ub.getPassword());
				ResultSet rs = pstmt.executeQuery();
				rs.next();

				if (rs.getInt(1) == 1) {
					writeLog(ub.getUsername(), ub.getSessionid(),
							ub.getClientip(), "Login");
					// Populate First name
					PreparedStatement pstmt2 = getConnection()
							.prepareStatement(getFirstName);
					pstmt2.setString(1, ub.getUsername());
					ResultSet rs2 = pstmt2.executeQuery();
					rs2.next();
					ub.setFirstname(rs2.getString(1));
					conn.commit();
					ConnectionManager.getInstance().close();
					status = "NORMALUSER";
					// Setting a session attribute to handle the logout
					// functionality
					session.setAttribute("logout", "no");
					return status;
				} else {
					writeLog(ub.getUsername(), ub.getSessionid(),
							ub.getClientip(), "FailLogin");
					conn.commit();
					ConnectionManager.getInstance().close();
					ub.setPassword("");
					ub.setDisplayLoginMessage(true);
					ub.setLoginMessage("Invalid username / password");
					return status;
				}
			}
		} catch (MySQLSyntaxErrorException e) {
			ub.setUsername("");
			ub.setPassword("");
			ub.setEmail("");
			ub.setLoginMessage("Login failed. Please contact Admin");
			// e.printStackTrace();
			ub.setDisplayLoginMessage(true);
			ConnectionManager.getInstance().close();
			return status;
		} catch (SQLException e) {
			ub.setPassword("");
			ub.setLoginMessage("Unable to login. Please contact admin");
			ub.setDisplayLoginMessage(true);
			ConnectionManager.getInstance().close();
			return status;
		} catch (NullPointerException e) { // Null pointer exception occurs when
			// the DB connection is not created
			// e.printStackTrace();
			ub.setPassword("");
			ub.setLoginMessage("Cannot connect to database. Unable to login");
			ub.setDisplayLoginMessage(true);
			ConnectionManager.getInstance().close();
			return status;
		} catch (Exception e) { // Null pointer exception occurs when
			// the DB connection is not created
			// e.printStackTrace();
			ub.setPassword("");
			ub.setLoginMessage("Cannot connect to database. Unable to login");
			ub.setDisplayLoginMessage(true);
			ConnectionManager.getInstance().close();
			return status;
		}

	}

	public static void writeLog(String user_name, String session_id,
			String client_ip, String action) throws NullPointerException,
			MySQLSyntaxErrorException, SQLException {
		String insertLog = "INSERT INTO user_log(user_name, session_id, client_ip, activity) VALUES (?,?,?,\""
				+ action + "\")";

		PreparedStatement pstmt = getConnection().prepareStatement(insertLog);
		pstmt.setString(1, user_name);
		pstmt.setString(2, session_id);
		pstmt.setString(3, client_ip);
		int status = pstmt.executeUpdate();

		if (status == 1) {
			conn.commit();

		} else {
		}
	}

	public String createUserTable() {

		String usertable = "CREATE TABLE all_users(user_id INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,user_name CHAR(30) NOT NULL UNIQUE,user_password CHAR(50) NOT NULL,first_name CHAR(50),last_name CHAR(50),email CHAR(80) NOT NULL UNIQUE,creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,active CHAR(1) DEFAULT \"Y\")";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UserBean ub = (UserBean) session.getAttribute("userBean");
		ub.setMessage("");
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				PreparedStatement pstmt = getConnection().prepareStatement(
						usertable);
				int result = pstmt.executeUpdate();
				if (result == 0) {
					conn.commit();
					ConnectionManager.getInstance().close();
					ub.setMessage("User table successfully created");
					return "USERTABLECREATED";
				} else {
					ub.setMessage("User table creation failed");
					return "USERTABLENOTCREATED";
				}
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
				// This exception occurs when we create table twice
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("User table already exists");
				return "USERTABLENOTCREATED";
			} catch (SQLException e) {
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("User table creation failed");
				return "USERTABLENOTCREATED";
			} catch (NullPointerException e) { // Null pointer exception occurs
												// when
				// the DB connection is not created
				ub.setMessage("Cannot establish connection to the database");
				// e.printStackTrace();
				return "USERTABLENOTCREATED";
			} catch (Exception e) {
				// e.printStackTrace();
				return "USERTABLENOTCREATED";
			}
		}

	}

	public String createLogTable() {
		String logtable = "CREATE TABLE user_log(log_id INT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,user_name CHAR(30),session_id CHAR(100),client_ip CHAR(100),activity CHAR(20),date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UserBean ub = (UserBean) session.getAttribute("userBean");
		ub.setMessage("");
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				PreparedStatement pstmt = getConnection().prepareStatement(
						logtable);
				int result = pstmt.executeUpdate();
				if (result == 0) {
					conn.commit();
					ConnectionManager.getInstance().close();
					ub.setMessage("Log table successfully created");
					return "LOGTABLECREATED";
				} else {
					ub.setMessage("Log table creation failed");
					return "LOGTABLENOTCREATED";
				}
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
				// This exception occurs when we create table twice
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("Log table already exists");
				return "LOGTABLENOTCREATED";
			} catch (SQLException e) {
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("Log table creation failed");
				return "LOGTABLENOTCREATED";
			} catch (NullPointerException e) { // Null pointer exception occurs
												// when
				// the DB connection is not created
				ub.setMessage("Cannot establish connection to the database");
				// e.printStackTrace();
				return "LOGTABLENOTCREATED";
			} catch (Exception e) {
				// e.printStackTrace();
				return "LOGTABLENOTCREATED";
			}
		}
	}

	public String dropUserTable() {
		String droptable = "DROP TABLE all_users";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UserBean ub = (UserBean) session.getAttribute("userBean");
		ub.setMessage("");
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				PreparedStatement pstmt = getConnection().prepareStatement(
						droptable);
				int status = pstmt.executeUpdate();
				if (status == 0) {
					conn.commit();
					ConnectionManager.getInstance().close();
					ub.setMessage("User table successfully dropped");
					return "USERTABLEDROPPED";
				} else {
					ub.setMessage("User table drop failed");
					return "USERTABLEDROPFAIL";
				}
			} catch (MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("User table drop failed. Table does not exist");
				return "USERTABLEDROPFAIL";
			} catch (SQLException e) {
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("User table drop failed");
				return "USERTABLEDROPFAIL";
			} catch (NullPointerException e) { // Null pointer exception occurs
												// when
				// the DB connection is not created
				ub.setMessage("Cannot establish connection to the database");
				// e.printStackTrace();
				return "USERTABLEDROPFAIL";
			} catch (Exception e) {
				// e.printStackTrace();
				return "USERTABLEDROPFAIL";
			}
		}
	}

	public String dropLogTable() {
		String droptable = "DROP TABLE user_log";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UserBean ub = (UserBean) session.getAttribute("userBean");
		ub.setMessage("");
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				PreparedStatement pstmt = getConnection().prepareStatement(
						droptable);
				int status = pstmt.executeUpdate();
				if (status == 0) {
					conn.commit();
					ConnectionManager.getInstance().close();
					ub.setMessage("Log table successfully dropped");
					return "LOGTABLESUCCESS";
				} else {
					ub.setMessage("Log table drop failed");
					return "LOGTABLEDROPFAIL";
				}
			} catch (MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("Log table drop failed. Table does not exist");
				return "LOGTABLEDROPFAIL";
			} catch (SQLException e) {
				// e.printStackTrace();
				ConnectionManager.getInstance().close();
				ub.setMessage("Log table drop failed");
				return "LOGTABLEDROPFAIL";
			} catch (NullPointerException e) { // Null pointer exception occurs
												// when
				// the DB connection is not created
				ub.setMessage("Cannot establish connection to the database");
				// e.printStackTrace();
				return "LOGTABLEDROPFAIL";
			} catch (Exception e) {
				// e.printStackTrace();
				return "LOGTABLEDROPFAIL";
			}
		}
	}

	public String viewUsers() {
		String query = "SELECT user_id, user_name, first_name, last_name, email, creation_date, active FROM all_users ORDER BY user_id";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		DbBean db = (DbBean) session.getAttribute("dbBean");
		db.setDbmessage("");
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				db.setQueryType("select");
				db.executeSQL(query);
				db.generateResult();
				return "VIEWUSERSSUCESS";
			} catch (MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				db.setDbmessage("Tables does not exist");
				db.setRenderResult(false);
				return "VIEWUSERFAIL";
			} catch (NullPointerException e) {
				db.setDbmessage("Cannot establish connection to the database");
				db.setRenderResult(false);
				// e.printStackTrace();
				return "VIEWUSERFAIL";
			} catch (SQLException e) {
				db.setRenderResult(false);
				// e.printStackTrace();
				return "VIEWUSERFAIL";
			} catch (Exception e) {
				// e.printStackTrace();
				return "VIEWUSERFAIL";
			}
		}
	}

	public String viewUserLog() {
		String query = "SELECT * FROM user_log ORDER BY log_id DESC";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);

		DbBean db = (DbBean) session.getAttribute("dbBean");
		db.setDbmessage("");
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				db.setQueryType("select");
				db.executeSQL(query);
				db.generateResult();
				return "VIEWLOGSUCESS";
			} catch (MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				db.setDbmessage("Tables does not exist");
				db.setRenderResult(false);
				return "VIEWLOGFAIL";
			} catch (NullPointerException e) {
				db.setDbmessage("Cannot establish connection to the database");
				db.setRenderResult(false);
				// e.printStackTrace();
				return "VIEWLOGFAIL";
			} catch (SQLException e) {
				db.setRenderResult(false);
				// e.printStackTrace();
				return "VIEWLOGFAIL";
			} catch (Exception e) {
				// e.printStackTrace();
				return "VIEWLOGFAIL";
			}
		}
	}

	public String Logout() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UserBean ub = (UserBean) session.getAttribute("userBean");
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				ub.setMessage("");
				session.setAttribute("logout", "yes");
				// Insert log only for normal users
				if (!(ub.getUsername().equals("f14g26"))) {
					writeLog(ub.getUsername(), ub.getSessionid(),
							ub.getClientip(), "Logout");
				}
				/*
				 * Assigning null to instance variable of connection manager
				 * class, so that new connection is created when we logout and
				 * signin to different server
				 */
				FacesContext facesContextCm = FacesContext.getCurrentInstance();
				HttpSession sessionCm = (HttpSession) facesContextCm
						.getExternalContext().getSession(false);

				ConnectionManager cm = (ConnectionManager) sessionCm
						.getAttribute("connectionManager");
				FileActionBean fab = (FileActionBean) sessionCm
						.getAttribute("fileActionBean");
				// Cannot create object of ConnectionManager, as the constructor
				// is declared private
				cm.setInstance(null);
				conn = null;
				session.invalidate();
				return "LOGOUTSUCCESS";

			} catch (Exception e) {
				// e.printStackTrace();
				return "LOGOUTFAIL";
			}
		}
	}

	// Returns YES if the user is Admin User. Returns NO is the user is Normal
	// User
	public String isAdmin() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UserBean ub = (UserBean) session.getAttribute("userBean");
		// Reset all the messages
		resetMessages();

		try {
			if (session.getAttribute("logout") == null
					|| session.getAttribute("logout") == "yes") {
				return "GOHOME";
			} else {
				if (ub.getUsername().equals("f14g26")) {
					return "ISADMIN";
				} else {
					return "NOTADMIN";
				}
			}

		} catch (Exception e) {
			// e.printStackTrace();
			return "NOTADMIN";
		}
	}

	// Method to reset all the messages to null
	public static void resetMessages() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		UserBean ub = (UserBean) session.getAttribute("userBean");
		DbBean db = (DbBean) session.getAttribute("dbBean");
		MessageBean mb = (MessageBean) session.getAttribute("messageBean");
		FileActionBean fab = (FileActionBean) session
				.getAttribute("fileActionBean");
		PopulateList pl = (PopulateList) session.getAttribute("populateList");
		MathUtil mu = (MathUtil) session.getAttribute("mathUtil");
		DbTransactions dt = (DbTransactions) session
				.getAttribute("dbTransactions");
		// Reset message in MessageBean
		try {
			ub.setMessage("");
			ub.setLoginMessage("");
			ub.setRegisterMessage("");
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// Reset message in DbBean
		try {
			db.setDbmessage("");
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// Reset message in MessageBean
		try {
			mb.setFileImportMsg("");
			mb.setExamineTablesMsg("");
			mb.setMathCalcMsg("");
			mb.setDisplayPerformAnalysisMsg(true);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// Reset message in FileActionBean
		try {
			fab.setFileImport(false);
			fab.setFileImport(false);
			fab.setFileLabel("");
			fab.setTotalRows(0);
			fab.setNumberRows(0);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// Reset message in PopulateList
		try {
			pl.getTablesList().clear();
			pl.setSelectTable("");
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// Reset message in MathUtil
		try {
			mu.setDisplayScatterPlot(false);
			mu.setDisplayRegEq(false);
			mu.setxValue(null);
			mu.setyValue(null);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// Reset message in DbTransactions
		try {
			dt.setConnectionStatus("");
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	// Tests the connection to database
	public String testConnection() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			try {
				Connection conn = getConnection();
				if (conn == null) {
					connectionStatus = "FAIL";
					return "CONNECTIONFAILED";
				} else {
					connectionStatus = "SUCCESS";
					return "CONNECTIONESTABLISHED";
				}
			} catch (SQLException e) {
				// e.printStackTrace();
				connectionStatus = "FAIL";
				return "CONNECTIONFAILED";
			} catch (Exception e) {
				// e.printStackTrace();
				connectionStatus = "FAIL";
				return "CONNECTIONFAILED";
			}
		}
	}

	// Executes a query and returns a ResultSet, used to populate drop down
	// lists
	public ResultSet executeQuery(String query) {
		try {
			Statement statement = getConnection().createStatement();
			ResultSet rs = statement.executeQuery(query);
			return rs;

		} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
			// e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			// e.printStackTrace();
			return null;
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	public String displayTableData() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			DbBean db = (DbBean) session.getAttribute("dbBean");
			PopulateList pl = (PopulateList) session
					.getAttribute("populateList");
			MessageBean mb = (MessageBean) session.getAttribute("messageBean");
			MathUtil mu = (MathUtil) session.getAttribute("mathUtil");
			String selectQuery = "SELECT * FROM " + pl.getSelectTable();
			try {
				mb.setDisplayExamineTablesMsg(false);
				mu.setDisplayResults(false);
				mu.setDisplayCalcErrorMsg(false);
				pl.getSelectColumn().clear();
				mu.getAnalysisList().clear();
				mu.setDisplayScatterPlot(false);
				mu.setDisplayScatterPlotError(false);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			try {
				db.setQueryType("select");
				db.executeSQL(selectQuery);
				db.generateResult();
				pl.listofColumns();
				mb.setDisplayPerformAnalysisMsg(false);
				return "DISPLAYSUCCESS";
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				mb.setExamineTablesMsg("Please select a table from the list");
				db.setRenderResult(false);
				mu.setDisplayResults(false);
				mu.setDisplayCalcErrorMsg(false);
				mb.setDisplayExamineTablesMsg(true);
				return "DISPLAYFAIL";
			} catch (SQLException e) {
				// e.printStackTrace();
				db.setRenderResult(false);
				mu.setDisplayResults(false);
				mu.setDisplayCalcErrorMsg(false);
				mb.setDisplayExamineTablesMsg(false);
				return "DISPLAYFAIL";
			} catch (NullPointerException e) {
				// e.printStackTrace();
				db.setRenderResult(false);
				mu.setDisplayResults(false);
				mu.setDisplayCalcErrorMsg(false);
				return "DISPLAYFAIL";
			} catch (Exception e) {
				// e.printStackTrace();
				db.setRenderResult(false);
				mu.setDisplayResults(false);
				mu.setDisplayCalcErrorMsg(false);
				mb.setDisplayExamineTablesMsg(false);
				return "DISPLAYFAIL";
			}
		}
	}

	public String dropTable() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			DbBean db = (DbBean) session.getAttribute("dbBean");
			PopulateList pl = (PopulateList) session
					.getAttribute("populateList");
			UserBean ub = (UserBean) session.getAttribute("userBean");
			MessageBean mb = (MessageBean) session.getAttribute("messageBean");
			MathUtil mu = (MathUtil) session.getAttribute("mathUtil");
			String sqlStatement = "DROP TABLE " + pl.getSelectTable();
			try {
				pl.getSelectColumn().clear();
				pl.getTablesList().clear();
				db.setRenderResult(false);
				mb.setDisplayExamineTablesMsg(false);
				mu.setDisplayResults(false);
				mu.setDisplayCalcErrorMsg(false);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			try {
				db.setQueryType("drop");
				db.executeSQL(sqlStatement);
				pl.listofTables();
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				ub.setMessage("Unable to drop table");
				return "TABLEDROPFAILED";
			} catch (SQLException e) {
				// e.printStackTrace();
				ub.setMessage("Unable to drop table");
				return "TABLEDROPFAILED";
			} catch (NullPointerException e) {
				// e.printStackTrace();
				ub.setMessage("Unable to drop table");
				return "TABLEDROPFAILED";
			} catch (Exception e) {
				// e.printStackTrace();
				ub.setMessage("Unable to drop table");
				return "TABLEDROPFAILED";
			}
			ub.setMessage("Table: " + pl.getSelectTable()
					+ " successfully dropped");
			return "TABLEDROPPED";
		}
	}
}