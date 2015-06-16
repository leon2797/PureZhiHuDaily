package com.purezhihudaily.framework.db.domain;

import java.util.ArrayList;

import com.purezhihudaily.framework.db.model.GenericModel;

public class DailyNews extends GenericModel {

	private static final long serialVersionUID = -5872171817956928785L;
	
	public long id;
    public int type;
    public String title;
    public String share_url;
    public ArrayList<String> images;
    public ArrayList<String> css;
    public boolean isRead = false;
    public boolean isTag = false;
    public boolean isTheme = false;

}
