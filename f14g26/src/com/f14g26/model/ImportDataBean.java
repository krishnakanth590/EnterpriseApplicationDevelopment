package com.f14g26.model;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.util.*;

@ManagedBean(name = "importDataBean")
@SessionScoped
public class ImportDataBean {
	private String variableName;
	private String dataType;
	private String variableType;
	private String dataValue;

	private ArrayList<String> dataTypeList = new ArrayList<String>(4);

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getDataValue() {
		return dataValue;
	}

	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public ArrayList<String> getDataTypeList() {
		return dataTypeList;
	}

	public void setDataTypeList(ArrayList<String> dataTypeList) {
		this.dataTypeList = dataTypeList;
	}

	public ImportDataBean() {
	}

	@PostConstruct
	public void init() {
		dataTypeList.add("string");
		dataTypeList.add("integer");
		dataTypeList.add("double");
		dataTypeList.add("date");
	}
}