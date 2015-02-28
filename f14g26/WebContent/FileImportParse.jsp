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
<title>File Import</title>
</head>
<body>
	<center>
		<f:view>
			<div style="background-color: Gainsboro">
				<br>
				<h:outputText value="Parse Data"
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
				<t:dataTable value="#{fileActionBean.idb}" var="rowNumber"
					rendered="#{fileActionBean.renderParseTable}" border="1"
					cellspacing="0" cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="800"
					style="background-color: AliceBlue;
 					   border-bottom-style: solid;
					   border-top-style: solid;
 					   border-left-style: solid;
 					   border-right-style: solid;
 					   margin: 10px;text-align: center">
					<h:column>
						<f:facet name="header">
							<h:outputText value="variableName" />
						</f:facet>
						<h:outputText value="#{rowNumber.variableName}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="SampleValue" />
						</f:facet>
						<h:outputText value="#{rowNumber.dataValue}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="DataType" />
						</f:facet>
						<h:outputText value="#{rowNumber.dataType}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="DataTypeList" />
						</f:facet>
						<h:selectOneMenu value="#{rowNumber.dataType}">
							<f:selectItem itemValue="STRING" itemLabel="STRING" />
							<f:selectItem itemValue="INTEGER" itemLabel="INTEGER" />
							<f:selectItem itemValue="DOUBLE" itemLabel="DOUBLE" />
							<f:selectItem itemValue="DATE" itemLabel="DATE" />
						</h:selectOneMenu>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="VariableType" />
						</f:facet>
						<h:outputText value="#{rowNumber.variableType}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="VariableTypeList" />
						</f:facet>
						<h:selectOneMenu value="#{rowNumber.variableType}">
							<f:selectItem itemValue="QUANTITATIVE" />
							<f:selectItem itemValue="CATEGORICAL" />
							<f:selectItem itemValue="COUNT" />
						</h:selectOneMenu>
					</h:column>
				</t:dataTable>
				<h:commandButton type="submit" value="Import"
					action="#{fileActionBean.processDataImport}"
					style="font-weight:bold;font-size:100%" />
			</h:form>
		</f:view>
	</center>
</body>
</html>