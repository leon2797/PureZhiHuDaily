package com.purezhihudaily.framework.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="Record")
public class RecordModel extends GenericModel {

	private static final long serialVersionUID = -5987039464614301067L;
	
	@DatabaseField(id = true, canBeNull = false)
	private String newsId;
	
	@DatabaseField
	private boolean isRead;
	
	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	

}
