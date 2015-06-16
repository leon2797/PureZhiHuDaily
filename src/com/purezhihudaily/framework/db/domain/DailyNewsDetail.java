package com.purezhihudaily.framework.db.domain;

import java.util.ArrayList;

import com.purezhihudaily.framework.db.model.GenericModel;

public class DailyNewsDetail extends GenericModel {
	
	private static final long serialVersionUID = 3363825530357267211L;
	
	public String body;
	public String image_source;
	public String title;
	public String image;
	public String share_url;
	public ArrayList<String> js;
	public int type;
	public String ga_prefix;
	public long id;
	public ArrayList<String> css;
}