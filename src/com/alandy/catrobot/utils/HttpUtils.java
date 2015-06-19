package com.alandy.catrobot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLEncoder;




import java.util.Date;

import com.alandy.catrobot.bean.ChatMessage;
import com.alandy.catrobot.bean.ChatMessage.Type;
import com.alandy.catrobot.bean.Result;
import com.google.gson.Gson;

/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟开发团队 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月18日 下午8:36:52
 *
 * 描 述 ： 工具类
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class HttpUtils {
	private static final String URL = "http://www.tuling123.com/openapi/api";
	private static final String API_KEY = "a138f9de32cfe6da0e4b7b36e98f19a2";
	private static InputStream is = null;
	private static ByteArrayOutputStream baos = null;

	/**
	 * 发送一个消息，得到返回的消息·
	 * @param msg
	 * @return
	 */
	public static ChatMessage sendMessage(String msg){
		ChatMessage chatMessage = new ChatMessage();
		String jsonRes = doGet(msg);
		Gson gson = new Gson(); 
		Result result = null;
		try {
			result = gson.fromJson(jsonRes, Result.class);
			chatMessage.setMsg(result.getText());
		} catch (Exception e) {
			chatMessage.setMsg("服务器繁忙，请稍候再试");
		}
		
		chatMessage.setDate(new Date());
		chatMessage.setType(Type.INCOMING);
		return chatMessage;
	}
	
	public static String doGet(String msg) {
		String result = "";
		String url = setParams(msg);

		try {
			//设置http连接
			java.net.URL urlNet = new java.net.URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlNet
					.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			//拿到服务器返回的数据流
			is = conn.getInputStream();
			
			//设置一个缓冲区
			int len = -1;
			byte[] buf = new byte[128];
			baos = new ByteArrayOutputStream();
			//读数据流
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			result = new String(baos.toByteArray());

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (is != null) {
					is.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 拼接URL
	 * @param msg
	 * @return url
	 */
	private static String setParams(String msg) {
		String url = "";
		try {
			url = URL + "?key=" + API_KEY + "&info="
					+ URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}
}
