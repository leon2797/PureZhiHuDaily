package com.purezhihudaily.framework.db.domain;

import com.purezhihudaily.framework.db.model.GenericModel;

public class DailyNewsComments extends GenericModel {

	private static final long serialVersionUID = -3330736177974586380L;
	
	public long id;
	
	public String author;
	
	public String avatar;
	
	public String content;
	
	public long time;
	
	public String likes;
	
}
