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
<title>Session Information</title>
</head>
<body>
	<f:view>
		<center>
			<div style="background-color: Gainsboro">
				<br>
				<h:outputText value="Session Information"
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
			<h:panelGrid columns="2" cellspacing="0" cellpadding="5" border="1">
				<h:outputText value="Signed in as : " style="font-weight:bold" />
				<h:outputText value="#{userBean.username}" />
				<h:outputText value="Connected to Database : "
					style="font-weight:bold" />
				<h:outputText value="#{dbBean.server}" />
			</h:panelGrid>
			<br>
			<h:form>
				<h:commandButton type="submit" value="TestConnection"
					action="#{dbTransactions.testConnection}" style="font-weight:bold" />
				<h:outputText value="  :  " />
				<h:outputText value="#{dbTransactions.connectionStatus}"
					style="font-weight:bold" />
			</h:form>
		</center>
	</f:view>
</body>
</html>