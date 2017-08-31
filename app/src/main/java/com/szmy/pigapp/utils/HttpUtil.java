package com.szmy.pigapp.utils;

import android.util.Log;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public synchronized String postDataMethod(String serviceURI,
			MultipartEntity mpEntity) {
		String responseStr = "";
		try {
			HttpPost httpRequest = new HttpPost(serviceURI);
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setTimeout(params, 6000); // 从连接池中获取连接的超时时间
			HttpConnectionParams.setConnectionTimeout(params, 15000);// 通过网络与服务器建立连接的超时时间
			HttpConnectionParams.setSoTimeout(params, 30000);// 读响应数据的超时时间
			httpRequest.setParams(params);
			// 下面开始跟服务器传递数据，使用BasicNameValuePair

			httpRequest.setEntity(mpEntity);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			final int ret = httpResponse.getStatusLine().getStatusCode();
			if (ret == HttpStatus.SC_OK) {
				responseStr = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
				// responseStr = DesEncrypt.getDesString(responseStr);
			} else {
				responseStr = "";
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("reslog", responseStr);

		return responseStr;

	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 * @param serviceURI
	 * @param tokens
	 * @return
	 */
	public synchronized String postFileMethod(File file, String serviceURI,
			String loginkey, String id, String num) {
		String responseStr = "";
		try {
			HttpPost httpRequest = new HttpPost(serviceURI);
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setTimeout(params, 6000); // 从连接池中获取连接的超时时间
			HttpConnectionParams.setConnectionTimeout(params, 15000);// 通过网络与服务器建立连接的超时时间
			HttpConnectionParams.setSoTimeout(params, 30000);// 读响应数据的超时时间
			httpRequest.setParams(params);
			// 下面开始跟服务器传递数据，使用BasicNameValuePair

			MultipartEntity mpEntity = new MultipartEntity();

			mpEntity.addPart("orderID", new StringBody(id));
			mpEntity.addPart("uuid", new StringBody(loginkey));
			mpEntity.addPart("uploadImage", new FileBody(file));
			mpEntity.addPart("orderNum", new StringBody(num));

			httpRequest.setEntity(mpEntity);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			final int ret = httpResponse.getStatusLine().getStatusCode();
			if (ret == HttpStatus.SC_OK) {
				responseStr = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
			} else {
				responseStr = "";
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("reslog", responseStr);
		return responseStr;
	}

	/**
	 * @param url
	 *            请求URL
	 * @return 请求结果
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws Exception
	 */
	public static String getRequest(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(get);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity(),
					"utf-8");
			return result;
		}
		return null;
	}

	public synchronized String postDataMethod(String serviceURI, String user,
			String pwd, String phoneModel, String equipmentNumber) {
		String responseStr = "";
		try {
			HttpPost httpRequest = new HttpPost(serviceURI);
			HttpParams params = new BasicHttpParams();
			ConnManagerParams.setTimeout(params, 6000); // 从连接池中获取连接的超时时间
			HttpConnectionParams.setConnectionTimeout(params, 15000);// 通过网络与服务器建立连接的超时时间
			HttpConnectionParams.setSoTimeout(params, 30000);// 读响应数据的超时时间
			httpRequest.setParams(params);
			// 下面开始跟服务器传递数据，使用BasicNameValuePair
			MultipartEntity mpEntity = new MultipartEntity();
			mpEntity.addPart("e.username", new StringBody(user));
			mpEntity.addPart("e.password", new StringBody(pwd));
			mpEntity.addPart("phoneModel",
					new StringBody(phoneModel.replaceAll(" ", "")));
			mpEntity.addPart("equipmentNumber",
					new StringBody(equipmentNumber.replaceAll(" ", "")));
			httpRequest.setEntity(mpEntity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			final int ret = httpResponse.getStatusLine().getStatusCode();
			if (ret == HttpStatus.SC_OK) {
				responseStr = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
			} else {
				responseStr = "";
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("reslog", responseStr);
		return responseStr;
	}

	// 发送手机号码获取短信验证码
	public static String sendSMS(String msg, String phone) {
		String result = "";
		String postUrl = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
		String account = "cf_nxt";
		String password = new MD5().GetMD5Code("shenzhoumuyi123");
		// msg = new String("您的验证码是：4321。请不要把验证码泄露给其他人。");

		try {

			URL url = new URL(postUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);// 允许连接提交信息
			connection.setRequestMethod("POST");// 网页提交方式“GET”、“POST”
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Connection", "Keep-Alive");
			StringBuffer sb = new StringBuffer();
			sb.append("account=" + account);
			sb.append("&password=" + password);
			sb.append("&mobile=" + phone);
			sb.append("&content=" + msg);
			OutputStream os = connection.getOutputStream();
			os.write(sb.toString().getBytes());
			os.close();

			String line = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			System.out.println(result);
			in.close();

		} catch (IOException e) {
			e.printStackTrace(System.out);
		}

		return result;
	}

}
