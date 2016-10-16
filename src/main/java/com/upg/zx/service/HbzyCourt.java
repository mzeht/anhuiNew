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

/**
 * 淮北市中级人民法院
 * 
 * @author lisheng
 * 
 *         2016-9-26 网站无数据 跳过
 */
@Service
public class HbzyCourt {

	private final static String HOST = "www.hbcourt.gov.cn";

	@Autowired
	private CourtDao hbzyHandl;

	private HttpClientImp httpclientHandl;
	private String updatedate;

	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}

	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}

	public void getInfo() {
		// http://www.hbcourt.gov.cn/plus/list.php?tid=57&PageNo=2
		HttpClientImp httpclientHandl = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(hbzyHandl);
		ArrayList linkList = new ArrayList();
		String url = "http://www.hbcourt.gov.cn/plus/list.php?tid=57";
		String detailUrl = "http://www.hbcourt.gov.cn";
		boolean flag = false;
		String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
		
		for (int i = 2; i > 1; i--) {
			url = url + "&PageNo=" + i;
			String response = httpclientHandl.getTest("gb2312", url, "", HOST, "", 80);
			linkList = getLink(response);
			if (ckm.getUpdate(updatedate, "淮北市中级人民法院")) {
				for (int j = 0; j < linkList.size(); j++) {
					ArrayList<Court> list = new ArrayList<Court>();
					String detailResponse = httpclientHandl.getTest("gb2312", detailUrl + linkList.get(j), "", HOST,
							"", 80);
					list = getDetailContent(detailResponse);
					hbzyHandl.insertCourt(list);
				}
			}

		}
	}

	private ArrayList getLink(String content) {

		ArrayList list = new ArrayList();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div.listbox").get(0).select("a.title");
		updatedate = doc.select("span.info").get(0).text();
		if (updatedate.indexOf("日期") > -1) {
			updatedate = updatedate.substring(updatedate.indexOf("日期") + 3, updatedate.indexOf("日期") + 13);
		}
		for (int i = 0; i < eles.size(); i++) {
			list.add(eles.get(i).attr("href"));
		}

		return list;

	}

	private ArrayList<Court> getDetailContent(String content) {

		ArrayList<Court> list = new ArrayList<Court>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div.content").get(0).getElementsByTag("table ").get(0).getElementsByTag("tr");
		eles = eles.get(0).getElementsByTag("table ").get(0).getElementsByTag("tr");
		int index = doc.select("div.info").text().indexOf("-");

		String year = doc.select("div.info").text().substring(index - 4, index);
		String startTime = "";
		for (int i = 0; i < eles.size();) {
			if (eles.get(i).getElementsByTag("td").text().indexOf("案     号") > -1) {
				i++;
				continue;
			}

			Court court = new Court();
			int columsize = eles.get(i).getElementsByTag("td").size();
			if (!eles.get(i).getElementsByTag("td").text().contains("案     号")) {
				if (columsize == 7) {
					startTime = year + "年" + eles.get(i).getElementsByTag("td").get(0).text();
					// 案号
					court.setCase_no(eles.get(i).getElementsByTag("td").get(1).text());
					// 案由
					court.setCase_res(eles.get(i).getElementsByTag("td").get(2).text());
					// 原告
					court.setPlaintiff(eles.get(i).getElementsByTag("td").get(3).text());
					// 被告
					court.setDefandant(eles.get(i).getElementsByTag("td").get(4).text());
					// 日期
					court.setStartTime(startTime + "" + eles.get(i).getElementsByTag("td").get(5).text());
					// 法庭
					court.setCourt(eles.get(i).getElementsByTag("td").get(6).text());

				} else {
					// 案号
					court.setCase_no(eles.get(i).getElementsByTag("td").get(0).text());
					// 案由
					court.setCase_res(eles.get(i).getElementsByTag("td").get(1).text());
					// 原告
					court.setPlaintiff(eles.get(i).getElementsByTag("td").get(2).text());
					// 被告
					court.setDefandant(eles.get(i).getElementsByTag("td").get(3).text());
					// 日期
					court.setStartTime(startTime + "" + eles.get(i).getElementsByTag("td").get(4).text());
					// 法庭
					court.setCourt(eles.get(i).getElementsByTag("td").get(5).text());
				}
			}
			court.setDataFrom("淮北市中级人民法院");
			if ((court.getCase_res() != null || court.getPlaintiff() != null) && court.getStartTime() != null) {
				list.add(court);
			}

			i++;
		}

		return list;
	}

	/**
	 * 得到page数
	 * 
	 * @return int
	 * @author lis
	 **/
	public int getPageByjsoup(String content) {

		int page = 0;
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("span.fengye");
		String strPage = eles.get(1).text();
		int start = strPage.indexOf("/");
		strPage = strPage.substring(start + 1);
		start = strPage.indexOf("   ");
		page = Integer.parseInt(strPage.substring(0, start));

		return page;
	}

	public static void main(String ages[]) {
		HbzyCourt ls = new HbzyCourt();
		ls.getInfo();
	}
}
