package com.szmy.pigapp.utils;

import java.security.NoSuchAlgorithmException;

public class SignException extends Exception {
	/**
	 * 初始化一个异常抓捕 
	 * 
	 * @param e
	 */
	public SignException(NoSuchAlgorithmException e) {
		super.initCause(e);
	}

	/**
	 *	同步号 
	 */
	private static final long serialVersionUID = -5949230333663353594L;
	

}
