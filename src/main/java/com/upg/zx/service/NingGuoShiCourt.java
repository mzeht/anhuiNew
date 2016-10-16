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
 * 安徽省宁国市人民法院
 * 
 * @author litaotao
 * 
 */
@Service
public class NingGuoShiCourt {
	private final String host = "www.ngfy.gov.cn";
	@Autowired
	private CourtDao ngsHandl;
	private String updatedate;
	private HttpClientImp httpclientHandl;

	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}

	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}

	public void getUrl() {
		HttpClientImp hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(ngsHandl);
		String msg = hc.getTest("gb2312",
				"http://www.ngfy.gov.cn/news_more.asp?lm=&lm2=118&open=_blank&tj=0&hot=0&lryname=&tcolor=999999", "",
				host, "", 80);
		ArrayList link = getHref(msg);
		if (ckm.getUpdate(updatedate, "安徽省宁国市人民法院")) {
			for (int i = 0; i < link.size(); i++) {
				String detail = hc.getTest("gb2312", "http://www.ngfy.gov.cn/" + String.valueOf(link.get(i)), "", host,
						"", 80);
				analyHtmlForDetail(detail);
			}
		}
	}

	public ArrayList<String> getHref(String content) {
		ArrayList<String> linkList = new ArrayList<String>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div.txt").get(0).getElementsByTag("tr");
		String temp = eles.get(0).getElementsByTag("td").text();
		if (temp.indexOf("日") - temp.indexOf("年") > 1) {
			updatedate = temp.substring(temp.indexOf("年") - 4, temp.indexOf("日") + 1);
			updatedate = updatedate.replace("年", "-").replace("月", "-").replace("日", "");
		}
		for (int i = 0; i < eles.size(); i++) {
			linkList.add(eles.get(i).getElementsByTag("a").get(1).attr("href"));
		}
		return linkList;
	}

	public void analyHtmlForDetail(String content) {
		ArrayList<Court> array = new ArrayList<Court>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div.NewsContent").get(0).getElementsByTag("table");
		if (eles.size() > 0) {
			eles = eles.get(0).getElementsByTag("tr");
			Court court = new Court();
			court.setDataFrom("宁国市人民法院");
			for (int i = 1; i < eles.size(); i++) {
				Elements neweles = eles.get(i).getElementsByTag("td");
				for (int j = 0; j < 1; j++) {
					court.setCourt(neweles.get(0).text());
					if (neweles.size() == 5) {
						court.setStartTime(neweles.get(1).text());
						court.setCase_no(neweles.get(2).text());
						court.setCase_res(neweles.get(3).text());
						court.setJudge(neweles.get(4).text());
					} else {
						court.setStartTime(neweles.get(2).text());
						court.setCase_no(neweles.get(3).text());
						court.setCase_res(neweles.get(4).text());
						court.setJudge(neweles.get(5).text());
					}
					array.add(court);
					court = new Court();
					System.out.println("++++++++++++++++++++");
				}
			}
		} else {
			Elements newEles = doc.select("div#NewsContentLabel").select("p");
			Court court = new Court();
			court.setDataFrom("宁国市人民法院");
			for (int i = 0; i < newEles.size(); i++) {
				String msg = newEles.get(i).text();
				msg = msg.replaceAll("　", "").replaceAll(" ", "");
				int startNo = msg.indexOf("案号：");
				int endNo = msg.indexOf("案由");
				int startRes = msg.indexOf("由：");
				int endRes = msg.indexOf("被");
				int startD = msg.indexOf("被");
				int endD = msg.indexOf("承办");
				int startCourt = msg.indexOf("地点");
				int endCourt = msg.indexOf("日期");
				int startTime = msg.indexOf("日期");
				int endTime = msg.indexOf("时间");
				int startP = msg.indexOf("原告");
				int endP = msg.indexOf("被");
				int startJud = msg.indexOf("审判");
				if (startJud == -1 && endD == -1 && startD != -1) {
					court.setPlaintiff(msg.substring(startD, startCourt));

				}
				if (startJud == -1 && startD != -1 && endD != -1) {
					court.setPlaintiff(msg.substring(startD, endD));

				}
				if (startD != -1 && startJud != -1) {
					court.setPlaintiff(msg.substring(startD, startJud));

				}
				if (startNo != -1 && endNo != -1) {
					court.setCase_no(msg.substring(startNo + 3, endNo));// OK

				}
				if (startRes != -1 && endRes != -1) {
					court.setCase_res(msg.substring(startRes + 2, endRes));

				}
				if (startCourt != -1 && endCourt != -1) {
					court.setCourt(msg.substring(startCourt + 3, endCourt));

				}
				if (startTime != -1 && endTime != -1) {
					court.setStartTime(msg.substring(startTime + 3, endTime));

				}
				if (startP != -1 && endP != -1) {
					court.setDefandant(msg.substring(startP, endP));

				}

				array.add(court);
				court = new Court();
				court.setDataFrom("宁国市人民法院");
			}

		}
		ngsHandl.insertCourt(array);
	}

	public static void main(String ages[]) {
		NingGuoShiCourt ls = new NingGuoShiCourt();
		ls.getUrl();
	}

}
