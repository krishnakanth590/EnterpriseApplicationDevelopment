package com.f14g26.model;

import java.io.*;
import java.util.*;
import java.sql.Date;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

@ManagedBean(name = "fileActionBean")
@SessionScoped
public class FileActionBean {

	private UploadedFile uploadedFile;
	private String fileLabel;
	private String fileName;
	private long fileSize;
	private Boolean fileImport = false;
	private Boolean fileImportError = true;
	private Boolean renderParseTable = false;
	private int numberRows = 0;
	private String fileContentType;
	private String uploadedFileContents;
	private ArrayList<ImportDataBean> idb;
	private ArrayList<StringBuffer> insertQueries;
	private int totalRows = 0;
	private String outputFormat = "xml";

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public int getNumberRows() {
		return numberRows;
	}

	public void setNumberRows(int numberRows) {
		this.numberRows = numberRows;
	}

	public Boolean getFileImport() {
		return fileImport;
	}

	public void setFileImport(Boolean fileImport) {
		this.fileImport = fileImport;
	}

	public Boolean getFileImportError() {
		return fileImportError;
	}

	public void setFileImportError(Boolean fileImportError) {
		this.fileImportError = fileImportError;
	}

	public Boolean getRenderParseTable() {
		return renderParseTable;
	}

	public void setRenderParseTable(Boolean renderParseTable) {
		this.renderParseTable = renderParseTable;
	}

	public String getFileLabel() {
		return fileLabel;
	}

