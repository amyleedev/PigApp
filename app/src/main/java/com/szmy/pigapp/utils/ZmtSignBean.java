package com.szmy.pigapp.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <br>
 * <b>对于通信双方，同时进行加密解密验证，保证数据的安全性</b>
 */
public class ZmtSignBean {
	
	public static final String secret="szmyzmt2016";
	public static final String ZHESHANGZHIFUSECRET="nxt_szmy";
	public static String CODE = "";

//	public static void main(String[] args) {
//		Map<String,String> map=new HashMap<String, String>();
//		map.put("CustNo", "41077026471");
//		System.out.println(sign(map));
//	}
//
public static String createPmString(Map<String,String>  map,String CODE1) {
	String sign = sign(map,CODE1);// 生成附加的加密参�?
	map.put("api_sign", sign);// 把加密参数放入参数集�?
	StringBuilder param = new StringBuilder();
	Set<String> keySet = map.keySet();
	for (String s : keySet) {
		param.append("&").append(s).append("=")
				.append(encode64(map.get(s)));
	}
	return param.toString().substring(1);
}
	/**
	 * 签名方法，用于生成签名。
	 * 
	 * @param params 服务器传递参数
	 * @param // 加密码
	 */
	public static String sign(Map<String, String> params,String CODE1) {
		CODE = CODE1;
		String result = null;
		if (params == null)
			return result;
		params.remove("api_sign");
		Iterator<String> iter = params.keySet().iterator();
		StringBuffer orgin = new StringBuffer(CODE);
		System.out.println(".......................................");
		while (iter.hasNext()) {
			String name = (String) iter.next();
			System.out.println(name+"="+params.get(name));
			orgin.append(name).append(params.get(name));
		}
		System.out.print("orgin:"+orgin);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
		} catch (Exception ex) {
			throw new RuntimeException("sign error !");
		}
		return result;
	}
   
    /**
     * 验证签名
     * 
     * @param sign			签名
     * @param parameter		参数map
     * @param
     * @return
     * @throws SignException
     */

    public static boolean validateSign(String sign, Map<String,String> parameter) throws SignException {
       if(sign==null||secret==null||parameter==null)return false; 
       return sign.equals(sign(parameter,CODE));

    } 
    
   
    /**
	 * 二行制转字符串
	 */
	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
	/**
	 * 地址编码
	 *
	 * @param src
	 * @return
	 */
	private static String encode64(String src) {
		try {
			src = URLEncoder.encode(src, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
		}
		return src;
	}
}
