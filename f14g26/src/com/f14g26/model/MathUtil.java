package com.f14g26.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.math3.stat.StatUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

@ManagedBean(name = "mathUtil")
@SessionScoped
public class MathUtil {

	private ArrayList<ResultsBean> results;
	private boolean displayResults = false;
	private String calcErrorMessage;
	private boolean displayCalcErrorMsg = false;
	private ArrayList<String> analysisList;
	private boolean displayMinValue = false;
	private boolean displayMaxValue = false;
	private boolean displayMean = false;
	private boolean displayStd = false;
	private boolean displayQ1 = false;
	private boolean displayMedian = false;
	private boolean displayQ3 = false;
	private String filePath = null;
	private boolean displayScatterPlot = false;
	private boolean displayRegEq = false;
	private String scatterPlotError = "";
	private boolean displayScatterPlotError = false;
	private String regEq = null;
	private String intercept;
	private String slope;
	private Double xValue = null;
	private String yValue = null;

	public Double getxValue() {
		return xValue;
	}

	public void setxValue(Double xValue) {
		this.xValue = xValue;
	}

	public String getyValue() {
		return yValue;
	}

	public void setyValue(String yValue) {
		this.yValue = yValue;
	}

	public String getIntercept() {
		return intercept;
	}

	public void setIntercept(String intercept) {
		this.intercept = intercept;
	}

	public String getSlope() {
		return slope;
	}

	public void setSlope(String slope) {
		this.slope = slope;
	}

	public String getRegEq() {
		return regEq;
	}

	public void setRegEq(String regEq) {
		this.regEq = regEq;
	}

	public boolean isDisplayRegEq() {
		return displayRegEq;
	}

	public void setDisplayRegEq(boolean displayRegEq) {
		this.displayRegEq = displayRegEq;
	}

	public boolean isDisplayScatterPlotError() {
		return displayScatterPlotError;
	}

	public void setDisplayScatterPlotError(boolean displayScatterPlotError) {
		this.displayScatterPlotError = displayScatterPlotError;
	}

	public String getScatterPlotError() {
		return scatterPlotError;
	}

	public void setScatterPlotError(String scatterPlotError) {
		this.scatterPlotError = scatterPlotError;
	}

	public boolean isDisplayScatterPlot() {
		return displayScatterPlot;
	}

