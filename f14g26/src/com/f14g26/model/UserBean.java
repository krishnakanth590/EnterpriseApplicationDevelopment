package com.f14g26.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean {
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private String sessionid;
	private String clientip;
	private String message;
	private String loginMessage;
	private String registerMessage;
	boolean displayRegisterMessage = false;
	boolean displayLoginMessage = false;

	public boolean isDisplayRegisterMessage() {
		return displayRegisterMessage;
	}

	public boolean isDisplayLoginMessage() {
		return displayLoginMessage;
	}

	public void setDisplayLoginMessage(boolean displayLoginMessage) {
		this.displayLoginMessage = displayLoginMessage;
	}

	public void setDisplayRegisterMessage(boolean displayRegisterMessage) {
		this.displayRegisterMessage = displayRegisterMessage;
	}

	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

	public String getRegisterMessage() {
		return registerMessage;
	}

	public void setRegisterMessage(String registerMessage) {
		this.registerMessage = registerMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

}