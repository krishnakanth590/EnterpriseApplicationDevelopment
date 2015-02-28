package com.f14g26.model;

import java.sql.*;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import com.f14g26.db.ConnectionManager;
import com.f14g26.db.DbTransactions;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

@ManagedBean(name = "dbBean")
@SessionScoped
public class DbBean {

	private String server;
	private String dbmessage;
	private ResultSet rs;
	private String queryType;
	private String sqlQuery;
	private Result result;
	private ArrayList<String> columnNames;
	private int numberColumns;
	private ResultSetMetaData rsmd;
	private int numberRows;
	private boolean renderResult;

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public String getDbmessage() {
		return dbmessage;
	}

	public void setDbmessage(String dbmessage) {
		this.dbmessage = dbmessage;
	}

	public boolean isRenderResult() {
		return renderResult;
	}

	public void setRenderResult(boolean renderResult) {
		this.renderResult = renderResult;
	}

	public boolean executeSQL(String sqlQuery) throws SQLException,
			NullPointerException, MySQLSyntaxErrorException {
		this.sqlQuery = sqlQuery;
		if (!queryType.equalsIgnoreCase("select")) {
			PreparedStatement pstmt = DbTransactions.getConnection()
					.prepareStatement(sqlQuery);
			pstmt.executeUpdate();
			DbTransactions.getConnection().commit();
			return true;
		} else {
			PreparedStatement pstmt = DbTransactions.getConnection()
					.prepareStatement(sqlQuery);
			rs = pstmt.executeQuery();
			return true;
		}
	}

	// Process batch insert or update statements
	public boolean batchExecute(ArrayList<StringBuffer> sqlQueries)
			throws SQLException, NullPointerException,
			MySQLSyntaxErrorException {
		Statement statement = DbTransactions.getConnection().createStatement();
		for (int i = 0; i < sqlQueries.size(); i++) {

			statement.addBatch(sqlQueries.get(i).toString());
		}
		statement.executeBatch();
		statement.close();
		DbTransactions.getConnection().commit();
		ConnectionManager.getInstance().close();
		return true;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(ArrayList<String> columnNames) {
		this.columnNames = columnNames;
	}

	public int getNumberColumns() {
		return numberColumns;
	}

	public void setNumberColumns(int numberColumns) {
		this.numberColumns = numberColumns;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public boolean generateResult() throws SQLException {
		renderResult = true;
		result = ResultSupport.toResult(rs);
		rsmd = rs.getMetaData();
		numberColumns = rsmd.getColumnCount();
		numberRows = result.getRowCount();

		String columnNameList[] = result.getColumnNames();
		columnNames = new ArrayList<String>(numberColumns);
		for (int i = 0; i < numberColumns; i++) {
			columnNames.add(columnNameList[i]);
		}
		return true;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
}