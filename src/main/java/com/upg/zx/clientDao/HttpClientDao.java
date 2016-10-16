package com.upg.zx.clientDao;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.BasicCookieStore;

public interface HttpClientDao {
	public String httpGet(BasicCookieStore basicCookieStore, String encode,
			String url, Map<String, String> map, String proxy, int port);

	public String httpPost(BasicCookieStore basicCookieStore, String encode,
			String url, Map<String, String> map,
			List<NameValuePair> formparams, String proxy, int port);

}
