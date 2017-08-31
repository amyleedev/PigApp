package com.szmy.pigapp.image;

import java.io.Serializable;


public class ImageItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3482950220465214855L;
	public String imageId;
	public String thumbnailPath;
	public String sourcePath;
	public boolean isSelected = false;
	
}
