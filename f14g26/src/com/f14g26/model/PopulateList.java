package com.f14g26.model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.sql.*;

import com.f14g26.db.DbTransactions;

import java.util.*;

@ManagedBean(name = "populateList")
@SessionScoped
public class PopulateList implements Serializable {
	private ArrayList<String> tablesList;
	private ArrayList<String> columnsList;
	private String selectTable = "";
	private ArrayList<String> selectColumn;
	private String selectXColumn;
	private String selectYColumn;
	private ArrayList<String> columnsDataType;

	public String getSelectXColumn() {
		return selectXColumn;
	}

	public void setSelectXColumn(String selectXColumn) {
		this.selectXColumn = selectXColumn;
	}

	public String getSelectYColumn() {
		return selectYColumn;
	}

	public void setSelectYColumn(String selectYColumn) {
		this.selectYColumn = selectYColumn;
	}

	public ArrayList<String> getColumnsDataType() {
		return columnsDataType;
	}

	public void setColumnsDataType(ArrayList<String> columnsDataType) {
		this.columnsDataType = columnsDataType;
	}

	public ArrayList<String> getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(ArrayList<String> columnsList) {
		this.columnsList = columnsList;
	}

	public ArrayList<String> getSelectColumn() {
		return selectColumn;
	}

	public void setSelectColumn(ArrayList<String> selectColumn) {
		this.selectColumn = selectColumn;
	}

	public String getSelectTable() {
		return selectTable;
	}

	public void setSelectTable(String selectTable) {
		this.selectTable = selectTable;
	}

	public String listofTables() {
		String sqlQuery = "SHOW TABLES FROM f14g26 WHERE tables_in_f14g26 NOT IN (\"all_users\",\"user_log\")";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			DbTransactions dt = (DbTransactions) session
					.getAttribute("dbTransactions");
			// To hide old table data when we come back and click Examine Tables
			// button
			DbBean db = (DbBean) session.getAttribute("dbBean");
			MessageBean mb = (MessageBean) session.getAttribute("messageBean");
			MathUtil mu = (MathUtil) session.getAttribute("mathUtil");
			ResultSet rs = dt.executeQuery(sqlQuery);
			tablesList = new ArrayList<String>();
			try {
				db.setRenderResult(false);
				mb.setDisplayExamineTablesMsg(false);
				mu.setDisplayResults(false);
				mu.setDisplayCalcErrorMsg(false);
				selectColumn.clear();
				tablesList.clear();
				mu.getAnalysisList().clear();
			} catch (Exception e) {
				// e.printStackTrace();
			}
			try {
				while (rs.next()) {
					tablesList.add(rs.getString(1));
				}
			} catch (SQLException e) {
				// e.printStackTrace();
			} catch (Exception e) {
				// e.printStackTrace();
			}
			return "EXAMINETABLES";
		}
	}

	public String listofColumns() {
		String sqlQuery = "SHOW COLUMNS FROM " + selectTable
				+ " WHERE field != \"S_No\"";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		DbTransactions dt = (DbTransactions) session
				.getAttribute("dbTransactions");
		ResultSet rs = dt.executeQuery(sqlQuery);
		columnsList = new ArrayList<String>();
		columnsDataType = new ArrayList<String>();
		try {
			while (rs.next()) {
				columnsList.add(rs.getString(1));
				columnsDataType.add(rs.getString(2));
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return "EXAMINETABLES";
	}

	public ArrayList<String> getTablesList() {
		return tablesList;
	}

	public void setTablesList(ArrayList<String> tablesList) {
		this.tablesList = tablesList;
	}
}