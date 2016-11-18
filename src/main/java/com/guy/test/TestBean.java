package com.guy.test;

import java.io.Serializable;

public class TestBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2152052862092149067L;
	
	private String name;
	private String Description;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return Description;
	}
	
	public void setDescription(String description) {
		Description = description;
	}
}