	public void setDisplayScatterPlot(boolean displayScatterPlot) {
		this.displayScatterPlot = displayScatterPlot;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isDisplayMinValue() {
		return displayMinValue;
	}

	public void setDisplayMinValue(boolean displayMinValue) {
		this.displayMinValue = displayMinValue;
	}

	public boolean isDisplayMaxValue() {
		return displayMaxValue;
	}

	public void setDisplayMaxValue(boolean displayMaxValue) {
		this.displayMaxValue = displayMaxValue;
	}

	public boolean isDisplayMean() {
		return displayMean;
	}

	public void setDisplayMean(boolean displayMean) {
		this.displayMean = displayMean;
	}

	public boolean isDisplayStd() {
		return displayStd;
	}

	public void setDisplayStd(boolean displayStd) {
		this.displayStd = displayStd;
	}

	public boolean isDisplayQ1() {
		return displayQ1;
	}

	public void setDisplayQ1(boolean displayQ1) {
		this.displayQ1 = displayQ1;
	}

	public boolean isDisplayMedian() {
		return displayMedian;
	}

	public void setDisplayMedian(boolean displayMedian) {
		this.displayMedian = displayMedian;
	}

	public boolean isDisplayQ3() {
		return displayQ3;
	}

	public void setDisplayQ3(boolean displayQ3) {
		this.displayQ3 = displayQ3;
	}

	public ArrayList<String> getAnalysisList() {
		return analysisList;
	}

	public void setAnalysisList(ArrayList<String> analysisList) {
		this.analysisList = analysisList;
	}

	public boolean isDisplayCalcErrorMsg() {
		return displayCalcErrorMsg;
	}

	public void setDisplayCalcErrorMsg(boolean displayCalcErrorMsg) {
		this.displayCalcErrorMsg = displayCalcErrorMsg;
	}

	public String getCalcErrorMessage() {
		return calcErrorMessage;
	}

	public void setCalcErrorMessage(String calcErrorMessage) {
		this.calcErrorMessage = calcErrorMessage;
	}

	public String calculateBasics() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			PopulateList pl = (PopulateList) session
					.getAttribute("populateList");
			DbBean db = (DbBean) session.getAttribute("dbBean");
			results = new ArrayList<ResultsBean>();
			displayMinValue = false;
			displayQ1 = false;
			displayMedian = false;
			displayQ3 = false;
			displayMaxValue = false;
			displayMean = false;
			displayStd = false;

			// Get the list of analysis and change the display property for
			// those
			// anaylsis to true
			for (int i = 0; i < analysisList.size(); i++) {
				if (analysisList.get(i).equalsIgnoreCase("minValue"))
					displayMinValue = true;
				if (analysisList.get(i).equalsIgnoreCase("q1"))
					displayQ1 = true;
				if (analysisList.get(i).equalsIgnoreCase("median"))
					displayMedian = true;
				if (analysisList.get(i).equalsIgnoreCase("q3"))
					displayQ3 = true;
				if (analysisList.get(i).equalsIgnoreCase("maxValue"))
					displayMaxValue = true;
				if (analysisList.get(i).equalsIgnoreCase("mean"))
					displayMean = true;
				if (analysisList.get(i).equalsIgnoreCase("std"))
					displayStd = true;
			}

			for (int i = 0; i < pl.getSelectColumn().size(); i++) {
				// Get the number of rows in result set
				int numberOfRows = 0;
				results.add(new ResultsBean());
				try {
					db.getRs().beforeFirst();
					while (db.getRs().next()) {
						numberOfRows++;
					}
				} catch (SQLException e1) {
					// e1.printStackTrace();
					displayCalcErrorMsg = true;
					displayResults = false;
					return "CALCFAILED";
				} catch (Exception e1) {
					// e1.printStackTrace();
					displayCalcErrorMsg = true;
					displayResults = false;
					return "CALCFAILED";
				}
				double[] listOfColumnValues = new double[numberOfRows];

				try {
					int j = 0;
					db.getRs().beforeFirst();
					while (db.getRs().next()) {
						// Get the column name --> Get the value in that column
						// --> Assign it to the array
						listOfColumnValues[j] = db.getRs().getDouble(
								pl.getSelectColumn().get(i));
						j++;
					}
					double minValue = StatUtils.min(listOfColumnValues);
					double maxValue = StatUtils.max(listOfColumnValues);
					double mean = StatUtils.mean(listOfColumnValues);
					double variance = StatUtils.variance(listOfColumnValues,
							mean);
					double std = Math.sqrt(variance);
					double median = StatUtils.percentile(listOfColumnValues,
							50.0);
					double q1 = StatUtils.percentile(listOfColumnValues, 25.0);
					double q3 = StatUtils.percentile(listOfColumnValues, 75.0);
					// Set the variable name first
					results.get(i).setVariableName(pl.getSelectColumn().get(i));
					results.get(i).setMinValue(minValue);
					results.get(i).setMaxValue(maxValue);
					results.get(i).setMean(mean);
					results.get(i).setVariance(variance);
					results.get(i).setStd(std);
					results.get(i).setMedian(median);
					results.get(i).setQ1(q1);
					results.get(i).setQ3(q3);
					displayResults = true;
					displayCalcErrorMsg = false;
				} catch (SQLException e) {
					calcErrorMessage = "Error calculating statistics. Select only numerical columns";
					// e.printStackTrace();
					displayCalcErrorMsg = true;
					displayResults = false;
					return "CALCFAILED";
				} catch (Exception e) {
					calcErrorMessage = "Error calculating statistics";
					// e.printStackTrace();
					displayCalcErrorMsg = true;
					displayResults = false;
					return "CALCFAILED";
				}

			}
			return "CALCSUCCESS";
		}
	}

	public boolean isDisplayResults() {
		return displayResults;
	}

	public void setDisplayResults(boolean displayResults) {
		this.displayResults = displayResults;
	}

	public ArrayList<ResultsBean> getResults() {
		return results;
	}

	public void setResults(ArrayList<ResultsBean> results) {
		this.results = results;
	}

	public String drawChart() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			PopulateList pl = (PopulateList) session
					.getAttribute("populateList");
			DbBean db = (DbBean) session.getAttribute("dbBean");
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series = new XYSeries("scatterplot");
			FacesContext context = FacesContext.getCurrentInstance();
			String path = context.getExternalContext().getRealPath("/temp");
			// Get column name --> Get value in that column
			String xaxis = pl.getSelectXColumn();
			String yaxis = pl.getSelectYColumn();
			try {
				db.getRs().beforeFirst();
				displayRegEq = false;
				yValue = null;
				xValue = null;
				while (db.getRs().next()) {
					// Add the values to the datapoints
					series.add(db.getRs().getDouble(pl.getSelectXColumn()), db
							.getRs().getDouble(pl.getSelectYColumn()));
				}
				dataset.addSeries(series);

				JFreeChart chart = ChartFactory.createScatterPlot(
						"Scatter plot", xaxis, yaxis, dataset);
				chart.clearSubtitles();
				// XYPlot plot = chart.getXYPlot();
				ChartFrame frame = new ChartFrame("Chart Frame", chart);
				frame.setVisible(false);
				frame.setSize(600, 450);
				filePath = path + "/scatterplot.jpeg";
				ChartUtilities.saveChartAsJPEG(new File(filePath), chart, 600,
						450);
				displayScatterPlot = true;
				displayScatterPlotError = false;
				return "CHARTSUCCESS";

			} catch (SQLException e) {
				// e.printStackTrace();
				displayScatterPlot = false;
				displayScatterPlotError = true;
				scatterPlotError = "Cannot create Scatter plot. Select columns with numerical data";
				return "CHARTFAILED";
			} catch (Exception e) {
				// e.printStackTrace();
				displayScatterPlot = false;
				displayScatterPlotError = true;
				scatterPlotError = "Unable to create Scatter Plot";
				return "CHARTFAILED";
			}
		}
	}

	public String drawRegLine() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			PopulateList pl = (PopulateList) session
					.getAttribute("populateList");
			DbBean db = (DbBean) session.getAttribute("dbBean");
			XYSeriesCollection dataset = new XYSeriesCollection();
			XYSeries series = new XYSeries("scatterplot");
			FacesContext context = FacesContext.getCurrentInstance();
			String path = context.getExternalContext().getRealPath("/temp");
			// Get column name --> Get value in that column
			String xaxis = pl.getSelectXColumn();
			String yaxis = pl.getSelectYColumn();
			DecimalFormat f = new DecimalFormat("#.###");
			double[] xvalues;
			double[] yvalues;
			try {
				displayRegEq = false;
				yValue = null;
				xValue = null;
				// Calculating number of rows
				int numberOfRows = 0;
				db.getRs().beforeFirst();
				while (db.getRs().next()) {
					numberOfRows++;
				}
				xvalues = new double[numberOfRows];
				yvalues = new double[numberOfRows];
				int i = 0;
				db.getRs().beforeFirst();

				while (db.getRs().next()) {
					// Add the values to the datapoints
					series.add(db.getRs().getDouble(pl.getSelectXColumn()), db
							.getRs().getDouble(pl.getSelectYColumn()));
					xvalues[i] = db.getRs().getDouble(pl.getSelectXColumn());
					yvalues[i] = db.getRs().getDouble(pl.getSelectYColumn());
					i++;
				}
				dataset.addSeries(series);
				double minReg = StatUtils.min(xvalues);
				double maxReg = StatUtils.max(xvalues);
				JFreeChart chart = ChartFactory.createScatterPlot(
						"Scatter plot", xaxis, yaxis, dataset);
				chart.clearSubtitles();
				// Code for regression
				// Getting the regression parameters
				double regressionParameters[] = Regression.getOLSRegression(
						dataset, 0);
				// Preparing line function using regression parameters
				LineFunction2D lineFunction = new LineFunction2D(
						regressionParameters[0], regressionParameters[1]);
				XYDataset datasetReg = DatasetUtilities.sampleFunction2D(
						lineFunction, minReg, maxReg, 2, "Regression Line");
				// Drawing the line dataset
				XYPlot plot = chart.getXYPlot();
				plot.setDataset(1, datasetReg);
				XYLineAndShapeRenderer xyline = new XYLineAndShapeRenderer(
						true, true);
				xyline.setSeriesPaint(0, Color.BLUE);
				xyline.setSeriesStroke(0, new BasicStroke(4.0f));
				plot.setRenderer(1, xyline);
				// Regression code end
				ChartFrame frame = new ChartFrame("Chart Frame", chart);
				frame.setVisible(false);
				frame.setSize(600, 450);
				filePath = path + "/scatterplot.jpeg";
				ChartUtilities.saveChartAsJPEG(new File(filePath), chart, 600,
						450);
				displayScatterPlot = true;
				displayScatterPlotError = false;
				displayRegEq = true;
				regEq = pl.getSelectYColumn() + " = " + regressionParameters[0]
						+ " + " + regressionParameters[1] + " ( "
						+ pl.getSelectXColumn() + " ) ";
				intercept = f.format(regressionParameters[0]);
				slope = f.format(regressionParameters[1]);
				return "CHARTSUCCESS";

			} catch (SQLException e) {
				// e.printStackTrace();
				displayScatterPlot = false;
				displayRegEq = false;
				displayScatterPlotError = true;
				scatterPlotError = "Cannot create Regression Line. Select columns with numerical data";
				return "CHARTFAILED";
			} catch (Exception e) {
				// e.printStackTrace();
				displayScatterPlot = false;
				displayRegEq = false;
				displayScatterPlotError = true;
				scatterPlotError = "Unable to create Regression Line";
				return "CHARTFAILED";
			}
		}
	}

	public String regress() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			DecimalFormat f = new DecimalFormat("#.###");
			try {
				Double tempYValue = Double.parseDouble(intercept)
						+ Double.parseDouble(slope) * xValue;
				yValue = f.format(tempYValue);
				return "CHARTSUCCESS";
			} catch (Exception e) {
				// e.printStackTrace();
				yValue = null;
				xValue = null;
				displayScatterPlotError = true;
				scatterPlotError = "Unable to calculate";
				return "CHARTFAILED";
			}

		}
	}
}