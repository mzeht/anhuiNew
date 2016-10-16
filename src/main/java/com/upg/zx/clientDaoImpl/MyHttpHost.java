package com.upg.zx.clientDaoImpl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import net.sf.json.JSONObject;

public class MyHttpHost {
	public static final String ENCODE_DEFAULT = "UTF-8";
	public static String serviceURL = "http://testcapture.wzyrz.cn/Zheng/proxy/newProtool";

	public HttpHost getHttpProxy() throws Exception {
		try {
			JSONObject rs = JSONObject.fromObject(doHttpProxyGET(serviceURL));
			JSONObject proxy = JSONObject.fromObject(rs.get("data"));
			System.out.println("useing proxy =====>>>>>" + proxy);
			return new HttpHost(proxy.getString("hostName"),
					proxy.getInt("port"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String doHttpProxyGET(String serviceURL) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(serviceURL);
			// 设定传输编码
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if (status == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null)
					return EntityUtils.toString(entity, ENCODE_DEFAULT);
			} else {
				throw new Exception("服务请求异常: " + status + ",【URL=" + serviceURL
						+ "】");
			}
		} finally {
			httpClient.close();
		}
		return "";
	}
}
