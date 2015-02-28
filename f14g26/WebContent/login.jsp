<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>f14g26 - Login</title>
</head>
<body>
	<f:view>
		<center>
			<h:form>
				<h:outputText value="f14g26 "
					style="color:Maroon;font-weight:bold;font-size:40px;text-shadow: 5px 5px 5px #DAA520" />

				<hr>
				<h:panelGrid columns="3">
					<h:outputText value="Username : " style="font-weight:bold;color:Maroon" />
					<h:inputText id="userName" value="#{userBean.username}"
						required="true" requiredMessage="User name is required"
						label="User Name" maxlength="30" />
					<h:message for="userName" style="color:red;font-weight:bold" />
					<h:outputText value="Password : " style="font-weight:bold;color:Maroon" />
					<h:inputSecret id="password" value="#{userBean.password}"
						required="true" requiredMessage="Password is required"
						label="Password" maxlength="50" />
					<h:message for="password" style="color:red;font-weight:bold" />
					<h:outputText value="Server : " style="font-weight:bold;color:Maroon" />
					<h:selectOneListbox value="#{dbBean.server}" size="1">
						<f:selectItem itemValue="131.193.209.54:3306"
							itemLabel="Server 54" />
						<f:selectItem itemValue="131.193.209.57:3306"
							itemLabel="Server 57" />
						<f:selectItem itemValue="localhost" itemLabel="localhost" />
					</h:selectOneListbox>
					<h:commandButton type="submit" value="Login"
						action="#{dbTransactions.checkUser}" style="font-weight:bold" />
				</h:panelGrid>
				<br>
				<!-- Displaying the return message -->
				<h:outputText rendered="#{userBean.displayLoginMessage }"
					value="#{userBean.loginMessage}" style="color:red;font-weight:bold" />
				<h:outputText rendered="#{userBean.displayRegisterMessage }"
					value="#{userBean.registerMessage}"
					style="color:Green;font-weight:bold" />
				<br>
				<h:outputText value="New user ? Click " style="font-size:18px;color:Maroon" />
				<a href="register.jsp" style="font-weight: bold; font-size: 19px"><b>here</b></a>
				<h:outputText value="to register" style="font-size:18px;color:Maroon" />
				<br>

			</h:form>
		</center>
		<%-- Documentation Links --%>
		<table align="center">
			<tr>
				<td><font size=3 color="Maroon"><b><a
							href="https://drive.google.com/file/d/0B6V_lo_W6LWDUnFWLVBUelIyaDA/view?usp=sharing"
							target="_blank"><font color="Purple">User's Guide</font></a></b></font></td>
				<td><font size=3 color="Maroon"><b><a
							href="https://drive.google.com/file/d/0B6V_lo_W6LWDcFZtZC1CNFk1WDA/view?usp=sharing"
							target="_blank"><font color="Purple">Developer's Guide</font></a></b></font></td>
			</tr>
		</table>
	</f:view>
</body>
</html>