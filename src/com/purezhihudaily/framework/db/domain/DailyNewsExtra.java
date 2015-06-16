package com.purezhihudaily.framework.db.domain;

import com.purezhihudaily.framework.db.model.GenericModel;

public class DailyNewsExtra extends GenericModel {

	private static final long serialVersionUID = 3927473715166312393L;
	
	private String comments;
	
	private String popularity;

	public String getComments() {
		return comments;
	}
	
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPopularity() {
		return popularity;
	}

	public void setPopularity(String popularity) {
		this.popularity = popularity;
	}

}
