<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
	<f:view>
		<center>
			<h:form>
				<h1 align="center" style = "text-shadow: 7px 7px 7px #EEE8AA">Register</h1>
				<hr>
				<h:commandButton type="submit" value="Home"
					action="#{dbTransactions.isAdmin}" />
				<hr>
			</h:form>
			<h:form>
				<h:panelGrid columns="3">
					<h:outputText value="Username* : "
						style="font-weight:bold;color:Maroon" />
					<h:inputText id="userName" value="#{userBean.username}"
						required="true" requiredMessage="User name is required"
						label="User Name" maxlength="30" />
					<h:message for="userName" style="color:red;font-weight:bold" />
					<h:outputText value="Password* : "
						style="font-weight:bold;color:Maroon" />
					<h:inputSecret id="password" value="#{userBean.password}"
						required="true" requiredMessage="Password is required"
						label="Password" maxlength="50" />
					<h:message for="password" style="color:red;font-weight:bold" />
					<h:outputText value="FirstName* : "
						style="font-weight:bold;color:Maroon" />
					<h:inputText id="firstName" value="#{userBean.firstname}"
						required="true" requiredMessage="Firstname is requried"
						label="First Name" maxlength="50" />
					<h:message for="firstName" style="color:red;font-weight:bold" />
					<h:outputText value="LastName : "
						style="font-weight:bold;color:Maroon" />
					<h:inputText id="lastName" value="#{userBean.lastname}"
						label="Last Name" maxlength="50" />
					<h:message for="lastName" style="color:red;font-weight:bold" />
					<h:outputText value="Email* : "
						style="font-weight:bold;color:Maroon" />
					<h:inputText id="email" value="#{userBean.email}" required="true"
						label="Email" requiredMessage="Email is required" maxlength="50" />
					<h:message for="email" style="color:red;font-weight:bold" />
					<h:outputText value="Server : "
						style="font-weight:bold;color:Maroon" />
					<h:selectOneListbox value="#{dbBean.server}" size="1">
						<f:selectItem itemValue="131.193.209.54:3306"
							itemLabel="Server 54" />
						<f:selectItem itemValue="131.193.209.57:3306"
							itemLabel="Server 57" />
						<f:selectItem itemValue="localhost" itemLabel="localhost" />
					</h:selectOneListbox>
				</h:panelGrid>
				<br>
				<h:commandButton type="submit" value="Register"
					action="#{dbTransactions.insertUser}" style="font-weight:bold" />
			</h:form>
			<br>
			<h:outputText value="Fields marked * are mandatory" style="font-weight:bold"/>
			<hr>
			<!-- Displaying the return message -->
			<h:outputText value="#{userBean.registerMessage}"
				style="font-weight:bold;color:Red" />
		</center>
	</f:view>
</body>
</html>