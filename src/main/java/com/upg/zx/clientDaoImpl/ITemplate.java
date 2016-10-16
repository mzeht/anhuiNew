package com.upg.zx.clientDaoImpl;

import java.util.ArrayList;
import java.util.Map;

import com.upg.zx.dto.Court;
import com.upg.zx.dto.HttpClientParam;

public interface ITemplate {
	public  ArrayList<Court> analyzeHtml(String content);
	public ArrayList<HttpClientParam> SetParamForHttpClient(int page,String courtNo);
	public int getPageByjsoup(String content);
	public int getPageforPhptemplate(String content,String tag);
	public ArrayList  getLink(String content,Map<String,String> map);
}
