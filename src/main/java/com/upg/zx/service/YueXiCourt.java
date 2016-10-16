package com.upg.zx.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upg.zx.clientDaoImpl.CheckSiteModule;
import com.upg.zx.clientDaoImpl.HttpClientImp;
import com.upg.zx.dao.CourtDao;
import com.upg.zx.dto.Court;

/**
 * 岳西市法院
 * 
 * @author 001552
 */
@Service
public class YueXiCourt {
	@Autowired
	private CourtDao yxHandl;
	private String updatedate = "";

	// 翻页
	public void pageTurn() {
		for (int i = 5; i > 1; i--) {
			System.out.println(i + "页");
			getHref("http://www.yxfyw.gov.cn/content/channel/52e1f3387f8b9a632f0148f5/page-" + i + "/");
		}
	}

	// 抓href
	public void getHref(String url) {
		HttpClientImp hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(yxHandl);
		String msg = hc.getTest("utf-8", url, "http://www.yxfyw.gov.cn/", "www.yxfyw.gov.cn", "", 80);
		Document doc = Jsoup.parse(msg);
		Elements links = doc.select("ul.is-listnews").select("a");
		Elements eles = doc.select("ul.is-listnews").get(0).getElementsByTag("li");
		String temp = eles.get(0).text();
		if (temp.contains("-")) {
			updatedate = temp.substring(0, temp.lastIndexOf("-") + 3);
		}
		if (ckm.getUpdate(updatedate, "岳西市法院")) {
			for (Element link : links) {
				getMsg(link.attr("href"));
				// System.out.println(link.attr("href"));
			}
		}
	}

	// 抓法庭
	public void getMsg(String url) {
		System.out.println("http://www.yxfyw.gov.cn" + url);
		HttpClientImp hc = new HttpClientImp();
		String msg = hc.getTest("utf-8", "http://www.yxfyw.gov.cn" + url, "", "www.yxfyw.gov.cn", "", 80);
		Document doc = Jsoup.parse(msg);
		Elements links = doc.select("div.is-newscontnet").select("p");
		Court court = new Court();
		court.setDataFrom("岳西市法院");
		for (Element link : links) {
			if (link.text().contains("定于") && link.text().contains("在")) {
				int begin = link.text().indexOf("定于");
				int end = link.text().indexOf("在");
				String tim = link.text().substring(begin + 2, end);
				System.out.println(tim);
				court.setStartTime(tim);
				String case_res = link.text().substring(end, link.text().length());
				System.out.println(case_res);
				court.setCase_res(case_res);
			}
		}
		if (court.getCase_res() != null) {
			yxHandl.insertCourt(court);
		}

	}

	public static void main(String[] args) {
		YueXiCourt court = new YueXiCourt();
		court.pageTurn();
	}
}
