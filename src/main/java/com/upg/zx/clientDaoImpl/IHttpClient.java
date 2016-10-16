package com.upg.zx.clientDaoImpl;

import java.util.ArrayList;

import com.upg.zx.dto.HttpClientParam;

public interface IHttpClient {
	public String gethtmlByGet(String encode,String url, String p_referer, String host);
	public String gethtmlByPost(String url, String encoding,String p_referer, String host,String accept,ArrayList<HttpClientParam> list);
	
}
