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
<title>View Registered Users</title>
</head>
<body>
	<f:view>
		<center>
			<div style="background-color: Gainsboro">
				<br>
				<h:outputText value="View Users"
					style="font-weight:bold;color:brown;font-size:27px" />
				<br>
				<hr>
			</div>
			<h:form>
				<div style="background-color: OldLace">
					<h:panelGrid columns="3">
						<h:commandButton type="submit" value="Home"
							action="#{dbTransactions.isAdmin}" style="font-weight:bold" />
						<h:commandButton type="submit" value="Admin Tools"
							action="AdminTools.jsp" style="font-weight:bold" />
						<h:commandButton type="submit" value="Logout"
							action="#{dbTransactions.Logout}"
							onclick="return confirm('Are you sure you want to logout?')"
							style="font-weight:bold" />
					</h:panelGrid>
				</div>
			</h:form>
			<hr>
			<h:outputText value="#{dbBean.dbmessage}"
				style="font-weight:bold;color:Red" />
			<hr>
			<!-- 
			<h:panelGrid columns="2" rendered="#{dbBean.renderResult}">
				<h:outputText value="SQL Query : " />
				<h:outputText value="#{dbBean.sqlQuery}" />
				<h:outputText value="Column Names : " />
				<h:outputText value="#{dbBean.columnNames}" />
				<h:outputText value="No of columns : " />
				<h:outputText value="#{dbBean.numberColumns}" />
				<h:outputText value="No of rows: " />
				<h:outputText value="#{dbBean.numberRows}" />
			</h:panelGrid> 
			<hr>  -->
			<div
				style="background-attachment: scroll; overflow: auto; height: 400px; background-repeat: repeat">
				<t:dataTable value="#{dbBean.result}" var="row"
					rendered="#{dbBean.renderResult}" border="1" cellspacing="0"
					cellpadding="1" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx" width="900"
					style="text-align: center;background-color: Linen">
					<t:columns var="col" value="#{dbBean.columnNames}"
						style="background-color: PapayaWhip">
						<f:facet name="header">
							<t:outputText styleClass="outputHeader" value="#{col}" />
						</f:facet>
						<t:outputText styleClass="outputText" value="#{row[col]}">
						</t:outputText>
					</t:columns>
				</t:dataTable>
			</div>
		</center>
	</f:view>
</body>
</html>