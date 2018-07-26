package com.auro.bean;

public class AuroWorkSpaceBean {

	private String title;
	
	private String url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "AuroWorkSpaceBean [title=" + title + ", url=" + url + "]";
	}
	
	
	
}
