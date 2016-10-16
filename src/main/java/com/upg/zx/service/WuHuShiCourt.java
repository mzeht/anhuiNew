package com.upg.zx.service;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upg.zx.clientDaoImpl.CheckSiteModule;
import com.upg.zx.clientDaoImpl.HttpClientImp;
import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.Court;
import com.upg.zx.dto.HttpClientParam;

/**
 * 芜湖市中级人民法院
 * 
 * @author litaotao
 * 
 */

@Service
public class WuHuShiCourt {
	private final String host = "www.wuhucourt.gov.cn";
	@Autowired
	private CourtDao whHandl;
	private HttpClientImp httpclientHandl;

	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}

	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}

	public void getUrl() {
		final String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
		HttpClientImp hc = new HttpClientImp();
		ArrayList<HttpClientParam> paramList = new ArrayList<HttpClientParam>();
		String detail = hc.getTest("gb2312", "http://www.wuhucourt.gov.cn/NewsList.aspx?TypeID=1111", "", host, "", 80);
		String para = getParameter(detail);// 得到第一页的参数
		for (int page = 3; page > 1; page++) {
			paramList = SetParamForHttpClient(page, para);
			String msg = hc.gethtmlByPost("http://www.wuhucourt.gov.cn/NewsList.aspx?TypeID=1111", "gb2312",
					"http://www.wuhucourt.gov.cn/NewsList.aspx?TypeID=1111", host, accept, paramList);
			getHtml(msg);
		}
	}

	// 得参数
	private String getParameter(String content) {
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div");
		String para = eles.get(0).getElementsByTag("input").attr("value");
		return para;
	}

	// 传参数
	public ArrayList<HttpClientParam> SetParamForHttpClient(int page, String parameter) {
		ArrayList<HttpClientParam> arraylist = new ArrayList<HttpClientParam>();
		for (int i = 0; i < 5; i++) {
			HttpClientParam param = new HttpClientParam();
			if (i == 0) {
				param.setParamName("__VIEWSTATE");
				param.setParamValue(parameter);
			} else if (i == 1) {
				param.setParamName("__VIEWSTATEGENERATOR");
				param.setParamValue("14DD91A0");
			} else if (i == 2) {
				param.setParamName("__EVENTTARGET");
				param.setParamValue("Wpg_NewList");
			} else if (i == 3) {
				param.setParamName("__EVENTARGUMENT");
				param.setParamValue(String.valueOf(page));
			} else if (i == 4) {
				param.setParamName("select3");
				param.setParamValue("文章标题");
			}
			arraylist.add(param);
		}
		return arraylist;
	}

	// 取网址
	private ArrayList<String> getHref(String content) {
		ArrayList<String> linkList = new ArrayList<String>();
		CheckSiteModule ckm = new CheckSiteModule(whHandl);
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.getElementById("DLNewsList").getElementsByTag("a");
		Elements eless = doc.getElementById("DLNewsList").getElementsByTag("span");
		String time = eless.get(2).text();
		time = time.replace("   ", "");
		if (ckm.getUpdate(time, "芜湖市中级人民法院")) {
			for (int i = 0; i < eles.size(); i++) {
				linkList.add(eles.get(i).attr("href"));
			}
		}
		return linkList;
	}

	// 取数据
	private void getHtml(String content) {
		ArrayList<String> linkList = new ArrayList<String>();
		ArrayList<Court> array = new ArrayList<Court>();
		HttpClientImp hc = new HttpClientImp();
		linkList = getHref(content);
		Elements tabletitle = null;
		int index = 0;
		for (int i = 0; i < linkList.size(); i++) {
			System.out.println(linkList.get(i));
			String htmlInfo = hc.getTest("gb2312", "http://www.wuhucourt.gov.cn/" + linkList.get(i),
					"http://www.wuhucourt.gov.cn/NewsList.aspx?TypeID=1111", host, "", 80);
			Document doc = Jsoup.parse(htmlInfo);
			Elements eles = doc.select("table#DLContent table tr");
			for (int j = 0; j < eles.size(); j++) {
				if (eles.get(j).text().contains("开庭")) {
					tabletitle = eles.get(j).getElementsByTag("td");
					index = j;
					break;
				}
			}
			for (int j = index + 1; j < eles.size(); j++) {
				Elements tablelink = eles.get(j).getElementsByTag("td");
				Court court = new Court();
				court.setDataFrom("芜湖市中级人民法院");
				for (int k = 0; k < tablelink.size(); k++) {
					String msg = tabletitle.get(k).text() + "<>" + tablelink.get(k).text().replaceAll("\\s", "");
					if (msg.contains("开庭法庭")) {
						court.setCourt(msg.split("<>")[1]);

					} else if (msg.contains("开庭日期")) {
						court.setStartTime(msg.split("<>")[1]);

					} else if (msg.contains("开庭时间")) {
						if (court.getStartTime() == null) {
							court.setStartTime(msg.split("<>")[1]);
						}
					} else if (msg.contains("案号")) {
						court.setCase_no(msg.split("<>")[1]);

					} else if (msg.contains("案由")) {
						court.setCase_res(msg.split("<>")[1]);

					} else if (msg.contains("当事人")) {
						court.setPerson(msg.split("<>")[1]);
					}
				}

				array.add(court);
			}
			whHandl.insertCourt(array);

		}
	}

	public static void main(String[] args) {
		WuHuShiCourt a = new WuHuShiCourt();
		a.getUrl();

	}
}
