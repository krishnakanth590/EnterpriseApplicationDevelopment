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
				<h:outputText value="Import Data"
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
			<h:form enctype="multipart/form-data">
				<h:panelGrid columns="2"
					style="background-color: Lavender ;
 					   border-bottom-style: solid;
					   border-top-style: solid;
 					   border-left-style: solid;
 					   border-right-style: solid;;
 					   margin: 20px"
					cellpadding="5" cellspacing="5">
					<h:outputLabel value="Select a file to upload : "
						style="font-weight:bold" />
					<t:inputFileUpload id="fileUpload" label="File to upload"
						storage="default" value="#{fileActionBean.uploadedFile}" size="60" />
					<h:outputLabel value="Name of the table to create :"
						style="font-weight:bold" />
					<h:inputText id="fileLabel" value="#{fileActionBean.fileLabel}"
						size="60" />
					<h:outputLabel value=" " />
					<h:commandButton id="upload"
						action="#{fileActionBean.processFileUpload}" value="Submit"
						style="font-weight:bold" />
				</h:panelGrid>
				<hr>
				<h:outputLabel rendered="#{fileActionBean.fileImport }"
					value="Table successfully created. Number of records imported: "
					style="font-weight:bold;color:Green" />
				<h:outputText rendered="#{fileActionBean.fileImport }"
					value="#{fileActionBean.numberRows }"
					style="font-weight:bold;color:Green" />
				<h:outputText rendered="#{fileActionBean.fileImportError }"
					value="#{messageBean.fileImportMsg }"
					style="font-weight:bold;color:Red" />
			</h:form>
		</f:view>
	</center>
</body>
</html>