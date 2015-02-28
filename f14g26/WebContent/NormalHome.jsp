<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
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
<title>Welcome !</title>
</head>
<f:view>
	<center>
		<div style="background-color: Gainsboro">
			<br>
			<h:outputText value="Welcome "
				style="font-weight:bold;color:brown;font-size:27px" />
			<h:outputText value="#{userBean.firstname}"
				style="font-weight:bold;color:brown;font-size:27px" />
			<h:outputText value=" !"
				style="font-weight:bold;color:brown;font-size:27px" />
			<br>
			<hr>
		</div>
		<h:form>
		<div style="background-color: OldLace">
			<h:panelGrid columns="10">
				<h:commandButton type="submit" value="Home"
					action="#{dbTransactions.isAdmin}" style="font-weight:bold" />
				<h:commandButton type="submit" value="Examine Tables"
					action="#{populateList.listofTables}" style="font-weight:bold" />
				<h:commandButton type="submit" value="Import Data"
					action="FileImport.jsp" style="font-weight:bold" />
				<h:commandButton type="submit" value="Session Info"
					action="SessionInfo.jsp" style="font-weight:bold" />
				<h:commandButton type="submit" value="Logout"
					action="#{dbTransactions.Logout}"
					onclick="return confirm('Are you sure you want to logout?')"
					style="font-weight:bold" />
			</h:panelGrid>
			</div>
		</h:form>
		<hr>
	</center>
</f:view>
</body>
</html>