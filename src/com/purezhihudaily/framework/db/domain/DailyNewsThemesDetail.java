package com.purezhihudaily.framework.db.domain;

import java.util.ArrayList;
import java.util.List;

import com.purezhihudaily.framework.db.model.GenericModel;

public class DailyNewsThemesDetail extends GenericModel {

	private static final long serialVersionUID = 5462458846672165999L;
	
	public String name;
	
	public String description;
	
	public String image;
	
	public List<DailyNews> stories = new ArrayList<DailyNews>();

}
