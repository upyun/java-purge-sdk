/**
 * @filename PurgeDemo.java
 * @author lvtongda
 * @date 2014-05-24
 * @about JAVA 版本换成刷新 SDK DEMO
 */

package demo;

import com.UpYun;

public class PurgeDemo {
	// 运行前先设置以下参数
	private static final String BUCKET_NAME = "服务名称";
	private static final String USER_NAME = "操作员名";
	private static final String USER_PWD = "操作员密码";
	
	// 要刷新的 URL
	// URL 形式：http://ceshi-images.b0.upaiyun.com/100.jpg\nhttp://ceshi-images.b0.upaiyun.com/100.jpg\n
	private static String URL = "";
	
	private static UpYun upyun = null;
	
	public static void main(String[] args) {
		
		// 初始化服务
		upyun = new UpYun(BUCKET_NAME, USER_NAME, USER_PWD);
	
		// 刷新缓存
		testPurgeUrl();
	}
	
	public static void testPurgeUrl() {
		String result = upyun.purgeUrl(URL);
		System.out.println(result);
	}
}
