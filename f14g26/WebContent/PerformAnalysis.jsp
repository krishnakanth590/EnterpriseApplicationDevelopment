<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="t" uri="http://myfaces.apache.org/tomahawk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%
	if (session.getAttribute("logout") == null
			|| session.getAttribute("logout") == "yes") {
%><meta http-equiv="Refresh" content="0;url=login.jsp" />
<%
	}
%>
<title>Perform Analysis</title>
</head>
<body>
	<center>
		<f:view>
			<div style="background-color: Gainsboro">
				<br>
				<h:outputText value="Data Analysis"
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
			<h:outputText
				value="To perform analysis, you must click on DisplayData first"
				style="color:red;font-weight:bold"
				rendered="#{messageBean.displayPerformAnalysisMsg }" />
			<h:panelGrid columns="2" rendered="#{dbBean.renderResult}"
				cellpadding="5" cellspacing="1" border="1">
				<h:outputText value="Table : " style="font-weight:bold" />
				<h:outputText value="#{populateList.selectTable}"
					style="font-weight:bold;color:DarkRed;font-size:20px" />
			</h:panelGrid>
			<br>
			<h:form>
				<!-- Display Columns List -->
				<h:panelGrid columns="5" rendered="#{dbBean.renderResult}"
					cellpadding="5" cellspacing="5">
					<h:outputText value="Select columns : " style="font-weight:bold" />
					<h:selectManyListbox value="#{populateList.selectColumn}">
						<f:selectItems value="#{populateList.columnsList}" />
					</h:selectManyListbox>
					<h:outputText value="Select analysis : " style="font-weight:bold" />
					<h:selectManyListbox value="#{mathUtil.analysisList}">
						<f:selectItem itemValue="minValue" itemLabel="Minimum" />
						<f:selectItem itemValue="q1" itemLabel="25% (Q1)" />
						<f:selectItem itemValue="median" itemLabel="50% (Median)" />
						<f:selectItem itemValue="q3" itemLabel="75% (Q3)" />
						<f:selectItem itemValue="maxValue" itemLabel="Maximum" />
						<f:selectItem itemValue="mean" itemLabel="Mean" />
						<f:selectItem itemValue="std" itemLabel="Standard Deviation" />
					</h:selectManyListbox>
					<h:commandButton type="submit" value="Calculate"
						action="#{mathUtil.calculateBasics}" style="font-weight:bold" />
				</h:panelGrid>
				<!-- Error message for calculation -->
				<br>
				<h:outputText rendered="#{mathUtil.displayCalcErrorMsg}"
					value="#{mathUtil.calcErrorMessage}"
					style="color:red;font-weight:bold" />
				<br>
				<!-- Display Analysis -->
				<t:dataTable value="#{mathUtil.results}" var="rowNumber"
					rendered="#{mathUtil.displayResults}" border="1" cellspacing="0"
					cellpadding="3" columnClasses="columnClass1 border"
					headerClass="headerClass" footerClass="footerClass"
					rowClasses="rowClass2" styleClass="dataTableEx"
					style="background-color: AliceBlue;
 					   border-bottom-style: solid;
					   border-top-style: solid;
 					   border-left-style: solid;
 					   border-right-style: solid;
 					   margin: 10px;text-align: center">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Variable Name" />
						</f:facet>
						<h:outputText value="#{rowNumber.variableName}" />
					</h:column>
					<h:column rendered="#{mathUtil.displayMinValue}">
						<f:facet name="header">
							<h:outputText value="Minimum Value" />
						</f:facet>
						<h:outputText value="#{rowNumber.minValue}" />
					</h:column>
					<h:column rendered="#{mathUtil.displayMaxValue}">
						<f:facet name="header">
							<h:outputText value="Maximum Value" />
						</f:facet>
						<h:outputText value="#{rowNumber.maxValue}" />
					</h:column>
					<h:column rendered="#{mathUtil.displayMean}">
						<f:facet name="header">
							<h:outputText value="Mean" />
						</f:facet>
						<h:outputText value="#{rowNumber.mean}" />
					</h:column>
					<h:column rendered="#{mathUtil.displayStd}">
						<f:facet name="header">
							<h:outputText value="Standard Deviation" />
						</f:facet>
						<h:outputText value="#{rowNumber.std}" />
					</h:column>
					<h:column rendered="#{mathUtil.displayMedian}">
						<f:facet name="header">
							<h:outputText value="Median" />
						</f:facet>
						<h:outputText value="#{rowNumber.median}" />
					</h:column>
					<h:column rendered="#{mathUtil.displayQ1}">
						<f:facet name="header">
							<h:outputText value="First Quartile (Q1)" />
						</f:facet>
						<h:outputText value="#{rowNumber.q1}" />
					</h:column>
					<h:column rendered="#{mathUtil.displayQ3}">
						<f:facet name="header">
							<h:outputText value="Third Quartile (Q3)" />
						</f:facet>
						<h:outputText value="#{rowNumber.q3}" />
					</h:column>
				</t:dataTable>
				<br>
				<!-- Display list of columns for scatter plot creation  -->
				<h:panelGrid columns="6" rendered="#{dbBean.renderResult}"
					cellpadding="5" cellspacing="5">
					<h:outputText value="Select column for X Axis : "
						style="font-weight:bold" />
					<h:selectOneListbox value="#{populateList.selectXColumn}" size="1">
						<f:selectItems value="#{populateList.columnsList}" />
					</h:selectOneListbox>
					<h:outputText value="Select column for Y Axis : "
						style="font-weight:bold" />
					<h:selectOneListbox value="#{populateList.selectYColumn}" size="1">
						<f:selectItems value="#{populateList.columnsList}" />
					</h:selectOneListbox>
					<h:commandButton type="submit" value="Draw Scatter Plot"
						action="#{mathUtil.drawChart}" style="font-weight:bold" />
					<h:commandButton type="submit" value="Draw Regression Line"
						action="#{mathUtil.drawRegLine}" style="font-weight:bold" />
				</h:panelGrid>
				<!--  Display scatter plot -->
				<h:graphicImage rendered="#{mathUtil.displayScatterPlot}"
					value="temp/scatterplot.jpeg" />
				<br>
				<h:outputText value="Regression equation : "
					rendered="#{mathUtil.displayRegEq}"
					style="font-size:24px;font-weight:bold" />
				<br>
				<h:panelGrid columns="9" rendered="#{mathUtil.displayRegEq}"
					cellpadding="4" cellspacing="4">
					<h:outputText value="#{populateList.selectYColumn}"
						style="font-size:20px;font-weight:bold;color:brown" />
					<h:outputText value="=" style="font-size:20px;font-weight:bold" />
					<h:outputText value="#{mathUtil.intercept}"
						style="font-size:20px;font-weight:bold;color:blue" />
					<h:outputText value="+" style="font-size:20px;font-weight:bold" />
					<h:outputText value="#{mathUtil.slope}"
						style="font-size:20px;font-weight:bold;color:blue" />
					<h:outputText value="(" style="font-size:20px;font-weight:bold" />
					<h:outputText value="#{populateList.selectXColumn}"
						style="font-size:20px;font-weight:bold;color:brown" />
					<h:outputText value=")" style="font-size:20px;font-weight:bold;" />
				</h:panelGrid>
				<h:panelGrid columns="5" rendered="#{mathUtil.displayRegEq}"
					cellpadding="3" cellspacing="3">
					<h:outputText value="Enter value of "
						style="font-size:20px;font-weight:bold" />
					<h:outputText value="#{populateList.selectXColumn}"
						style="font-size:20px;font-weight:bold;color:brown" />
					<h:outputText value=": " style="font-size:20px;font-weight:bold" />
					<h:inputText id="xValue" value="#{mathUtil.xValue}" />
					<h:message for="xValue" style="color:red;font-weight:bold" />
					<h:outputText value="" />
					<h:outputText value="#{populateList.selectYColumn}"
						style="font-size:20px;font-weight:bold;color:brown" />
					<h:outputText value=": " style="font-size:20px;font-weight:bold" />
					<h:outputText value="#{mathUtil.yValue}"
						style="font-size:20px;font-weight:bold" />
					<h:commandButton type="submit" value="Calculate"
						action="#{mathUtil.regress}" style="font-weight:bold" />
				</h:panelGrid>
				<br>
				<br>
				<!--  Display scatter plot error -->
				<h:outputText value="#{mathUtil.scatterPlotError}"
					rendered="#{mathUtil.displayScatterPlotError}"
					style="color:red;font-weight:bold" />
			</h:form>
		</f:view>
	</center>
</body>
</html>