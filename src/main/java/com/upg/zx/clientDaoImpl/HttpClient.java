package com.upg.zx.clientDaoImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Repository;

import com.upg.zx.clientDao.HttpClientDao;

@Repository(value = "httpClientDao")
public class HttpClient implements HttpClientDao {

	public String httpGet(BasicCookieStore basicCookieStore, String encode,
			String url, Map<String, String> map, String proxy, int port) {
		String result = "";
		HttpClientBuilder builder = HttpClients.custom();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(20000).setConnectTimeout(20000).build();// 设置请求和传输超时时间
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		if (StringUtils.isNotEmpty(proxy) && port != 0) {
			HttpHost p = new HttpHost(proxy, port);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					p);
			builder = HttpClients.custom().setRoutePlanner(routePlanner);
		}
		if (basicCookieStore != null)
			builder = builder.setDefaultCookieStore(basicCookieStore);
		if (map != null && map.size() > 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpClient httpClient = builder.build();
		try {
			response = httpClient.execute(httpGet);
			Thread.sleep((long) (Math.random() * 1500 + 1500));
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			} else if (response.getStatusLine().getStatusCode() == 404) {
				return "页面丢失";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String httpGetForNX(BasicCookieStore basicCookieStore,
			String encode, String url, Map<String, String> map, String proxy,
			int port) {
		HttpClientBuilder builder = HttpClients.custom();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		if (StringUtils.isNotEmpty(proxy) && port != 0) {
			HttpHost p = new HttpHost(proxy, port);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					p);
			builder = HttpClients.custom().setRoutePlanner(routePlanner);
		}
		if (basicCookieStore != null)
			builder = builder.setDefaultCookieStore(basicCookieStore);
		if (map != null && map.size() > 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpClient httpClient = builder.build();
		StringBuffer sb = new StringBuffer();
		try {
			response = httpClient.execute(httpGet);
			Thread.sleep((long) (Math.random() * 1500 + 1500));
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				InputStream is = httpEntity.getContent();
				byte[] b = new byte[1024];
				int len;
				while ((len = is.read(b)) != -1) {
					sb.append(new String(b, 0, len, encode));
				}
			} else if (response.getStatusLine().getStatusCode() == 404) {
				return "页面丢失";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String httpGet(BasicCookieStore basicCookieStore, String encode,
			String url, Map<String, String> map, MyHttpHost ip) {
		String result = "";
		HttpClientBuilder builder = HttpClients.custom();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		HttpHost p;
		try {
			p = ip.getHttpProxy();
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					p);
			builder = HttpClients.custom().setRoutePlanner(routePlanner);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (basicCookieStore != null)
			builder = builder.setDefaultCookieStore(basicCookieStore);
		if (map != null && map.size() > 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpClient httpClient = builder.build();
		try {
			response = httpClient.execute(httpGet);
			Thread.sleep((long) (Math.random() * 1500 + 1500));
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			} else if (response.getStatusLine().getStatusCode() == 404) {
				return "页面丢失";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String httpGetForFJ(BasicCookieStore basicCookieStore,
			String encode, String url, Map<String, String> map, MyHttpHost ip,
			boolean flag) {
		String result = "";
		HttpClientBuilder builder = HttpClients.custom();
		RequestConfig requestConfig = null;
		if (flag) {
			requestConfig = RequestConfig.custom().setSocketTimeout(10000)
					.setConnectTimeout(10000).setRedirectsEnabled(false)
					.build();
		} else
			requestConfig = RequestConfig.custom().setSocketTimeout(10000)
					.setConnectTimeout(10000).setRedirectsEnabled(false)
					.build();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		HttpHost p;
		try {
			p = ip.getHttpProxy();
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					p);
			builder = HttpClients.custom().setRoutePlanner(routePlanner);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (basicCookieStore != null)
			builder = builder.setDefaultCookieStore(basicCookieStore);
		if (map != null && map.size() > 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpClient httpClient = builder.build();
		try {
			response = httpClient.execute(httpGet);
			Thread.sleep((long) (Math.random() * 1500 + 1500));
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			} else if (response.getStatusLine().getStatusCode() >= 300
					&& response.getStatusLine().getStatusCode() <= 307) {
				String msg = response.getFirstHeader("Set-Cookie").toString();
				String newUrl = url + msg.replace("Set-Cookie: ", "&");
				return newUrl;
			} else if (response.getStatusLine().getStatusCode() == 404) {
				return "页面丢失";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}


	public String httpPost(BasicCookieStore basicCookieStore, String encode,
			String url, Map<String, String> map,
			List<NameValuePair> formparams, MyHttpHost ip) {
		String result = "";
		HttpClientBuilder builder = HttpClients.custom();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		HttpHost p;
		try {
			p = ip.getHttpProxy();
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					p);
			builder = HttpClients.custom().setRoutePlanner(routePlanner);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (basicCookieStore != null)
			builder = builder.setDefaultCookieStore(basicCookieStore);
		if (map.size() > 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpClient httpClient = builder.build();
		UrlEncodedFormEntity entity;
		try {
			Thread.sleep((long) (Math.random() * 1500 + 1500));
			entity = new UrlEncodedFormEntity(formparams, encode);
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public String httpPost(BasicCookieStore basicCookieStore, String encode,
			String url, Map<String, String> map,
			List<NameValuePair> formparams, String proxy, int port) {
		String result = "";
		HttpClientBuilder builder = HttpClients.custom();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(10000).setConnectTimeout(10000).build();// 设置请求和传输超时时间
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		if (StringUtils.isNotEmpty(proxy) && port != 0) {
			HttpHost p = new HttpHost(proxy, port);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					p);
			builder = HttpClients.custom().setRoutePlanner(routePlanner);
		}
		if (basicCookieStore != null)
			builder = builder.setDefaultCookieStore(basicCookieStore);
		if (map.size() > 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpClient httpClient = builder.build();
		UrlEncodedFormEntity entity;
		try {
			Thread.sleep((long) (Math.random() * 1500 + 1000));
			entity = new UrlEncodedFormEntity(formparams, encode);
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @Description: 上海专用，100S响应时间
	 * @return
	 */
	public String httpPostForSH(BasicCookieStore basicCookieStore,
			String encode, String url, Map<String, String> map,
			List<NameValuePair> formparams, String proxy, int port) {
		String result = "";
		HttpClientBuilder builder = HttpClients.custom();
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(100000).setConnectTimeout(100000).build();// 设置请求和传输超时时间
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		CloseableHttpResponse response = null;
		if (StringUtils.isNotEmpty(proxy) && port != 0) {
			HttpHost p = new HttpHost(proxy, port);
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					p);
			builder = HttpClients.custom().setRoutePlanner(routePlanner);
		}
		if (basicCookieStore != null)
			builder = builder.setDefaultCookieStore(basicCookieStore);
		if (map.size() > 0) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpClient httpClient = builder.build();
		UrlEncodedFormEntity entity;
		try {
			Thread.sleep((long) (Math.random() * 1500 + 1000));
			entity = new UrlEncodedFormEntity(formparams, encode);
			httpPost.setEntity(entity);
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = response.getEntity();
				result = EntityUtils.toString(httpEntity, encode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
