/**
 * @filename UpYun.java
 * @author lvtongda 
 * @date 2014-05-24
 * @about JAVA 版本缓存刷新 SDK
 */

package com;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;

public class UpYun {
	
	/** 默认的编码格式 */
	private static final String UTF8 = "UTF-8";

	/** SDK 版本号*/
	private static String VERSION = "2.0";
	
	private static String EXPECT = "Expect";
	private static String AUTHORIZATION = "Authorization";
	private static String DATE = "Date";
	
	public static String PURGE_API = "http://purge.upyun.com/purge/";
	
	// 服务名称
	protected String bucketName = null; 
	// 操作员名
	protected String userName = null; 
	// 操作员密码
	protected String password = null;

	/**
	 * 初始化 UpYun 接口
	 * @param bucketName
	 * @param userName
	 * @param password
	 */
	public UpYun(final String bucketName, final String userName, final String password) {
		this.bucketName = bucketName;
		this.userName = userName;
		this.password = md5(password);	
	}
	
	/**
	 * 获取当前 SDK 的版本号
	 * @return
	 */
	public String version() {
		return VERSION;
	}
	
	/**
	 * 刷新缓存
	 * @param url
	 * @return
	 */
	public String purgeUrl(String url) {

		String result = null;
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(PURGE_API);
		
		String sign = md5(url + "&" + bucketName + "&" + getGMTDate() + "&" + password);
		post.setHeader(EXPECT, "");
		post.setHeader(AUTHORIZATION, "UpYun " + bucketName + ":" + userName + ":" + sign);
		post.setHeader(DATE, getGMTDate());
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("purge", url));
		post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			result = EntityUtils.toString(response.getEntity());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 对字符串进行 MD5 加密
	 * @param strSrc 
	 * @return
	 */
	public static String md5(String strSrc) {
			String result = "";
			byte[] temp = null;
			MessageDigest md5 = null;
			
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			try {
				temp = md5.digest(strSrc.getBytes(UTF8));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			
			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			
			return result;
	}
	
	/**
	 * 获取 GMT 格式时间戳 
	 * @return
	 */
	private String getGMTDate() {
		SimpleDateFormat df = new SimpleDateFormat(
				"E, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(new Date());
	}
}
