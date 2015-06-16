package com.purezhihudaily.framework.db.domain;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import com.purezhihudaily.framework.db.model.GenericModel;

public class DailyNewsList extends GenericModel {

	private static final long serialVersionUID = 2315258146852889207L;
	
	public String date;
    public List<DailyNews> stories = new ArrayList<DailyNews>();

}
