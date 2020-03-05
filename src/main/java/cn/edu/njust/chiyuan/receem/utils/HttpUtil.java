package cn.edu.njust.chiyuan.receem.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class HttpUtil {
	public static String doPost(String url, String content) {
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/json");
			StringEntity se = new StringEntity(content);
			httppost.setEntity(se);
			//HttpResponse httpresponse = new DefaultHttpClient().execute(proxy,httppost);
			HttpResponse httpresponse = new DefaultHttpClient().execute(httppost);
			System.out.println(httpresponse.getStatusLine().getStatusCode());
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpresponse.getEntity());
				return strResult;
			} else {
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	private static JSONObject getJsonObject(JSONObject json, DefaultHttpClient client, HttpEntityEnclosingRequestBase base) throws IOException {
		JSONObject response;StringEntity s = new StringEntity(json.toString());
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");//发送json数据需要设置contentType
		base.setEntity(s);
		HttpResponse res = client.execute(base);
		HttpEntity entity = res.getEntity();
		String result = EntityUtils.toString(entity);// 返回json格式：
		response = JSONObject.fromObject(result);
		return response;
		}
	public static String doGet(String url) {
		try {
			
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Accept","application/json");
			//HttpResponse httpresponse = new DefaultHttpClient().execute(proxy,httpget);
			HttpResponse httpresponse = new DefaultHttpClient().execute(httpget);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpresponse.getEntity());
				return strResult;
			} else {
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}

	}
	
	public static JSONObject doGet2(String url){
		JSONObject response = null;
		try{
		DefaultHttpClient client = new DefaultHttpClient();
		URIBuilder builder = new URIBuilder(url);
//		Set<String> set = para.keySet();
//		for(String key: set){
//		builder.setParameter(key, para.get(key));
//		}
		HttpGet request = new HttpGet(builder.build());
//		if(StringUtils.isNotEmpty(authorValue)){
//		request.setHeader("Authorization",authorValue);
//		}
		request.setHeader("ContentTye","application/json");
		HttpResponse res = client.execute(request);
		HttpEntity entity = res.getEntity();
		String result = EntityUtils.toString(res.getEntity());// 返回json格式：
		response = JSONObject.fromObject(result);
		} catch (Exception e) {
		throw new RuntimeException(e);
		}
		return response;
		}
		//}
	
	public static JSONObject doPut(String url, String authorValue, JSONObject json){
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPut put = new HttpPut(url);
		if(StringUtils.isNotEmpty(authorValue)){
			put.setHeader("x-els-apikey",authorValue);
			}
		JSONObject response = null;
		try {
			response = getJsonObject(json, client, put);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return response;
		}
	
	
		
	public static String doPut(String urlstr,String json) {
		try {
	        URL url = new URL(urlstr);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("PUT");
	        connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("x-els-apikey", "7f59af901d2d86f78a1fd60c1bf9426a");
	        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
	        osw.write(json);
	        osw.flush();
	        osw.close();
	        System.err.println(connection.getResponseCode());
	        return connection.getContent().toString();
	        //String strResult = EntityUtils.toString(httpresponse.getEntity());
			//return strResult;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return "";

		}
		
	}
	
	public static void doDelete(String urlstr,String json) {
		try {
			
	        URL url = new URL(urlstr);
	        System.out.println("delete!!!");
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("DELETE");
	        connection.setDoOutput(true);
//	        connection.setRequestProperty("Content-Type", "application/json");
//	        connection.setRequestProperty("Accept", "application/json");
	        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
	        osw.write(json);
	        osw.flush();
	        osw.close();
	        System.out.println("code="+connection.getResponseCode());
	        System.err.println(connection.getResponseCode());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
	}
	//
	
//	public static void doPost(String urlstr,String json) {
//		try {
//	        URL url = new URL(urlstr);
//	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//	        connection.setRequestMethod("POST");
//	        connection.setDoOutput(true);
//	        connection.setRequestProperty("Content-Type", "application/json");
//	        connection.setRequestProperty("Accept", "application/json");
//	        OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
//	        osw.write(json);
//	        osw.flush();
//	        osw.close();
//	        System.err.println(connection.getResponseCode());
//	        System.out.println(connection.getContent());
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//
//		}
//	}
	
	
}
