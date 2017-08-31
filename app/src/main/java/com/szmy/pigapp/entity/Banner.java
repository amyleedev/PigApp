package com.szmy.pigapp.entity;

import java.io.Serializable;

public class Banner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4154792235445723766L;
	private String id;
	private String imgPath;
	private String srcPath;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

}