	public void setFileLabel(String fileLabel) {
		this.fileLabel = fileLabel;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getUploadedFileContents() {
		return uploadedFileContents;
	}

	public void setUploadedFileContents(String uploadedFileContents) {
		this.uploadedFileContents = uploadedFileContents;
	}

	public ArrayList<ImportDataBean> getIdb() {
		return idb;
	}

	public void setIdb(ArrayList<ImportDataBean> idb) {
		this.idb = idb;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	FacesContext context = FacesContext.getCurrentInstance();
	String path = context.getExternalContext().getRealPath("/temp");

	public String processFileUpload() {

		uploadedFileContents = null;
		// Get instance of Message bean
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			MessageBean mb = (MessageBean) session.getAttribute("messageBean");
			File tempFile = null;
			FileOutputStream fos = null;

			int nCols = 0;
			int n = 0;
			try {
				mb.setFileImportMsg("");
				fileName = uploadedFile.getName();
				fileSize = uploadedFile.getSize();
				fileContentType = uploadedFile.getContentType();

				// next line if want upload in String for memory processing
				// uploadedFileContents = new String(uploadedFile.getBytes());

				tempFile = new File(path + "/" + fileName);
				fos = new FileOutputStream(tempFile);

				// next line if want file uploaded to disk
				fos.write(uploadedFile.getBytes());
				fos.close();

				Scanner s = new Scanner(tempFile);
				String input;
				Scanner d;
				input = s.nextLine();
				String[] strArr = input.split(",");
				nCols = strArr.length;
				idb = new ArrayList<ImportDataBean>(nCols);
				// variables - header line
				for (int i = 0; i < nCols; i++) {
					idb.add(new ImportDataBean());
					idb.get(i).setVariableName(strArr[i]);
				}
				// Getting the number of rows in the uploaded file by creating a
				// duplicate scanner
				Scanner dummy = new Scanner(tempFile);
				dummy.nextLine(); // Skipping the header row
				while (dummy.hasNext()) {
					input = dummy.nextLine();
					totalRows++;
				}
				dummy.close();
				insertQueries = new ArrayList<StringBuffer>(totalRows);
				while (s.hasNext()) {
					insertQueries.add(new StringBuffer("INSERT INTO "
							+ fileLabel + " VALUES(null,"));

					input = s.nextLine();
					int nColsTemp = input.split(",").length;
					strArr = input.split(",", idb.size() + 1);
					nCols = strArr.length;
					for (int i = 0; i < nCols; i++) {
						if (n == 0) {
							String type = "STRING";
							String dataType = "CATEGORICAL";
							try {
								Date dParse = Date.valueOf(strArr[i]);
								type = "DATE";
								dataType = "QUANTITATIVE";
							} catch (Exception e) {
								type = "STRING";
								dataType = "CATEGORICAL";
							}

							if (!type.equalsIgnoreCase("date"))
								try {
									Integer.parseInt(strArr[i]);
									type = "INTEGER";
									dataType = "COUNT";
								} catch (NumberFormatException e) {
									type = "STRING";
									dataType = "CATEGORICAL";
								}

							if ((!type.equalsIgnoreCase("integer"))
									&& (!type.equalsIgnoreCase("date")))
								try {
									Double.parseDouble(strArr[i]); // NumberFormatException
									type = "DOUBLE";
									dataType = "QUANTITATIVE";
								} catch (NumberFormatException e) {
									type = "STRING";
									dataType = "CATEGORICAL";
								}

							idb.get(i).setDataType(type);
							idb.get(i).setDataValue(strArr[i]);
							idb.get(i).setVariableType(dataType);
						}

						// Appending each column value to the insert query
						if (i == nCols - 1) {
							if (strArr[i].length() == 0
									&& ((idb.get(i).getDataType()
											.equalsIgnoreCase("integer")) || (idb
											.get(i).getDataType()
											.equalsIgnoreCase("double")))) {
								insertQueries.get(n).append(null + ")");
							} else {
								insertQueries.get(n).append(
										"\"" + strArr[i] + "\")");
							}
						} else {
							if (strArr[i].length() == 0
									&& ((idb.get(i).getDataType()
											.equalsIgnoreCase("integer")) || (idb
											.get(i).getDataType()
											.equalsIgnoreCase("double")))) {
								insertQueries.get(n).append(null + ",");
							} else {
								insertQueries.get(n).append(
										"\"" + strArr[i] + "\",");
							}
						}
					}
					d = new Scanner(input);
					d.useDelimiter(",");
					n++;

				}
				s.close();
			} catch (IOException e) {
				// e.printStackTrace();
				mb.setFileImportMsg("Error reading data from file");
				fileImportError = true;
				fileLabel = "";
				fileImport = false;
				totalRows = 0;
				numberRows = 0;
				return "IMPORTFAILED";
			} catch (Exception e) {
				mb.setFileImportMsg("Error reading data from file");
				fileImportError = true;
				fileLabel = "";
				fileImport = false;
				numberRows = 0;
				totalRows = 0;
				return "IMPORTFAILED";
			}
			renderParseTable = true;
			return "IMPORTSUCCESS";
		}
	}

	public ArrayList<StringBuffer> getInsertQueries() {
		return insertQueries;
	}

	public void setInsertQueries(ArrayList<StringBuffer> insertQueries) {
		this.insertQueries = insertQueries;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public String processDataImport() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			MessageBean mb = (MessageBean) session.getAttribute("messageBean");
			DbBean db = (DbBean) session.getAttribute("dbBean");

			StringBuffer sb = new StringBuffer("CREATE TABLE " + fileLabel
					+ " (S_No int AUTO_INCREMENT, ");
			// Check the below line
			int nCols = (idb == null) ? 0 : idb.size();
			for (int i = 0; i < nCols; i++) {
				if (idb.get(i).getDataType().equals("STRING")) {
					idb.get(i).setDataType("CHAR(200)");
				} else if (idb.get(i).getDataType().equals("DOUBLE")) {
					idb.get(i).setDataType("DOUBLE");
				} else if (idb.get(i).getDataType().equals("INTEGER")) {
					idb.get(i).setDataType("INT");
				} else if (idb.get(i).getDataType().equals("DATE")) {
					idb.get(i).setDataType("TIMESTAMP");
				}
				sb.append(idb.get(i).getVariableName() + " "
						+ idb.get(i).getDataType());
				if (i != (nCols - 1))
					sb.append(", ");
			}
			sb.append(" , PRIMARY KEY (S_No))");
			db.setQueryType("INSERT");
			boolean insertStatus = false;
			boolean tableCreationStatus = false;
			// Creating table
			try {
				if (db.executeSQL(sb.toString())) {
					numberRows = totalRows;
					tableCreationStatus = true;
				}
			} catch (MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				fileImport = false;
				mb.setFileImportMsg("MySQL Syntax error. Table creation failed");
				numberRows = 0;
				totalRows = 0;
				return "INSERTFAILED";
			} catch (NullPointerException e) {
				fileImport = false;
				mb.setFileImportMsg("Cannot establish connection to the database. Table creation failed");
				// e.printStackTrace();
				numberRows = 0;
				totalRows = 0;
				return "INSERTFAILED";
			} catch (SQLException e) {
				fileImport = false;
				mb.setFileImportMsg("SQL Exception occured. Table creation failed");
				// e.printStackTrace();
				numberRows = 0;
				totalRows = 0;
				return "INSERTFAILED";
			} catch (Exception e) {
				fileImport = false;
				mb.setFileImportMsg("Exception occured. Table creation failed");
				// e.printStackTrace();
				numberRows = 0;
				totalRows = 0;
				return "INSERTFAILED";
			}
			// Inserting data
			if (tableCreationStatus == true) {
				try {
					insertStatus = db.batchExecute(insertQueries);
					if (insertStatus) {
						totalRows = 0;
						fileImport = true;
						return "INSERTSUCCESS";
					}
				} catch (MySQLSyntaxErrorException e) {
					// e.printStackTrace();
					numberRows = 0;
					totalRows = 0;
					fileImport = false;
					mb.setFileImportMsg("MySQL Syntax error. Cannot insert data");
				} catch (NullPointerException e) {
					fileImport = false;
					mb.setFileImportMsg("Cannot establish connection to the database. Cannot insert data");
					numberRows = 0;
					totalRows = 0;
					// e.printStackTrace();
				} catch (SQLException e) {
					fileImport = false;
					mb.setFileImportMsg("SQL Exception occured. Cannot insert data");
					numberRows = 0;
					totalRows = 0;
					// e.printStackTrace();
				} catch (Exception e) {
					fileImport = false;
					mb.setFileImportMsg("Exception occured");
					numberRows = 0;
					totalRows = 0;
					// e.printStackTrace();
				}
			}
			// Drop the table if insert operation fails
			try {
				if (insertStatus == false) {
					db.setQueryType("DROP");
					String dropQuery = "DROP TABLE " + fileLabel;
					db.executeSQL(dropQuery);
					return "INSERTFAILED";
				}
			} catch (MySQLSyntaxErrorException e) {
				// e.printStackTrace();
				fileImport = false;
				return "INSERTFAILED";
			} catch (NullPointerException e) {
				fileImport = false;
				// e.printStackTrace();
				return "INSERTFAILED";
			} catch (SQLException e) {
				fileImport = false;
				// e.printStackTrace();
				return "INSERTFAILED";
			} catch (Exception e) {
				fileImport = false;
				// e.printStackTrace();
				return "INSERTFAILED";
			}
			return "INSERTSUCCESS";
		}
	}

