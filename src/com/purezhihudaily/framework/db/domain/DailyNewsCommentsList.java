package com.purezhihudaily.framework.db.domain;

import java.util.ArrayList;
import java.util.List;

import com.purezhihudaily.framework.db.model.GenericModel;

public class DailyNewsCommentsList extends GenericModel {

	private static final long serialVersionUID = 7645497881772322823L;
	
	public List<DailyNewsComments> comments = new ArrayList<DailyNewsComments>();

}
