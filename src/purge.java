/*
 * Filename: purge.java
 * Author: lvtongda 
 * Date: 2014-05-24
 * About: JAVA 版本缓存刷新例子
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
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

public class purge {
	private static String BUCKET_NAME = ""; // 空间名
	private static String USER_NAME = ""; // 操作员帐号
	private static String USER_PWD = ""; // 操作员密码
	
	//例如：http://ceshi-images.b0.upaiyun.com/100.jpg\nhttp://ceshi-images.b0.upaiyun.com/100.jpg\n
	private static String PURGE = ""; // 要刷的链接地址
	
	private static String URL = "http://purge.upyun.com/purge/";
	
	public static void main(String args[]) {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String date = df.format(now.getTime()) + " GMT";
		String sign = md5(PURGE + "&" + BUCKET_NAME + "&" + date + "&" + md5(USER_PWD));
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(URL);
		
		post.setHeader("Expect", "");
		post.setHeader("Authorization", "UpYun " + BUCKET_NAME + ":" + USER_NAME + ":" + sign);
		post.setHeader("DATE", date);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("purge", PURGE));
		post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
		HttpResponse response;
		try {
			response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String md5(String strSrc) {
			String result = "";
			byte[] temp = null;
			MessageDigest md5 = null;
			try {
				md5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			try {
				temp = md5.digest(strSrc.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			
			return result;
	}
	
}
