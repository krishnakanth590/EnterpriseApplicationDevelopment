package com.f14g26.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "messageBean")
@SessionScoped
public class MessageBean {
	private String fileImportMsg;
	private String mathCalcMsg;
	private String examineTablesMsg;
	private boolean displayExamineTablesMsg = false;
	private boolean displayPerformAnalysisMsg = true;

	public boolean isDisplayPerformAnalysisMsg() {
		return displayPerformAnalysisMsg;
	}

	public void setDisplayPerformAnalysisMsg(boolean displayPerformAnalysisMsg) {
		this.displayPerformAnalysisMsg = displayPerformAnalysisMsg;
	}

	public boolean isDisplayExamineTablesMsg() {
		return displayExamineTablesMsg;
	}

	public void setDisplayExamineTablesMsg(boolean displayExamineTablesMsg) {
		this.displayExamineTablesMsg = displayExamineTablesMsg;
	}

	public String getExamineTablesMsg() {
		return examineTablesMsg;
	}

	public void setExamineTablesMsg(String examineTablesMsg) {
		this.examineTablesMsg = examineTablesMsg;
	}

	public String getMathCalcMsg() {
		return mathCalcMsg;
	}

	public void setMathCalcMsg(String mathCalcMsg) {
		this.mathCalcMsg = mathCalcMsg;
	}

	public String getFileImportMsg() {
		return fileImportMsg;
	}

	public void setFileImportMsg(String fileImportMsg) {
		this.fileImportMsg = fileImportMsg;
	}

}