	public String processFileDownload() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		if (session.getAttribute("logout") == null
				|| session.getAttribute("logout") == "yes") {
			return "GOHOME";
		} else {
			UserBean ub = (UserBean) session.getAttribute("userBean");
			DbBean db = (DbBean) session.getAttribute("dbBean");
			PopulateList pl = (PopulateList) session
					.getAttribute("populateList");
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			FileOutputStream fos = null;
			String path = fc.getExternalContext().getRealPath("/temp");
			String fileName;
			String fileNameBase;
			File f;
			String sqlQuery = "SELECT * FROM " + pl.getSelectTable();
			if (outputFormat.equalsIgnoreCase("csv")) {
				try {
					fileNameBase = pl.getSelectTable() + ".csv";
					fileName = path + "/" + ub.getUsername() + "_"
							+ fileNameBase;
					f = new File(fileName);
					db.setQueryType("SELECT");
					db.executeSQL(sqlQuery);
					Result result = ResultSupport.toResult(db.getRs());
					Object[][] sData = result.getRowsByIndex();
					String columnNames[] = result.getColumnNames();
					StringBuffer sb = new StringBuffer();
					fos = new FileOutputStream(fileName);
					for (int i = 0; i < columnNames.length; i++) {
						if (i == columnNames.length - 1) {
							sb.append(columnNames[i].toString());
						} else {
							sb.append(columnNames[i].toString() + ",");
						}
					}
					sb.append("\n");
					fos.write(sb.toString().getBytes());
					for (int i = 0; i < sData.length; i++) {
						sb = new StringBuffer();
						for (int j = 0; j < sData[0].length; j++) {
							if (j == sData[0].length - 1) {
								sb.append(sData[i][j].toString());
							} else {
								sb.append(sData[i][j].toString() + ",");
							}
						}
						sb.append("\n");
						fos.write(sb.toString().getBytes());
					}
					fos.flush();
					fos.close();
				} catch (MySQLSyntaxErrorException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (SQLException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (FileNotFoundException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (IOException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "IMPORTFAILED";
				} catch (Exception e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				}
				String mimeType = ec.getMimeType(fileName);
				FileInputStream in = null;
				byte b;
				ec.responseReset();
				ec.setResponseContentType(mimeType);
				ec.setResponseContentLength((int) f.length());
				ec.setResponseHeader("Content-Disposition",
						"attachment; filename=\"" + fileNameBase + "\"");
				try {
					in = new FileInputStream(f);
					OutputStream output = ec.getResponseOutputStream();
					while (true) {
						b = (byte) in.read();
						if (b < 0)
							break;
						output.write(b);
					}
				} catch (IOException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (Exception e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			} else if (outputFormat.equalsIgnoreCase("xml")) {
				try {
					fileNameBase = pl.getSelectTable() + ".xml";
					fileName = path + "/" + ub.getUsername() + "_"
							+ fileNameBase;
					f = new File(fileName);
					db.setQueryType("SELECT");
					db.executeSQL(sqlQuery);
					Result result = ResultSupport.toResult(db.getRs());
					Object[][] sData = result.getRowsByIndex();
					String columnNames[] = result.getColumnNames();
					StringBuffer sb = new StringBuffer();
					fos = new FileOutputStream(fileName);
					sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					sb.append("\n");
					sb.append("<" + pl.getSelectTable() + ">");
					sb.append("\n");
					fos.write(sb.toString().getBytes());
					for (int i = 0; i < sData.length; i++) {
						sb = new StringBuffer();
						sb.append("<S_No id = \"" + sData[i][0].toString()
								+ "\">");
						sb.append("\n");
						for (int j = 1; j < sData[0].length; j++) {
							sb.append("<" + columnNames[j] + ">");
							sb.append(sData[i][j].toString());
							sb.append("</" + columnNames[j] + ">");
							sb.append("\n");
						}
						sb.append("</S_No>");
						sb.append("\n");
						if (i == sData.length - 1) {
							sb.append("</" + pl.getSelectTable() + ">");
						}
						fos.write(sb.toString().getBytes());
					}
					fos.flush();
					fos.close();
				} catch (MySQLSyntaxErrorException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (SQLException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (FileNotFoundException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (IOException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "IMPORTFAILED";
				} catch (Exception e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				}
				String mimeType = ec.getMimeType(fileName);
				FileInputStream in = null;
				byte b;
				ec.responseReset();
				ec.setResponseContentType(mimeType);
				ec.setResponseContentLength((int) f.length());
				ec.setResponseHeader("Content-Disposition",
						"attachment; filename=\"" + fileNameBase + "\"");
				try {
					in = new FileInputStream(f);
					OutputStream output = ec.getResponseOutputStream();
					while (true) {
						b = (byte) in.read();
						if (b < 0)
							break;
						output.write(b);
					}
				} catch (IOException e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} catch (Exception e) {
					// e.printStackTrace();
					fileLabel = "";
					return "EXPORTFAILED";
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			}
			fc.responseComplete();
			return "EXPORTSUCCESS";
		}
	}
}
