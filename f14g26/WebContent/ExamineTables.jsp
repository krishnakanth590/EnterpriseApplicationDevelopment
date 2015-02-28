<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!--  Logic to redirect to login page if session is invalid -->
<%
	if (session.getAttribute("logout") == null
			|| session.getAttribute("logout") == "yes") {
%><meta http-equiv="Refresh" content="0;url=login.jsp" />
<%
	}
%>
<title>Examine Tables</title>
</head>
<body>
	<center>
		<f:view>
			<div style="background-color: Gainsboro">
				<br>
				<h:outputText value="Examine Tables"
					style="font-weight:bold;color:brown;font-size:27px" />
				<br>
				<hr>
			</div>
			<h:form>
				<div style="background-color: OldLace">
					<h:panelGrid columns="10">
						<h:commandButton type="submit" value="Home"
							action="#{dbTransactions.isAdmin}" style="font-weight:bold" />
						<h:commandButton type="submit" value="Logout"
							action="#{dbTransactions.Logout}"
							onclick="return confirm('Are you sure you want to logout?')"
							style="font-weight:bold" />
					</h:panelGrid>
				</div>
			</h:form>
			<hr>
			<h:form>
				<div style="background-color: GhostWhite">
					<h:panelGrid columns="8" cellpadding="5" cellspacing="5">
						<h:outputText value="Select a table : " style="font-weight:bold" />
						<h:selectOneMenu value="#{populateList.selectTable}">
							<f:selectItems value="#{populateList.tablesList}" />
						</h:selectOneMenu>
						<h:commandButton type="submit" value="Display Data"
							action="#{dbTransactions.displayTableData}"
							style="font-weight:bold" />
						<h:commandButton type="submit" value="Drop Table"
							action="#{dbTransactions.dropTable}" style="font-weight:bold"
							onclick="return confirm('Are you sure you want to drop the table?')" />
						<h:selectOneRadio value="#{fileActionBean.outputFormat}"
							style="font-weight:bold">
							<f:selectItem itemValue="xml" itemLabel="XML" />
							<f:selectItem itemValue="csv" itemLabel="CSV" />
						</h:selectOneRadio>
						<h:commandButton type="submit" value="Export Data"
							action="#{fileActionBean.processFileDownload}"
							style="font-weight:bold" />
						<h:commandButton type="submit" value="Perform Analysis"
							action="PerformAnalysis.jsp" style="font-weight:bold" />
					</h:panelGrid>
				</div>
				<hr>
				<h:outputText rendered="#{messageBean.displayExamineTablesMsg}"
					value="#{messageBean.examineTablesMsg}"
					style="color:red;font-weight:bold" />
				<br>
				<!-- Display Table Data -->
				<div
					style="background-attachment: scroll; overflow: auto; height: 300px; background-repeat: repeat">
					<t:dataTable value="#{dbBean.result}" var="row"
						rendered="#{dbBean.renderResult}" border="1" cellspacing="0"
						cellpadding="3" columnClasses="columnClass1 border"
						headerClass="headerClass" footerClass="footerClass"
						rowClasses="rowClass2" styleClass="dataTableEx"
						style="background-color: Azure">
						<t:columns var="col" value="#{dbBean.columnNames}"
							style="text-align: center">
							<f:facet name="header">
								<t:outputText styleClass="outputHeader" value="#{col}" />
							</f:facet>
							<t:outputText styleClass="outputText" value="#{row[col]}">
							</t:outputText>
						</t:columns>
					</t:dataTable>
				</div>
			</h:form>
		</f:view>
	</center>
</body>
</html>