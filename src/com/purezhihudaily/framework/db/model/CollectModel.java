package com.purezhihudaily.framework.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="Collect")
public class CollectModel extends GenericModel {
	
	private static final long serialVersionUID = -3135999936414096378L;

	@DatabaseField(id = true, canBeNull = false)
	private String newsid;
	
	@DatabaseField
	private String title;
	
	@DatabaseField
	private String image;
	
	@DatabaseField
	private boolean isTheme;

	public String getNewsid() {
		return newsid;
	}

	public void setNewsid(String newsid) {
		this.newsid = newsid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean getIsTheme() {
		return isTheme;
	}

	public void setIsTheme(boolean isTheme) {
		this.isTheme = isTheme;
	}
	
}
