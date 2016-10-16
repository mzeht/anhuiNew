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
 * 铜陵市中级人民法院
 * 
 * @author litaotao
 * 
 */
@Service
public class TongLingShiCourt {
	private final String host = "www.tlcourt.gov.cn";
	@Autowired
	private CourtDao tlHandl;
	private String updatedate;
	private HttpClientImp httpclientHandl;

	public HttpClientImp getHttpclientHandl() {
		return httpclientHandl;
	}

	public void setHttpclientHandl(HttpClientImp httpclientHandl) {
		this.httpclientHandl = httpclientHandl;
	}

	public void geturl() {
		HttpClientImp hc = new HttpClientImp();
		CheckSiteModule ckm = new CheckSiteModule(tlHandl);
		for (int i = 2; i >1; i--) {
			String msg = hc.getTest("utf-8",
					"http://www.tlcourt.gov.cn/content/channel/53f5b78e9a05c2070266f074/page-" + i + "/", "", host, "",
					80);
			ArrayList link = getHref(msg);
			if (!ckm.getUpdate(updatedate, "铜陵市中级人民法院")) {
				break;
			}
			for (int j = 0; j < link.size() - 1; j++) {
				String detail = hc.getTest("utf-8", "http://www.tlcourt.gov.cn" + String.valueOf(link.get(j)), "",
						host,"",80);
				analyHtmlForDetail(detail);
			}
		}

	}

	// 取网址
	private ArrayList getHref(String content) {
		ArrayList linkList = new ArrayList();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eless = doc.select("ul.is-listnews").get(0).getElementsByTag("li");
		Elements eles = doc.select("ul.is-listnews").get(0).getElementsByTag("a");
		String temp = eless.get(0).text();
		if (temp.indexOf("]") - temp.indexOf("[") > 1) {
			updatedate = temp.substring(temp.indexOf("["), temp.indexOf("]") + 1);
		}
		for (int i = 0; i < eles.size(); i++) {
			linkList.add(eles.get(i).attr("href"));
		}
		return linkList;
	}

	// 取数据
	private void analyHtmlForDetail(String content) {
		ArrayList<Court> array = new ArrayList<Court>();
		Document doc = null;
		doc = Jsoup.parse(content);
		Elements eles = doc.select("div.is-newscontnet").get(0).getElementsByTag("p");

		Court court = new Court();
		court.setDataFrom("铜陵市中级人民法院");
		boolean flag = false;
		for (int i = 0; i < eles.size(); i++) {
			String detail = eles.get(i).text().replaceAll(" ", "").replaceAll("：", ":").replace("被上诉人", "被告")
					.replace("上诉人", "原告").replace("再审申请人", "原告").replace("被申请人", "被告").replaceAll(Jsoup.parse("&nbsp;").text(),"").replaceAll(" ", "");
			if (!detail.contains("合议庭成员")) {
				if (detail.contains("案号") || detail.contains("号")) {

					court.setCase_no(detail.split(":")[1]);
				} else if (detail.contains("案由:")) {

					court.setCase_res(detail.split(":")[1]);
				} else if (detail.contains("诉机关") || detail.contains("原告")) {
					if (court.getPlaintiff() == null) {
						court.setPlaintiff(detail.split(":")[1]);
					} else {
						court.setPlaintiff(court.getPlaintiff() + "，" + detail.split(":")[1]);
					}
				} else if (detail.contains("被告人") || detail.contains("被告")) {
					if (court.getDefandant() == null) {
						court.setDefandant(detail.split(":")[1]);
					} else {
						court.setDefandant(court.getDefandant() + "，" + detail.split(":")[1]);
					}

				} else if (detail.contains("地点") || detail.contains("开庭地点")) {

					court.setCourt(detail.split(":")[1]);
				} else if (detail.contains("时间:")) {

					court.setStartTime(detail.split(":")[1]);
					flag = true;
				}
			}

			if (flag) {
				array.add(court);
				court = new Court();
				court.setDataFrom("铜陵市中级人民法院");
				flag = false;
			}
		}
		tlHandl.insertCourt(array);

		
		
	}

	public static void main(String ages[]) {
		TongLingShiCourt ls = new TongLingShiCourt();
		ls.geturl();
	}
}
