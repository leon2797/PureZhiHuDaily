package com.purezhihudaily.framework.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="News")
public class NewsModel extends GenericModel {
	
	private static final long serialVersionUID = -2771341871440155479L;
	
	@DatabaseField(id = true, canBeNull = false)
	private String id;

	@DatabaseField
	private String key;
	
	@DatabaseField
	private String content;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
