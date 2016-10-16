package com.upg.zx.clientDaoImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;

import com.upg.zx.dto.HttpClientParam;
import com.upg.zx.dto.Proxy1;

public class HttpClientImp implements IHttpClient{
	
	private static Integer SC_OK = 200; 
	private String ip = "";
	private String proxy = "";
	private int port = 80;
	
	
	
	public String gethtmlByPost(String url, String encoding,String p_referer, String host,String accept,ArrayList<HttpClientParam> list){
		String responseMsg ="";
		HttpClient httpClient =new HttpClient();
		httpClient.getParams().setContentCharset(encoding);
		PostMethod postMethod =new PostMethod(url);

		postMethod.addRequestHeader("Referer", p_referer);

		postMethod.addRequestHeader("Accept",accept);
		
		postMethod.addRequestHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		
		postMethod.addRequestHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:37.0) Gecko/20100101 Firefox/37.0");
		
		postMethod.addRequestHeader("Host", host);
		postMethod.addRequestHeader("Connection", "keep-alive");
		postMethod.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");

		for (int i =0; i < list.size(); i++){
			postMethod.addParameter(list.get(i).getParamName(), list.get(i).getParamValue());
		}
				
		try {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		httpClient.getHttpConnectionManager().getParams()
		.setConnectionTimeout(10000);
		int num = httpClient.executeMethod(postMethod);
		if (num ==SC_OK) { 

			responseMsg =postMethod.getResponseBodyAsString();
		}
		} catch (Exception e) {
		e.printStackTrace();
		} finally {
		postMethod.releaseConnection();
		}
		return responseMsg;
	}
	
	/**
	 * GET方式获取网站内容
	 * @throws IOException 
	 * @throws HttpException 
	 *
	 * */
	public String gethtmlByGet(String encode,String p_url, String p_referer, String host){
			String return_str = "";
			HttpClient httpClient =new HttpClient();
			httpClient.getHostConfiguration().setProxy(ip,port); 
			httpClient.getParams().setAuthenticationPreemptive(true);
			httpClient.getParams().setContentCharset(encode);
			GetMethod getMethod = new GetMethod(p_url);
			if (p_referer != null && !"".equals(p_referer)) {
				getMethod.addRequestHeader("Referer", p_referer);
			}
			getMethod
					.addRequestHeader("Accept","*/*");
			getMethod
					.addRequestHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
			getMethod.addRequestHeader("Host", host);
			getMethod.addRequestHeader("Connection", "keep-alive");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		try {	
			httpClient.executeMethod(getMethod);
			return_str = new String(getMethod.getResponseBody(), encode);
		
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getMethod.releaseConnection();
		return return_str;
	}
	
	
	
	/**
	 * 
	 * 测试
	 * 
	 **/
	public String getTest(String encode,String p_url, String p_referer, String host,String proxy,int port){
		String return_str = "";
		HttpClient httpClient =new HttpClient();
		httpClient.getParams().setContentCharset(encode);
		GetMethod getMethod = new GetMethod(p_url);
		getMethod.setFollowRedirects(false);
		if (!"".equals(proxy) && proxy!=null){
			httpClient.getHostConfiguration().setProxy(proxy,port); 
			httpClient.getParams().setAuthenticationPreemptive(true);
		}
		if (p_referer != null && !"".equals(p_referer)) {
			getMethod.addRequestHeader("Referer", p_referer);
		}
		getMethod
		.addRequestHeader("Accept","*/*");
		getMethod
		.addRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
		getMethod.addRequestHeader("Host", host);
		getMethod.addRequestHeader("Connection", "keep-alive");
		try {	
			
			httpClient.executeMethod(getMethod);
			return_str = new String(getMethod.getResponseBody(), encode);
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getMethod.releaseConnection();
		return return_str;
	}
	
	/**
	 * 
	 * httpClient 4.3.2
	 * litt
	 * 
	 * */
	public String httpGet(String encode,String url,String proxy,int port) {
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
		if(StringUtils.isNotEmpty(proxy)&&port!=0){
			HttpHost p = new HttpHost(proxy, port);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(p);
			httpClient= HttpClients.custom()
					.setRoutePlanner(routePlanner).build();
		}
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			} else {
				httpGet.abort();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * httpClient 4.3.2
	 * @author litt
	 * 
	 * */
	public String httpPost(String encode,String url,String host,String proxy,int port,List<NameValuePair> list) {
		String result = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
		if(StringUtils.isNotEmpty(proxy)&&port!=0){
			HttpHost p = new HttpHost(proxy, port);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(p);
			httpClient= HttpClients.custom()
					.setRoutePlanner(routePlanner).build();
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpPost.addHeader("Host", host);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(list,encode));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			} else {
				httpPost.abort();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	public String getMatcherStr(String zwyx, String string) {
		Pattern p = Pattern.compile(string);
		Matcher m = p.matcher(zwyx);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	
}


