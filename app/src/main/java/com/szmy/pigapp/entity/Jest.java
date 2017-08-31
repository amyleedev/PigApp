package com.szmy.pigapp.entity;

import java.io.Serializable;

/**
 * 笑话 http://apistore.baidu.com/astore/toolshttpproxy?apiId=usy0yw&isAworks=1
 * */
public class Jest implements Serializable {

	private static final long serialVersionUID = 5002262125378627718L;
	private String ct;
	private String text;
	private String title;
	private int type;

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
